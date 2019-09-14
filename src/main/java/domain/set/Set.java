package domain.set;

import domain.game.Game;
import domain.tiebreak.TieBreak;
import exceptions.NoSuchPlayerException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Set {
    private Map<String, List<SetScore>> setScores;
    private List<Game> games;
    private String winner;
    private TieBreak tieBreak;

    public Set(String playerOne, String playerTwo) {
        this.setScores = new HashMap<>();
        this.setScores.put(playerOne, new ArrayList<>(Collections.singletonList(SetScore.ZERO)));
        this.setScores.put(playerTwo, new ArrayList<>(Collections.singletonList(SetScore.ZERO)));
        this.games = new ArrayList<>(Collections.singletonList(new Game(playerOne, playerTwo)));
    }

    public void scores(String player) throws NoSuchPlayerException {
        if (Objects.nonNull(tieBreak)) {
            manageSetScoreWhenTieBreakRuleEnabled(player);
        } else {
            manageSetWhenTieBreakRuleDisabled(player);
            enableTieBreakRuleIfNecessary();
        }
    }

    private void manageSetScoreWhenTieBreakRuleEnabled(String player) throws NoSuchPlayerException {
        tieBreak.scores(player);
        String tieBreakWinner = tieBreak.getWinner().orElse(null);
        if (Objects.nonNull(tieBreakWinner)) {
            incrementSetScore(player);
            endSet(player);
        }
    }


    private void manageSetWhenTieBreakRuleDisabled(String player) throws NoSuchPlayerException {
        getLastGame().scores(player);
        boolean isSetScoreIncremented = isSetScoreIncremented(player);
        if (isSetWinnable(player)) {
            endSet(player);
        } else if (shouldPlayNewGame(player) || isSetScoreIncremented) {
            createNewGame();
        }
    }

    private void createNewGame() {
        List<String> players = new ArrayList<>(this.setScores.keySet());
        this.games.add(new Game(players.get(0), players.get(1)));
    }

    private boolean isSetScoreIncremented(String player) {
        AtomicBoolean isSetScoreIncremented = new AtomicBoolean(false);
        final Optional<String> winner = this.getLastGame().getWinner();
        winner.ifPresent(w -> {
            incrementSetScore(player);
            isSetScoreIncremented.set(true);
        });
        return isSetScoreIncremented.get();
    }

    private Game getLastGame() {
        return this.games.get(this.games.size() - 1);
    }

    private SetScore getLastSetScore(List<SetScore> setScores) {
        return setScores.get(setScores.size() - 1);
    }

    private boolean shouldPlayNewGame(String scorer) throws NoSuchPlayerException {
        final SetScore playerOneSetScore = getLastSetScore(
                Optional.ofNullable(this.setScores.get(scorer)).orElseThrow(NoSuchPlayerException::new)
        );
        final SetScore playerTwoSetScore = getLastSetScore(
                this.setScores.entrySet()
                        .stream().filter(o -> !o.getKey().equals(scorer)).map(Map.Entry::getValue)
                        .flatMap(Collection::stream).collect(Collectors.toList())
        );
        return playerOneSetScore.equals(SetScore.SIX) && playerTwoSetScore.equals(SetScore.FIVE);
    }

    private boolean isSetWinnable(String scorer) throws NoSuchPlayerException {
        final SetScore playerOneSetScore = getLastSetScore(
                Optional.ofNullable(this.setScores.get(scorer)).orElseThrow(NoSuchPlayerException::new)
        );
        final SetScore playerTwoSetScore = getLastSetScore(
                this.setScores.entrySet()
                        .stream().filter(o -> !o.getKey().equals(scorer)).map(Map.Entry::getValue)
                        .flatMap(Collection::stream).collect(Collectors.toList())
        );

        return playerOneSetScore.equals(SetScore.SIX) && playerTwoSetScore.getValue() <= 4;
    }

    private void endSet(String playerName) {
        this.winner = playerName;
    }

    public Optional<String> getWinner() {
        return Optional.ofNullable(winner);
    }

    public void setPlayerSetScores(String player, List<SetScore> setScores) {
        this.setScores.replace(player, setScores);
    }

    public List<SetScore> getPlayerSetScores(String player) {
        return Optional.ofNullable(this.setScores.get(player)).orElse(new ArrayList<>());
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Optional<TieBreak> getTieBreak() {
        return Optional.ofNullable(tieBreak);
    }

    private void enableTieBreakRuleIfNecessary() {
        final boolean bothPlayerReachedSix = this.setScores.values()
                .stream().map(this::getLastSetScore)
                .filter(o -> o.equals(SetScore.SIX)).count() == 2;
        if (bothPlayerReachedSix) {
            List<String> players = new ArrayList<>(this.setScores.keySet());
            this.tieBreak = new TieBreak(players.get(0), players.get(1));
        }
    }

    private void incrementSetScore(String player) {
        this.setScores.get(player).add(getLastSetScore(this.setScores.get(player)).next());
        this.setScores.entrySet().stream()
                .filter(o -> !o.getKey().equals(player))
                .forEach(o -> o.getValue().add(getLastSetScore(o.getValue())));
    }

    public void setTieBreak(TieBreak tieBreak) {
        this.tieBreak = tieBreak;
    }
}
