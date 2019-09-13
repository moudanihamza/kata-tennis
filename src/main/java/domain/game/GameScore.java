package domain.game;

public enum GameScore {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40");

    GameScore(String value) {
    }

    public GameScore next() {
        if (ordinal() == values().length - 1)
            return this;
        return values()[ordinal() + 1];
    }

    public GameScore previous() {
        if (ordinal() == 0)
            return this;
        return values()[ordinal() - 1];
    }

}
