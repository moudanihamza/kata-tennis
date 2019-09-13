package domain.game;

public enum GameScore {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("ADVANTAGE"),
    DEUCE("DEUCE");
    private boolean deuceMode;

    GameScore(String value) {
    }

    public GameScore next() {
        if (isDeuceModeActivated()) {
            if (this.equals(FORTY) || this.equals(DEUCE)) {
                GameScore gameScore = ADVANTAGE;
                gameScore.activateDeuceMode();
                return gameScore;
            }
            return this;
        } else {
            if (ordinal() == values().length - 1 || this.equals(GameScore.ADVANTAGE) || this.equals(GameScore.DEUCE))
                return this;
            return values()[ordinal() + 1];
        }
    }

    public GameScore previous() {
        if (isDeuceModeActivated()) {
            if (this.equals(DEUCE)) {
                GameScore gameScore = FORTY;
                gameScore.activateDeuceMode();
                return gameScore;
            }
            return this;
        } else {
            if (ordinal() == 0 || this.equals(GameScore.ADVANTAGE) || this.equals(GameScore.DEUCE))
                return this;
            return values()[ordinal() - 1];
        }
    }

    public void activateDeuceMode() {
        this.deuceMode = true;
    }

    public void deactivateDeuceMode() {
        this.deuceMode = false;
    }

    public boolean isDeuceModeActivated() {
        return this.deuceMode;
    }

}
