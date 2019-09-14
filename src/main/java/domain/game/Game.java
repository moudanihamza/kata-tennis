package domain.game;

import exceptions.NoSuchPlayerException;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Map<String, List<GameScore>> gameScores;
    private String winner;

    public Game(String playerOne, String playerTwo) {
        this.gameScores = new HashMap<>();
        this.gameScores.put(playerOne, new ArrayList<>(Collections.singletonList(GameScore.ZERO)));
        this.gameScores.put(playerTwo, new ArrayList<>(Collections.singletonList(GameScore.ZERO)));
    }

    public void scores(String player) throws NoSuchPlayerException {
        if (Objects.isNull(this.winner)) {
            List<GameScore> gameScores = Optional.ofNullable(this.gameScores.get(player))
                    .orElseThrow(NoSuchPlayerException::new);

            if (isGameWinnable(getLastGameScore(gameScores))) {
                endGame(player);

            } else if (shouldWeResetScoreToDeuce(player)) {
                this.gameScores.forEach((k, v) -> {
                    GameScore gameScore = GameScore.DEUCE;
                    gameScore.activateDeuceMode();
                    v.add(gameScore);
                });
            } else {
                gameScores.add(getLastGameScore(gameScores).next());
                this.gameScores.replace(player, gameScores);
                if (shouldWeReturnFromDeuceScore()) {
                    this.gameScores.entrySet().stream()
                            .filter(o -> !o.getKey().equals(player))
                            .forEach(o -> o.getValue().add(getLastGameScore(o.getValue()).previous()));
                } else {
                    this.gameScores.entrySet().stream()
                            .filter(o -> !o.getKey().equals(player))
                            .forEach(o -> o.getValue().add(getLastGameScore(o.getValue())));
                }
            }
            activeDeuceModeIfNecessary();
        }
    }

    private boolean shouldWeReturnFromDeuceScore() {
        return this.gameScores.values().stream()
                .filter(o -> getLastGameScore(o).equals(GameScore.ADVANTAGE) || getLastGameScore(o).equals(GameScore.DEUCE)).count() == 2;
    }

    private boolean isGameWinnable(GameScore score) {
        return (score.equals(GameScore.FORTY) && !score.isDeuceModeActivated())
                || score.equals(GameScore.ADVANTAGE);
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

    private void activeDeuceModeIfNecessary() {
        final List<GameScore> gameScores = this.gameScores.values().stream()
                .map(this::getLastGameScore)
                .filter(o -> o.equals(GameScore.FORTY))
                .collect(Collectors.toList());
        if (gameScores.size() == 2) {
            this.gameScores.forEach((key, value) -> getLastGameScore(value).activateDeuceMode());
        }
    }

    private boolean shouldWeResetScoreToDeuce(String player) {
        final GameScore scorer = this.getLastGameScore(this.getPlayerScores(player));
        final GameScore secondPlayer = getLastGameScore(
                this.gameScores.entrySet().stream()
                        .filter(o -> !o.getKey().equals(player))
                        .map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList())
        );
        return scorer.equals(GameScore.FORTY) && secondPlayer.equals(GameScore.ADVANTAGE);
    }

    public  void setPlayerGameScore(String player, List<GameScore> gameScores) {
        this.gameScores.replace(player, gameScores);
    }

}
