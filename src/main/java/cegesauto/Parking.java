package cegesauto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Parking {

    private final List<UseCar> useCarList = new ArrayList<>();

    public void loadFromFile(Path path) {
        String line;
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            while ((line = bufferedReader.readLine()) != null) {
                createAndAddUseCarToList(line);
            }
        } catch (IOException ioException) {
            throw new IllegalStateException("Can not read file", ioException);
        }
    }

    public UseCar getLastUseCarOut() {
        return useCarList.stream()
                .filter(uc -> uc.direction() == Direction.OUT)
                .max(Comparator.comparing(UseCar::dateTime))
                .orElseThrow(() -> new IllegalArgumentException("Nem volt kihajtó autó."));
    }

    public void printUseCarsByDay(int day) {
        useCarList.stream()
                .filter(useCar -> useCar.dateTime().getDayOfMonth() == day)
                .forEach(System.out::println);
    }

    public int getNumberOfMissingCars() {
//        return useCarList.stream().filter(useCar -> useCar.direction() == Direction.OUT).count() - useCarList.stream().filter(useCar -> useCar.direction() == Direction.IN).count();
        int counter = 0;
        for (UseCar useCar : useCarList) {
            counter += useCar.direction() == Direction.OUT ? 1 : -1;
        }
        return counter;
    }

    public void printCarsWithKm() {
        Map<String, Integer> cars = getCarsWithKm();
        cars.keySet()
                .forEach(pn -> System.out.printf("%s %d km%n", pn, cars.get(pn)));
    }

    public void printLongestDistance() {
        Set<String> persons = getPersons();
        int max = 0;
        String maxPerson = null;
        for (String person : persons) {
            if (getLongestDistanceByPerson(person) > max) {
                maxPerson = person;
                max = getLongestDistanceByPerson(person);
            }
        }
        System.out.printf("Leghosszabb út: %d km, személy: %s", max, maxPerson);
    }

    public void writeWaybillToFile(String plateNumber) {
        String fileName = plateNumber.concat("_menetlevel.txt");
        Path path = Path.of("src", "main", "resources", fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            writeLineTofile(plateNumber, bufferedWriter);
        } catch (IOException ioException) {
            throw new IllegalStateException("Can not write file", ioException);
        }
    }

    private void writeLineTofile(String plateNumber, BufferedWriter bufferedWriter) throws IOException {
        StringBuilder line = new StringBuilder();
        for (UseCar useCar : useCarList) {
            line = createAndWriteLine(plateNumber, line, bufferedWriter, useCar);
        }
        bufferedWriter.write(line.toString());
    }

    private static StringBuilder createAndWriteLine(String plateNumber, StringBuilder line, BufferedWriter bufferedWriter, UseCar useCar) throws IOException {
        LocalTime time = LocalTime.of(useCar.dateTime().getHour(), useCar.dateTime().getMinute());
        if (useCar.plateNumber().equals(plateNumber)) {
            if (useCar.direction() == Direction.OUT) {
                line.append(String.format("%s \t %d. \t %tR \t %d km\t", useCar.personId(), useCar.dateTime().getDayOfMonth(), time, useCar.km()));
            }
            if (useCar.direction() == Direction.IN) {
                line.append(String.format("\t %d. \t %tR \t %d km", useCar.dateTime().getDayOfMonth(), time, useCar.km()));
                bufferedWriter.write(line.toString().concat(System.lineSeparator()));
                line = new StringBuilder();
            }
        }
        return line;
    }

    private Integer getLongestDistanceByPerson(String person) {
        int start = 0;
        int finish = 0;
        int max = 0;
        for (UseCar useCar : useCarList) {
            if (useCar.personId().equals(person)) {
                if (useCar.direction() == Direction.OUT) start = useCar.km();

                if (useCar.direction() == Direction.IN) {
                    finish = useCar.km();
                    int distance = finish - start;
                    max = Math.max(distance, max);
                }
            }
        }
        return max;
    }

    private Set<String> getPersons() {
        return useCarList.stream().map(UseCar::personId).collect(Collectors.toSet());
    }

    private Map<String, Integer> getCarsWithKm() {
        List<UseCar> useCarsReversed = new ArrayList<>(useCarList);
        Collections.reverse(useCarsReversed);

        Map<String, Integer> carsMin = new HashMap<>();
        useCarsReversed
                .forEach(useCar -> carsMin.put(useCar.plateNumber(), useCar.km()));

        Map<String, Integer> carsMax = new HashMap<>();
        useCarList
                .forEach(useCar -> carsMax.put(useCar.plateNumber(), useCar.km()));

        Map<String, Integer> cars = new TreeMap<>();
        carsMax.keySet()
                .forEach(pn -> cars.put(pn, carsMax.get(pn) - carsMin.get(pn)));

        return cars;
    }


    private void createAndAddUseCarToList(String line) {
        String[] useCarArray = line.split(" ");
        int day = Integer.parseInt(useCarArray[0]);
        LocalTime time = LocalTime.parse(useCarArray[1], DateTimeFormatter.ofPattern("HH:mm"));
        String plateNumber = useCarArray[2];
        String personId = useCarArray[3];
        int km = Integer.parseInt(useCarArray[4]);
        Direction direction = Integer.parseInt(useCarArray[5]) == 0 ? Direction.OUT : Direction.IN;
        LocalDate date = LocalDate.of(2000, 1, day);
        useCarList.add(new UseCar(LocalDateTime.of(date, time), plateNumber, personId, km, direction));
    }
}
