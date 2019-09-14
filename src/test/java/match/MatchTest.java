package match;

import domain.game.Game;
import domain.game.GameScore;
import domain.match.Match;
import domain.set.Set;
import domain.set.SetScore;
import domain.tiebreak.TieBreak;
import exceptions.NoSuchPlayerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class MatchTest {

    private static final String PLAYER_ONE = "playerOne";
    private static final String PLAYER_TWO = "playerTwo";
    private Match match;

    @Before
    public void init() {
        this.match = new Match(PLAYER_ONE, PLAYER_TWO);
    }
    /*
     * player should win the match if the set score before was 6 against 5
     */
    @Test
    public void player_should_win_the_match() throws NoSuchPlayerException {
        //GIVEN
        Game game = new Game(PLAYER_ONE, PLAYER_TWO);
        game.setPlayerGameScore(PLAYER_ONE, new ArrayList<>(Collections.singletonList(GameScore.FORTY)));
        game.setPlayerGameScore(PLAYER_TWO, new ArrayList<>(Collections.singletonList(GameScore.FIFTEEN)));
        Set set = new Set(PLAYER_ONE, PLAYER_TWO);
        set.setGames(new ArrayList<>(Collections.singletonList(game)));
        set.setPlayerSetScores(PLAYER_ONE, new ArrayList<>(Collections.singletonList(SetScore.SIX)));
        set.setPlayerSetScores(PLAYER_TWO, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));
        this.match.setSet(set);

        //WHEN
        this.match.scores(PLAYER_ONE);

        //THEN
        String winner = this.match.getWinner().orElse("noOneYet");
        Assert.assertEquals(PLAYER_ONE, winner);
    }

    @Test
    public void player_should_win_match_if_he_win_the_tie_break() throws NoSuchPlayerException {
        //GIVEN
        TieBreak tieBreak = new TieBreak(PLAYER_ONE, PLAYER_TWO);
        tieBreak.setPlayerTieBreakScores(PLAYER_ONE,new ArrayList<>(Collections.singletonList(6)));
        tieBreak.setPlayerTieBreakScores(PLAYER_TWO,new ArrayList<>(Collections.singletonList(2)));
        Set set = new Set(PLAYER_ONE, PLAYER_TWO);
        set.setPlayerSetScores(PLAYER_ONE, new ArrayList<>(Collections.singletonList(SetScore.SIX)));
        set.setPlayerSetScores(PLAYER_TWO, new ArrayList<>(Collections.singletonList(SetScore.SIX)));
        set.setTieBreak(tieBreak);
        this.match.setSet(set);

        //WHEN
        this.match.scores(PLAYER_ONE);

        //THEN
        String setWinner = this.match.getWinner().orElse("noOneYet");
        String tieBreakWinner = tieBreak.getWinner().orElse("noOneYet");
        Assert.assertEquals(PLAYER_ONE,setWinner);
        Assert.assertEquals(PLAYER_ONE,tieBreakWinner);
    }
}
