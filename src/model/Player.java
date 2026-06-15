package model;

import filters.Atributes;
import filters.Filters;
import filters.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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
    private final String playerFaceUrl;

    public Player(String[] s){
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
        playerFaceUrl = Atributes.parse("player_face_url", s[Atributes.PLAYER_FACE_URL.index]);
    }

    // Comparacao principal usada por listas, tabelas e AVL.
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
            return compareValues(playerId, otherPlayer.playerId);
        }

        Position[] selectedPositions = selectedPositionsFrom(filters);
        int positionResult = comparePositions(otherPlayer, selectedPositions);

        if(positionResult != 0){
            return positionResult;
        }

        for(Filters filter : filters){
            if(filter instanceof Position){
                continue;
            }

            int result = compareAttributeFilter(filter, otherPlayer);

            if(result != 0){
                return result;
            }
        }

        return compareValues(playerId, otherPlayer.playerId);
    }

    // Regras publicas de posicao.
    public boolean playsAs(Position position){
        if(position == null || playerPositions == null || playerPositions.isBlank()){
            return false;
        }

        String positions = "," + playerPositions.replace(" ", "") + ",";
        return positions.contains("," + position.name() + ",");
    }

    public boolean matchesAnyPosition(Position... positions){
        if(positions == null || positions.length == 0){
            return true;
        }

        for(Position position : positions){
            if(playsAs(position)){
                return true;
            }
        }

        return false;
    }

    public int comparePositions(Player otherPlayer, Position... positions){
        if(positions == null || positions.length == 0){
            return 0;
        }

        int currentMatchedPositions = selectedPositionCount(positions);
        int otherMatchedPositions = otherPlayer.selectedPositionCount(positions);
        int matchedPositionsResult = Integer.compare(otherMatchedPositions, currentMatchedPositions);

        if(matchedPositionsResult != 0){
            return matchedPositionsResult;
        }

        int positionAmountResult = Integer.compare(positionAmount(), otherPlayer.positionAmount());

        if(positionAmountResult != 0){
            return positionAmountResult;
        }

        for(Position position : positions){
            int result = comparePosition(position, otherPlayer);

            if(result != 0){
                return result;
            }
        }

        return 0;
    }

    // Valores usados pela tabela e painel de detalhes.
    public Object getValue(Atributes attribute){
        return valueOf(attribute);
    }

    public String getDisplayValue(Atributes attribute){
        Object value = getValue(attribute);

        if(value == null){
            return "";
        }

        return value.toString();
    }

    // Exportacao CSV.
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
                csvValue(clubName) + "," +
                csvValue(playerFaceUrl);
    }

    // Helpers de filtros.
    private Position[] selectedPositionsFrom(Filters... filters){
        List<Position> selectedPositions = new ArrayList<>();

        for(Filters filter : filters){
            if(filter instanceof Position){
                selectedPositions.add((Position) filter);
            }
        }

        return selectedPositions.toArray(new Position[0]);
    }

    private int compareAttributeFilter(Filters filter, Player otherPlayer){
        if(filter instanceof Atributes){
            Atributes attribute = (Atributes) filter;
            return compareValues(valueOf(attribute), otherPlayer.valueOf(attribute));
        }

        throw new IllegalArgumentException("Filtro sem comparacao configurada: " + filter);
    }

    // Helpers de posicao.
    private int selectedPositionCount(Position... positions){
        int selectedPositionCount = 0;

        for(Position position : positions){
            if(playsAs(position)){
                selectedPositionCount++;
            }
        }

        return selectedPositionCount;
    }

    private int comparePosition(Position position, Player otherPlayer){
        return Integer.compare(otherPlayer.positionPriority(position), this.positionPriority(position));
    }

    private int positionPriority(Position position){
        if(!playsAs(position)){
            return 0;
        }

        if(normalizedPositions().equals(position.name())){
            return 2;
        }

        return 1;
    }

    private int positionAmount(){
        if(playerPositions == null || playerPositions.isBlank()){
            return 0;
        }

        return normalizedPositions().split(",").length;
    }

    private String normalizedPositions(){
        return playerPositions.replace(" ", "");
    }

    // Helpers de atributos e valores.
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
            case PLAYER_FACE_URL:
                return playerFaceUrl;
            default:
                throw new IllegalArgumentException("Atributo sem comparacao configurada: " + attribute);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compareValues(Comparable currentValue, Comparable otherValue){
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

    // Helper de escrita CSV.
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
