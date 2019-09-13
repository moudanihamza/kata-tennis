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
            "ZERO,FIFTEEN",
            "FIFTEEN,THIRTY",
            "THIRTY,FORTY"})
    public void score_should_increment_using_next(GameScore given, GameScore expected) {
        //WHEN
        GameScore result = given.next();
        //THEN
        Assert.assertThat(result, is(expected));
    }

    @Test
    @Parameters({
            "ZERO,ZERO",
            "THIRTY,FIFTEEN",
            "FORTY,THIRTY"})
    public void score_should_decrement_using_previous(GameScore given, GameScore expected) {
        //WHEN
        GameScore result = given.previous();
        //THEN
        Assert.assertThat(result, is(expected));
    }
}
