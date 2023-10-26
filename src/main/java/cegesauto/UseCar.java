package cegesauto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record UseCar(LocalDateTime dateTime, String plateNumber, String personId, int km, Direction direction) {

    public String toString() {
        LocalTime time = LocalTime.of(dateTime.getHour(), dateTime.getMinute());
        return String.format("%tR %s %s %s", time, plateNumber, personId, direction.getValue());
    }
}
