package filters;

public enum Position implements Filters {
    GK("Goalkeeper", "Goleiro"),
    CB("Centre Back", "Zagueiro central"),
    LB("Left Back", "Lateral esquerdo"),
    RB("Right Back", "Lateral direito"),
    CDM("Central Defensive Midfielder", "Volante / meio-campista defensivo central"),
    CM("Central Midfielder", "Meio-campista central"),
    CAM("Central Attacking Midfielder", "Meia-atacante central / armador"),
    LM("Left Midfielder", "Meia pela esquerda"),
    RM("Right Midfielder", "Meia pela direita"),
    LW("Left Winger", "Ponta esquerda"),
    RW("Right Winger", "Ponta direita"),
    ST("Striker", "Atacante / centroavante");

    private final String englishName;
    private final String portugueseName;

    Position(String englishName, String portugueseName) {
        this.englishName = englishName;
        this.portugueseName = portugueseName;
    }

    public String englishName() {
        return englishName;
    }

    public String portugueseName() {
        return portugueseName;
    }
}
