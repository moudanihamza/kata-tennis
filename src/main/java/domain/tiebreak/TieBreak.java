package domain.tiebreak;

import exceptions.NoSuchPlayerException;

import java.util.*;
import java.util.stream.Collectors;

public class TieBreak {
    private Map<String, List<Integer>> tieBreakScores;
    private String winner;

    public TieBreak(String playerOne, String playerTwo) {
        this.tieBreakScores = new HashMap<>();
        this.tieBreakScores.put(playerOne, new ArrayList<>(Collections.singletonList(0)));
        this.tieBreakScores.put(playerTwo, new ArrayList<>(Collections.singletonList(0)));
    }

    public void scores(String player) throws NoSuchPlayerException {
        if (Objects.isNull(winner)) {
            final List<Integer> scores = Optional.ofNullable(this.tieBreakScores.get(player)).orElseThrow(NoSuchPlayerException::new);
            incrementScore(player, scores);
            if (isTieBreakWinnable(player)) {
                endTieBreak(player);
            }
        }

    }

    private void incrementScore(String player, List<Integer> scores) {
        scores.add(getLastScore(scores) + 1);
        this.tieBreakScores.replace(player, scores);
        this.tieBreakScores.entrySet().stream()
                .filter(o -> !o.getKey().equals(player))
                .forEach(o -> o.getValue().add(getLastScore(o.getValue())));
    }

    private int getLastScore(List<Integer> scores) {
        return scores.get(scores.size() - 1);
    }

    private boolean isTieBreakWinnable(String player) {
        final int playerOneScore = getLastScore(this.tieBreakScores.get(player));
        final int playerTwoScore = getLastScore(
                this.tieBreakScores.entrySet()
                        .stream()
                        .filter(o -> !o.getKey().equals(player)).map(Map.Entry::getValue)
                        .flatMap(Collection::stream).collect(Collectors.toList())
        );
        return playerOneScore >= 7 && (playerOneScore - playerTwoScore) >= 2;
    }

    private void endTieBreak(String player) {
        this.winner = player;
    }

    public List<Integer> getPlayerTieBreakScores(String player) {
        return Optional.ofNullable(this.tieBreakScores.get(player)).orElse(new ArrayList<>());
    }

    public void setPlayerTieBreakScores(String player, List<Integer> scores) {
        this.tieBreakScores.replace(player, scores);
    }

    public Optional<String> getWinner() {
        return Optional.ofNullable(winner);
    }
}
