package domain.match;

import domain.set.Set;
import domain.set.SetScore;
import domain.tiebreak.TieBreak;
import exceptions.NoSuchPlayerException;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Match {

    private Set set;
    private String winner;

    public Match(String playerOne, String playerTwo) {
        this.set = new Set(playerOne, playerTwo);
    }

    public void scores(String player) throws NoSuchPlayerException {
        if (Objects.isNull(winner)) {
            this.set.scores(player);
            if (isMatchWinnable(player)) {
                endMatch(player);
            }
        }
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public Optional<String> getWinner() {
        return Optional.ofNullable(winner);
    }

    private boolean isMatchWinnable(String player) {
        SetScore lastSetScore = getLastSetScore(player);
        String setWinner = this.set.getWinner().orElse(null);
        AtomicReference<String> tieBreakWinner = new AtomicReference<>();
        this.set.getTieBreak().flatMap(TieBreak::getWinner).ifPresent(tieBreakWinner::set);

        return (lastSetScore.equals(SetScore.SEVEN) && Objects.isNull(setWinner)) || tieBreakWinner.get().equals(player);
    }

    private SetScore getLastSetScore(String player) {
        return this.set.getPlayerSetScores(player).get(this.set.getPlayerSetScores(player).size() - 1);
    }

    private void endMatch(String player) {
        this.winner = player;
    }
}
