import java.util.Comparator;


//      Player inicialmente implements Comparable<Player>
//      Entretanto, o metodo abaixo não permite que Atributos sejam passados!
//    @Override
//    public int compareTo(Player o) {
//        return playerAtual.compareTo(outroPlayer, atributos);
//    }

//============================================================
/*
 *       => interface Comparator<T> recebe os atributos
 *       => sobrescreve int compare(T obj1, T obj2) passando os atributos para um metodo prórpio
 *       => este metodo próprio é o compareTo(Player p, Atributos... atributos)
 *       => valorDe() retorna o respectivo atributo de sua respectiva classe.
 *               obs: todos os atributos de Player são do tipo Wrapper Classes, as quais implementam a interface Comaprables.
 *                    Possuindo isso em comum, todas implementam o próprio .compareTo()
 *                    Graças a isso, valorDe() retorna um Comparable da classe respectiva.
 *       => compararValores(Comparable valorAtual, Comparable valorOutro) recebe os atributos de Player e retorna o compareTo() respectivo ao seu tipo.
 */
//===========================================================
public class Player{
    private final Integer playerId;
    private final String shortName;
    private final String longName;
    private final String playerPositions;
    private final Integer overall;
    private final Integer potential;
    private final Double valueEur;
    private final Double wageEur;
    private final Integer age;
    private final Integer heightCm;
    private final Integer weightKg;
    private final Integer clubTeamId;
    private final String clubName;

    Player(String[] s){
        playerId = Atributos.parse("player_id", s[Atributos.PLAYER_ID.indice]);
        shortName = Atributos.parse("short_name", s[Atributos.SHORT_NAME.indice]);
        longName = Atributos.parse("long_name", s[Atributos.LONG_NAME.indice]);
        playerPositions = Atributos.parse("player_positions", s[Atributos.PLAYER_POSITIONS.indice]);
        overall = Atributos.parse("overall", s[Atributos.OVERALL.indice]);
        potential = Atributos.parse("potential", s[Atributos.POTENTIAL.indice]);
        valueEur = Atributos.parse("value_eur", s[Atributos.VALUE_EUR.indice]);
        wageEur = Atributos.parse("wage_eur", s[Atributos.WAGE_EUR.indice]);
        age = Atributos.parse("age", s[Atributos.AGE.indice]);
        heightCm = Atributos.parse("height_cm", s[Atributos.HEIGHT_CM.indice]);
        weightKg = Atributos.parse("weight_kg", s[Atributos.WEIGHT_KG.indice]);
        clubTeamId = Atributos.parse("club_team_id", s[Atributos.CLUB_TEAM_ID.indice]);
        clubName = Atributos.parse("club_name", s[Atributos.CLUB_NAME.indice]);
    }

    public static Comparator<Player> comparandoPor(Atributos... atributos){
        Comparator<Player> comparator = new Comparator<Player>() {
            @Override
            public int compare(Player playerAtual, Player outroPlayer) {
                return playerAtual.compareTo(outroPlayer, atributos);
            }
        };
        return comparator;
        //return (playerAtual, outroPlayer) -> playerAtual.compareTo(outroPlayer, atributos);
    }

    public int compareTo(Player p, Atributos... atributos){
        if(atributos == null || atributos.length == 0){
            return compararValores(playerId, p.playerId);
        }

        for(Atributos atributo : atributos){
            int resultado = compararValores(valorDe(atributo), p.valorDe(atributo));

            if(resultado != 0){
                return resultado;
            }
        }

        return compararValores(playerId, p.playerId);
    }

    private Comparable<?> valorDe(Atributos atributo){
        switch(atributo){
            case PLAYER_ID:
                return playerId;
            case SHORT_NAME:
                return shortName;
            case LONG_NAME:
                return longName;
            case PLAYER_POSITIONS:
                return playerPositions;
            case OVERALL:
                return overall;
            case POTENTIAL:
                return potential;
            case VALUE_EUR:
                return valueEur;
            case WAGE_EUR:
                return wageEur;
            case AGE:
                return age;
            case HEIGHT_CM:
                return heightCm;
            case WEIGHT_KG:
                return weightKg;
            case CLUB_TEAM_ID:
                return clubTeamId;
            case CLUB_NAME:
                return clubName;
            default:
                throw new IllegalArgumentException("Atributo sem comparacao configurada: " + atributo);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compararValores(Comparable valorAtual, Comparable valorOutro){
        if(valorAtual == null && valorOutro == null){
            return 0;
        }
        if(valorAtual == null){
            return 1;
        }
        if(valorOutro == null){
            return -1;
        }

        return valorAtual.compareTo(valorOutro);
    }

    @Override
    public String toString(){
        return csvValue(playerId) + "," +
                csvValue(shortName) + "," +
                csvValue(longName) + "," +
                csvValue(playerPositions) + "," +
                csvValue(overall) + "," +
                csvValue(potential) + "," +
                csvValue(valueEur) + "," +
                csvValue(wageEur) + "," +
                csvValue(age) + "," +
                csvValue(heightCm) + "," +
                csvValue(weightKg) + "," +
                csvValue(clubTeamId) + "," +
                csvValue(clubName);
    }

    private String csvValue(Object value){
        if(value == null){
            return "";
        }

        String text = value.toString();

        if(text.contains(",") || text.contains("\"") || text.contains("\n")){
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }

        return text;
    }
}
