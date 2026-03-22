package models;

import java.util.Locale;

public enum RoomStatus {

    EMPTY,
    RENTED;

    public String dbValue() {
        return name();
    }

    /**
     * Chuẩn hóa chuỗi từ DB hoặc form về một giá trị enum hợp lệ.
     */
    public static RoomStatus parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return EMPTY;
        }
        try {
            return RoomStatus.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return EMPTY;
        }
    }
}
