package set;

import domain.game.Game;
import domain.game.GameScore;
import domain.set.Set;
import domain.set.SetScore;
import domain.tiebreak.TieBreak;
import exceptions.NoSuchPlayerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SetTest {

    private static final String PLAYER_ONE = "playerOne";
    private static final String PLAYER_TWO = "playerTwo";
    private Set set;

    @Before
    public void init() {
        this.set = new Set(PLAYER_ONE, PLAYER_TWO);
    }


    /*
     * player should win the set  if he got score 6 against 4 or lower
     */
    @Test
    public void playerOne_should_win_set() throws NoSuchPlayerException {
        //GIVEN
        Game game = new Game(PLAYER_ONE, PLAYER_TWO);
        game.setPlayerGameScore(PLAYER_ONE, new ArrayList<>(Collections.singletonList(GameScore.FORTY)));
        game.setPlayerGameScore(PLAYER_TWO, new ArrayList<>(Collections.singletonList(GameScore.FIFTEEN)));
        List<Game> games = new ArrayList<>(Collections.singletonList(game));
        this.set.setGames(games);
        this.set.setPlayerSetScores(PLAYER_ONE, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));
        this.set.setPlayerSetScores(PLAYER_TWO, new ArrayList<>(Collections.singletonList(SetScore.FOUR)));

        //WHEN
        this.set.scores(PLAYER_ONE);

        //THEN
        Assert.assertEquals(PLAYER_ONE, this.set.getWinner().orElse("NoOneYet"));
    }
    /*
     * player should play a new game if the score is 6 against 5
     */
    @Test
    public void players_should_play_new_game_and_no_one_win_the_set() throws NoSuchPlayerException {
        //GIVEN
        Game game = new Game(PLAYER_ONE, PLAYER_TWO);
        game.setPlayerGameScore(PLAYER_ONE, new ArrayList<>(Collections.singletonList(GameScore.FORTY)));
        game.setPlayerGameScore(PLAYER_TWO, new ArrayList<>(Collections.singletonList(GameScore.FIFTEEN)));
        List<Game> games = new ArrayList<>(Collections.singletonList(game));
        this.set.setGames(games);
        this.set.setPlayerSetScores(PLAYER_ONE, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));
        this.set.setPlayerSetScores(PLAYER_TWO, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));

        //WHEN
        this.set.scores(PLAYER_ONE);

        //THEN
        final String winner = this.set.getWinner().orElse(null);
        Assert.assertNull(winner);
        Assert.assertEquals(2, this.set.getGames().size());
    }


    @Test
    public void player_should_win_set_if_he_win_the_tie_break() throws NoSuchPlayerException {
        //GIVEN
        TieBreak tieBreak = new TieBreak(PLAYER_ONE, PLAYER_TWO);
        tieBreak.setPlayerTieBreakScores(PLAYER_ONE,new ArrayList<>(Collections.singletonList(6)));
        tieBreak.setPlayerTieBreakScores(PLAYER_TWO,new ArrayList<>(Collections.singletonList(2)));
        this.set.setTieBreak(tieBreak);

        //WHEN
        this.set.scores(PLAYER_ONE);

        //THEN
        String setWinner = this.set.getWinner().orElse("noOneYet");
        String tieBreakWinner = tieBreak.getWinner().orElse("noOneYet");
        Assert.assertEquals(PLAYER_ONE,setWinner);
        Assert.assertEquals(PLAYER_ONE,tieBreakWinner);
    }
}
