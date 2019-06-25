import service.ControlService;
import service.MovieService;
import service.ReaderService;

import java.io.BufferedReader;

public class App {
    private ReaderService readerService;
    private MovieService movieService;
    private ControlService controlService;

    public App(String[] params) {
        this.movieService = new MovieService();
        this.readerService = new ReaderService(this.movieService);
        // read the file and create the arrays
        BufferedReader bufferedReader = this.readerService.readFile();
        this.readerService.getData(bufferedReader);

        this.controlService = new ControlService(params, this.movieService);

        this.controlService.parseParams(params);

        System.out.println("Passed params:");
        for (int i = 0; i < params.length; i++) {
            System.out.println("Parameter " + i + ": " + params[i]);
        }
    }
}
