import filters.Atributes;
import filters.Filters;
import filters.Position;

import java.util.Comparator;


//      Player inicialmente implements Comparable<Player>
//      Entretanto, o metodo abaixo não permite que Atributes sejam passados!
//    @Override
//    public int compareTo(Player otherPlayer) {
//        return currentPlayer.compareTo(otherPlayer, filters);
//    }

//============================================================
/*
 *       => interface Comparator<T> recebe os atributos
 *       => sobrescreve int compare(T obj1, T obj2) passando os atributos para um metodo prórpio
 *       => este metodo próprio é o compareTo(Player otherPlayer, Filters... filters)
 *       => valueOf() retorna o respectivo atributo de sua respectiva classe.
 *               obs: todos os atributos de Player são do tipo Wrapper Classes, as quais implementam a interface Comaprables.
 *                    Possuindo isso em comum, todas implementam o próprio .compareTo()
 *                    Graças a isso, valueOf() retorna um Comparable da classe respectiva.
 *       => compareFilterVaues(Comparable currentValue, Comparable otherValue) recebe os atributos de Player e retorna o compareTo() respectivo ao seu tipo.
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
        playerId = Atributes.parse("player_id", s[Atributes.PLAYER_ID.index]);
        shortName = Atributes.parse("short_name", s[Atributes.SHORT_NAME.index]);
        longName = Atributes.parse("long_name", s[Atributes.LONG_NAME.index]);
        playerPositions = Atributes.parse("player_positions", s[Atributes.PLAYER_POSITIONS.index]);
        overall = Atributes.parse("overall", s[Atributes.OVERALL.index]);
        potential = Atributes.parse("potential", s[Atributes.POTENTIAL.index]);
        valueEur = Atributes.parse("value_eur", s[Atributes.VALUE_EUR.index]);
        wageEur = Atributes.parse("wage_eur", s[Atributes.WAGE_EUR.index]);
        age = Atributes.parse("age", s[Atributes.AGE.index]);
        heightCm = Atributes.parse("height_cm", s[Atributes.HEIGHT_CM.index]);
        weightKg = Atributes.parse("weight_kg", s[Atributes.WEIGHT_KG.index]);
        clubTeamId = Atributes.parse("club_team_id", s[Atributes.CLUB_TEAM_ID.index]);
        clubName = Atributes.parse("club_name", s[Atributes.CLUB_NAME.index]);
    }

    public static Comparator<Player> filters(Filters... filters){
        Comparator<Player> comparator = new Comparator<Player>() {
            @Override
            public int compare(Player currentPlayer, Player otherPlayer) {
                return currentPlayer.compareTo(otherPlayer, filters);
            }
        };
        return comparator;
        //return (currentPlayer, otherPlayer) -> currentPlayer.compareTo(otherPlayer, filters);
    }

    public int compareTo(Player otherPlayer, Filters... filters){
        if(filters == null || filters.length == 0){
            return compareFilterVaues(playerId, otherPlayer.playerId);
        }

        for(Filters filter : filters){
            int result = compareFilter(filter, otherPlayer);

            if(result != 0){
                return result;
            }
        }

        return compareFilterVaues(playerId, otherPlayer.playerId);
    }

    public boolean playsAs(Position position){
        if(playerPositions == null || playerPositions.isBlank()){
            return false;
        }

        String[] positions = playerPositions.split(", ");

        for(String currentPosition : positions){
            if(currentPosition.equals(position.name())){
                return true;
            }
        }

        return false;
    }

    private int compareFilter(Filters filter, Player otherPlayer){
        if(filter instanceof Atributes){
            Atributes attribute = (Atributes) filter;
            return compareFilterVaues(valueOf(attribute), otherPlayer.valueOf(attribute));
        }

        if(filter instanceof Position){
            Position position = (Position) filter;
            return Boolean.compare(otherPlayer.playsAs(position), this.playsAs(position));
        }

        throw new IllegalArgumentException("Filtro sem comparacao configurada: " + filter);
    }

    private Comparable<?> valueOf(Atributes attribute){
        switch(attribute){
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
                throw new IllegalArgumentException("Atributo sem comparacao configurada: " + attribute);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compareFilterVaues(Comparable currentValue, Comparable otherValue){
        if(currentValue == null && otherValue == null){
            return 0;
        }
        if(currentValue == null){
            return 1;
        }
        if(otherValue == null){
            return -1;
        }

        return currentValue.compareTo(otherValue);
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
