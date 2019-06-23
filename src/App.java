import service.MovieService;
import service.ReaderService;

public class App {
    private ReaderService readerService;
    private MovieService movieService;

    public App(String[] params) {
        this.movieService = new MovieService();
        this.readerService = new ReaderService(this.movieService);

        System.out.println("Passed params:");
        for (int i = 0; i < params.length; i++) {
            System.out.println("Parameter " + i + ": " + params[i]);
        }
        this.run();
    }

    private void run() {
        this.readerService.readFile();
    }
}
