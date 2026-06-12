package filters;

//===========================
/*
    String csvColumn -> nome das colunas em input/FC26_20250921.csv
    Class<?> type -> se o dado é Integer, String ou Double
    int index -> cada linha de input/FC26_20250921.csv se torna um Array
                    [239085, https://sofifa.com/player/239085/..., 26, ...]
*/
//===========================

public enum Atributes implements Filters {
    PLAYER_ID("player_id", Integer.class, 0),
    SHORT_NAME("short_name", String.class, 5),
    LONG_NAME("long_name", String.class, 6),
    PLAYER_POSITIONS("player_positions", String.class, 7),
    OVERALL("overall", Integer.class, 8),
    POTENTIAL("potential", Integer.class, 9),
    VALUE_EUR("value_eur", Double.class, 10),
    WAGE_EUR("wage_eur", Double.class, 11),
    AGE("age", Integer.class, 12),
    HEIGHT_CM("height_cm", Integer.class, 14),
    WEIGHT_KG("weight_kg", Integer.class, 15),
    CLUB_TEAM_ID("club_team_id", Integer.class, 19),
    CLUB_NAME("club_name", String.class, 20);

    private final String csvColumn;
    private final Class<?> type;
    public final int index;

    Atributes(String csvColumn, Class<?> type, int index) {
        this.csvColumn = csvColumn;
        this.type = type;
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String csvColumn, String data) {
        for (Atributes attribute : values()) {
            if (attribute.csvColumn.equals(csvColumn)) {
                return (T) attribute.parse(data);
            }
        }
        throw new IllegalArgumentException("Coluna CSV desconhecida: " + csvColumn);
    }

    private Object parse(String data) {
        if (this.type == String.class) {
            return data;
        }
        if (data == null || data.isBlank()) {
            return null;
        }
        if (this.type == Integer.class) {
            return Integer.parseInt(data);
        }
        if (this.type == Long.class) {
            return Long.parseLong(data);
        }
        if (this.type == Double.class) {
            return Double.parseDouble(data);
        }
        throw new IllegalArgumentException("Tipo sem parse configurado: " + this.type.getName());
    }

    public String csvColumn() {
        return csvColumn;
    }

    public Class<?> type() {
        return type;
    }
}
