package springboot.giftledger.enums;

public enum EventType {
    WEDDING("결혼"),
    FUNERAL("장례"),
    BIRTHDAY("생일"),
    ETC("기타");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
