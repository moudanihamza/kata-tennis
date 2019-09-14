package set;

import domain.set.SetScore;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class SetScoreTest {

    @Test
    @Parameters({
            "ZERO,ONE",
            "TWO,THREE"})
    public void score_should_increment_using_next(SetScore given, SetScore expected) {
        //WHEN
        SetScore result = given.next();
        //THEN
        Assert.assertThat(result, is(expected));
    }

    @Test
    @Parameters({
            "ZERO,ZERO",
            "THREE,TWO"})
    public void score_should_decrement_using_previous(SetScore given, SetScore expected) {
        //WHEN
        SetScore result = given.previous();
        //THEN
        Assert.assertThat(result, is(expected));
    }
}

