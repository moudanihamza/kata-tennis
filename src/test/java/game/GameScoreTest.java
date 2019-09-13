package game;

import domain.game.GameScore;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class GameScoreTest {

    @Test
    @Parameters({
            "ZERO,FIFTEEN,false",
            "FIFTEEN,THIRTY,false",
            "THIRTY,FORTY,false",
            "ADVANTAGE,ADVANTAGE,false",
            "DEUCE,ADVANTAGE,true"})
    public void score_should_increment_using_next(GameScore given, GameScore expected, boolean deuceActivated) {
        //GIVEN
        if (deuceActivated) {
            given.activateDeuceMode();
        }
        //WHEN
        GameScore result = given.next();
        //THEN
        Assert.assertThat(result, is(expected));
    }

    @Test
    @Parameters({
            "ZERO,ZERO,false",
            "THIRTY,FIFTEEN,false",
            "FORTY,THIRTY,false",
            "ADVANTAGE,ADVANTAGE,false",
            "DEUCE,FORTY,true"})
    public void score_should_decrement_using_previous(GameScore given, GameScore expected, boolean deuceActivated) {
        //GIVEN
        if (deuceActivated) {
            given.activateDeuceMode();
        }
        //WHEN
        GameScore result = given.previous();
        //THEN
        Assert.assertThat(result, is(expected));
    }
}
