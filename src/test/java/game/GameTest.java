package game;

import domain.game.Game;
import domain.game.GameScore;
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

import static org.hamcrest.CoreMatchers.is;

@RunWith(JUnitParamsRunner.class)
public class GameTest {
    private static final String PLAYER_ONE = "playerOne";
    private static final String PLAYER_TWO = "playerTwo";
    private Game game;

    @Before
    public void init() {
        this.game = new Game(PLAYER_ONE, PLAYER_TWO);
    }

    @Test
    @Parameters({"ZERO,ZERO,FIFTEEN,ZERO," + PLAYER_ONE + ",false",
            "FIFTEEN,ZERO,FIFTEEN,FIFTEEN," + PLAYER_TWO + ",false",
            "FORTY,FORTY,FORTY,ADVANTAGE," + PLAYER_TWO + ",true",
            "FORTY,ADVANTAGE,DEUCE,DEUCE," + PLAYER_ONE + ",true",
            "DEUCE,DEUCE,ADVANTAGE,FORTY," + PLAYER_ONE + ",true"})
    public void should_increment_when_given_player_score(GameScore givenPlayerOne, GameScore givenPlayerTwo, GameScore expectedPlayerOne,
                                                         GameScore expectedPlayerTwo, String playerName, boolean deuceMode) throws NoSuchPlayerException {
        //GIVEN
        if (deuceMode) {
            givenPlayerOne.activateDeuceMode();
            givenPlayerTwo.activateDeuceMode();
        }
        setScores(givenPlayerOne, givenPlayerTwo);
        //WHEN
        this.game.scores(playerName);

        //THEN
        Assert.assertThat(getLastGameScore(this.game.getPlayerScores(PLAYER_ONE)), is(expectedPlayerOne));
        Assert.assertThat(getLastGameScore(this.game.getPlayerScores(PLAYER_TWO)), is(expectedPlayerTwo));
    }

    @Test
    @Parameters({"FORTY,FIFTEEN," + PLAYER_ONE,
            "ADVANTAGE,FORTY," + PLAYER_ONE})
    public void player_should_be_a_winner_when_his_score_bigger_than_forty(GameScore playerOne, GameScore playerTwo, String playerName) throws NoSuchPlayerException {
        //GIVEN
        setScores(playerOne, playerTwo);

        //WHEN
        this.game.scores(playerName);

        //THEN
        Assert.assertEquals(this.game.getWinner().orElse("NoOneYet"), playerName);
        Assert.assertThat(getLastGameScore(this.game.getPlayerScores(PLAYER_ONE)), is(GameScore.ZERO));
        Assert.assertThat(getLastGameScore(this.game.getPlayerScores(PLAYER_TWO)), is(GameScore.ZERO));
    }

    private void setScores(GameScore playerOne, GameScore playerTwo) {
        this.game.SetPlayerScore(PLAYER_ONE, new ArrayList<>(Collections.singletonList(playerOne)));
        this.game.SetPlayerScore(PLAYER_TWO, new ArrayList<>(Collections.singletonList(playerTwo)));
    }

    private GameScore getLastGameScore(List<GameScore> gameScores) {
        return gameScores.get(gameScores.size() - 1);
    }

}
