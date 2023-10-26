package cegesauto;

public enum Direction {
    IN ("be"),
    OUT ("ki");

    private final String value;

    Direction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
