import java.time.LocalDate;

public class Player implements Comparable<Player> {
    private Integer sofifaId;
    private String playerUrl;
    private String shortName;
    private String longName;
    private Integer overall;
    private Integer potential;
    private Long valueEur;
    private Long wageEur;
    private Integer age;
    private LocalDate dob;
    private Integer heightCm;
    private Integer weightKg;
    private String playerFaceUrl;

    Player(String[] s){
        sofifaId = Atributos.parse("sofifa_id", s[Atributos.SOFIFA_ID.indice]);
        playerUrl = Atributos.parse("player_url", s[Atributos.PLAYER_URL.indice]);
        shortName = Atributos.parse("short_name", s[Atributos.SHORT_NAME.indice]);
        longName = Atributos.parse("long_name", s[Atributos.LONG_NAME.indice]);
        overall = Atributos.parse("overall", s[Atributos.OVERALL.indice]);
        potential = Atributos.parse("potential", s[Atributos.POTENTIAL.indice]);
        valueEur = Atributos.parse("value_eur", s[Atributos.VALUE_EUR.indice]);
        wageEur = Atributos.parse("wage_eur", s[Atributos.WAGE_EUR.indice]);
        age = Atributos.parse("age", s[Atributos.AGE.indice]);
        dob = Atributos.parse("dob", s[Atributos.DOB.indice]);
        heightCm = Atributos.parse("height_cm", s[Atributos.HEIGHT_CM.indice]);
        weightKg = Atributos.parse("weight_kg", s[Atributos.WEIGHT_KG.indice]);
        playerFaceUrl = Atributos.parse("player_face_url", s[Atributos.PLAYER_FACE_URL.indice]);
    }

    @Override
    public int compareTo(Player o) {
        return 1;
    }
}
