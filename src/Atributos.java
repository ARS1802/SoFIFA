//===========================
/*
    String colunaCsv -> nome das colunas em input/FC26_20250921.csv
    Class<?> tipo -> se o dado é Integer, String ou Double
    int indice -> cada linha de input/FC26_20250921.csv se torna um Array
                    [239085, https://sofifa.com/player/239085/..., 26, ...]
*/
//===========================

public enum Atributos {
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

    private final String colunaCsv;
    private final Class<?> tipo;
    public final int indice;

    Atributos(String colunaCsv, Class<?> tipo, int indice) {
        this.colunaCsv = colunaCsv;
        this.tipo = tipo;
        this.indice = indice;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String tipo, String data) {
        for (Atributos atributo : values()) {
            if (atributo.colunaCsv.equals(tipo)) {
                return (T) atributo.parse(data);
            }
        }
        throw new IllegalArgumentException("Coluna CSV desconhecida: " + tipo);
    }

    private Object parse(String data) {
        if (this.tipo == String.class) {
            return data;
        }
        if (data == null || data.isBlank()) {
            return null;
        }
        if (this.tipo == Integer.class) {
            return Integer.parseInt(data);
        }
        if (this.tipo == Long.class) {
            return Long.parseLong(data);
        }
        if (this.tipo == Double.class) {
            return Double.parseDouble(data);
        }
        throw new IllegalArgumentException("Tipo sem parse configurado: " + this.tipo.getName());
    }

    public String colunaCsv() {
        return colunaCsv;
    }

    public Class<?> tipo() {
        return tipo;
    }
}
