import service.ControlService;
import service.MovieService;
import service.ReaderService;

public class App {
    private ReaderService readerService;
    private MovieService movieService;
    private ControlService controlService;

    public App(String[] params) {
        this.movieService = new MovieService();
        this.readerService = new ReaderService(this.movieService);
        this.controlService = new ControlService(params, this.movieService);
        this.readerService.readFile();

        System.out.println("Passed params:");
        for (int i = 0; i < params.length; i++) {
            System.out.println("Parameter " + i + ": " + params[i]);
        }
    }
}
