package cegesauto;

import java.nio.file.Path;
import java.util.Scanner;

public class ParkingMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parking parking = new Parking();


//      1. Olvassa be és tárolja el az autok.txt fájl tartalmát!

        parking.loadFromFile(Path.of("src", "main", "resources", "autok.txt"));


//      2. Adja meg, hogy melyik autót vitték el utoljára a parkolóból! Az eredményt a mintának
//         megfelelően írja a képernyőre!

        UseCar useCarLast = parking.getLastUseCarOut();

        System.out.println("2. feladat");
        System.out.printf("%d. nap rendszám: %s", useCarLast.dateTime().getDayOfMonth(), useCarLast.plateNumber()
                .concat(System.lineSeparator()));


//      3. Kérjen be egy napot és írja ki a képernyőre a minta szerint, hogy mely autókat vitték ki és
//         hozták vissza az adott napon!

        System.out.println(System.lineSeparator().concat("3. feladat"));
        System.out.print("Nap: ");
        int day = scanner.nextInt();
        System.out.printf("Forgalom a(z) %d. napon: ", day);
        System.out.println();
        parking.printUseCarsByDay(day);


//      4. Adja meg, hogy hány autó nem volt bent a hónap végén a parkolóban!

        System.out.println(System.lineSeparator().concat("4. feladat"));
        System.out.printf("A hónap végén %d autót nem hoztak vissza.", parking.getNumberOfMissingCars());
        System.out.println();


//      5. Készítsen statisztikát, és írja ki a képernyőre mind a 10 autó esetén az ebben a hónapban
//         megtett távolságot kilométerben! A hónap végén még kint lévő autók esetén az utolsó
//         rögzített kilométerállással számoljon! A kiírásban az autók sorrendje tetszőleges lehet.

        System.out.println(System.lineSeparator().concat("5. feladat"));
        parking.printCarsWithKm();


//      6. Határozza meg, melyik személy volt az, aki az autó egy elvitele alatt a leghosszabb
//         távolságot tette meg! A személy azonosítóját és a megtett kilométert a minta szerint írja a
//         képernyőre! (Több legnagyobb érték esetén bármelyiket kiírhatja.)

        System.out.println(System.lineSeparator().concat("6. feladat"));
        parking.printLongestDistance();
        System.out.println();


//      7. Az autók esetén egy havi menetlevelet kell készíteni! Kérjen be a felhasználótól egy
//         rendszámot! Készítsen egy X_menetlevel.txt állományt, amelybe elkészíti az adott
//         rendszámú autó menetlevelét! (Az X helyére az autó rendszáma kerüljön!) A fájlba
//         soronként tabulátorral elválasztva a személy azonosítóját, a kivitel időpontját (nap.
//         óra:perc), a kilométerszámláló állását, a visszahozatal időpontját (nap. óra:perc), és
//         a kilométerszámláló állását írja a minta szerint! (A tabulátor karakter ASCII-kódja: 9.)

        System.out.println(System.lineSeparator().concat("7. feladat"));
        System.out.print("Rendszám: ");
        String plateNumber = scanner.next();

        parking.writeWaybillToFile(plateNumber);
        System.out.println("Menetlevél kész.");
    }
}
