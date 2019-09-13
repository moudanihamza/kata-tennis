package domain.game;

import exceptions.NoSuchPlayerException;

import java.util.*;

public class Game {
    private Map<String, List<GameScore>> gameScores;
    private String winner;

    public Game(String playerOne, String playerTwo) {
        this.gameScores = new HashMap<>();
        this.gameScores.put(playerOne, new ArrayList<>(Collections.singletonList(GameScore.ZERO)));
        this.gameScores.put(playerTwo, new ArrayList<>(Collections.singletonList(GameScore.ZERO)));
    }

    public void scores(String player) throws NoSuchPlayerException {
        List<GameScore> gameScores = Optional.ofNullable(this.gameScores.get(player))
                .orElseThrow(NoSuchPlayerException::new);

        if (isGameWinnable(getLastGameScore(gameScores))) {
            endGame(player);
        } else {
            gameScores.add(getLastGameScore(gameScores).next());
            this.gameScores.replace(player, gameScores);
            this.gameScores.entrySet().stream()
                    .filter(o -> !o.getKey().equals(player))
                    .forEach(o -> o.getValue().add(getLastGameScore(o.getValue())));
        }
    }

    private boolean isGameWinnable(GameScore score) {
        return score.equals(GameScore.FORTY);
    }

    private void endGame(String player) {
        this.winner = player;
        this.gameScores.forEach((key, value) -> value.add(GameScore.ZERO));
    }

    private GameScore getLastGameScore(List<GameScore> gameScores) {
        return gameScores.get(gameScores.size() - 1);
    }

    public Optional<String> getWinner() {
        return Optional.ofNullable(winner);
    }

    public void SetPlayerScore(String player, List<GameScore> gameScores) {
        this.gameScores.replace(player, gameScores);
    }

    public List<GameScore> getPlayerScores(String player) {
        return Optional.ofNullable(this.gameScores.get(player))
                .orElse(new ArrayList<>());
    }

}
