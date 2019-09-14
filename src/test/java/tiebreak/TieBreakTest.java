package tiebreak;

import domain.tiebreak.TieBreak;
import exceptions.NoSuchPlayerException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class TieBreakTest {

    private static final String PLAYER_ONE = "playerOne";
    private static final String PLAYER_TWO = "playerTwo";
    private TieBreak tieBreak;

    @Before
    public void init() {
        this.tieBreak = new TieBreak(PLAYER_ONE, PLAYER_TWO);
    }

    @Test
    public void tie_break_score_should_increment_after_scoring() throws NoSuchPlayerException {
        //WHEN
        this.tieBreak.scores(PLAYER_ONE);
        //
        int score = getLastScore(this.tieBreak.getPlayerTieBreakScores(PLAYER_ONE));
        Assert.assertEquals(1, score);
    }


    /*
     * player should win tie break if he gets at least 7 points and 2 points more than his opponent
     */
    @Test
    @Parameters({
            "6,2,true",
            "8,3,true",
            "6,6,false"
    })
    public void player_should_win_if_he_respect_criteria(int playerOneScore, int playerTwoScore, boolean shouldWin) throws NoSuchPlayerException {

        //GIVEN
        this.tieBreak.setPlayerTieBreakScores(PLAYER_ONE, new ArrayList<>(Collections.singletonList(playerOneScore)));
        this.tieBreak.setPlayerTieBreakScores(PLAYER_TWO, new ArrayList<>(Collections.singletonList(playerTwoScore)));

        //WHEN
        this.tieBreak.scores(PLAYER_ONE);

        //THEN
        String winner = this.tieBreak.getWinner().orElse("noOneYet");
        boolean heIsTheWinner = winner.equals(PLAYER_ONE);
        Assert.assertEquals(shouldWin, heIsTheWinner);

    }

    private int getLastScore(List<Integer> scores) {
        return scores.get(scores.size() - 1);
    }
}
