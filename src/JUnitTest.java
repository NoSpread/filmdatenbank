import static org.junit.jupiter.api.Assertions.assertEquals;

import model.*;
import org.junit.jupiter.api.Test;
import service.MovieService;

import java.util.Date;

public class JUnitTest {

    @Test
    public void test_MovieService() {
        MovieService test = new MovieService();
        Movie test_mov = new Movie().setId(0).setGenre("romance").setImdbRating(0.0).setImdbVotes(1000).setPlot("A basic Story").setReleased(new Date()).setTitle("Star Wars");
        Actor test_act = new Actor().setId(1).setName("Keanu Reeves");
        Director test_dir = new Director().setId(2).setName("Steven Spielberg");

        test.addMovie(test_mov);
        test.addActor(test_act);
        test.addDirector(test_dir);

        assertEquals(test_mov, test.getMovieById(0));
        assertEquals(test_act, test.getActorById(1));
        assertEquals(test_dir, test.getDirectorById(2));
    }
}
