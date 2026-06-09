import java.time.LocalDate;

public enum Atributos {
    SOFIFA_ID("sofifa_id", Integer.class,0),
    PLAYER_URL("player_url", String.class, 1),
    SHORT_NAME("short_name", String.class, 2),
    LONG_NAME("long_name", String.class,3),
    OVERALL("overall", Integer.class,5),
    POTENTIAL("potential", Integer.class,6),
    VALUE_EUR("value_eur", Long.class,7),
    WAGE_EUR("wage_eur", Long.class,8),
    AGE("age", Integer.class,9),
    DOB("dob", LocalDate.class,10),
    HEIGHT_CM("height_cm", Integer.class,11),
    WEIGHT_KG("weight_kg", Integer.class,12),
    PLAYER_FACE_URL("player_face_url", String.class,105);

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
        if (this.tipo == LocalDate.class) {
            return LocalDate.parse(data);
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
