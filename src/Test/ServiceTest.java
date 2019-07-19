package Test;

import model.Actor;
import model.Director;
import model.Movie;
import org.junit.jupiter.api.Test;
import service.MovieService;
import service.ReaderService;

import java.io.BufferedReader;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void readService() {
        MovieService test = new MovieService();
        ReaderService readerService = new ReaderService(test);
        BufferedReader bufferedReader = readerService.readFile();
        readerService.getData(bufferedReader);
    }

    @Test
    void modelTest() {
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