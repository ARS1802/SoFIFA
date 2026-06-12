package filters;

public enum Position implements Filters {
    GK(1, "Goalkeeper", "Goleiro"),
    CB(2, "Centre Back", "Zagueiro central"),
    LB(3, "Left Back", "Lateral esquerdo"),
    RB(4, "Right Back", "Lateral direito"),
    CDM(5, "Central Defensive Midfielder", "Volante / meio-campista defensivo central"),
    CM(6, "Central Midfielder", "Meio-campista central"),
    CAM(7, "Central Attacking Midfielder", "Meia-atacante central / armador"),
    LM(8, "Left Midfielder", "Meia pela esquerda"),
    RM(9, "Right Midfielder", "Meia pela direita"),
    LW(10, "Left Winger", "Ponta esquerda"),
    RW(11, "Right Winger", "Ponta direita"),
    ST(12, "Striker", "Atacante / centroavante");

    private final int weight;
    private final String englishName;
    private final String portugueseName;

    Position(int weight, String englishName, String portugueseName) {
        this.weight = weight;
        this.englishName = englishName;
        this.portugueseName = portugueseName;
    }

    public int weight() {
        return weight;
    }

    public String englishName() {
        return englishName;
    }

    public String portugueseName() {
        return portugueseName;
    }
}
