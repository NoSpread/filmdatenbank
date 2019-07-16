import service.ControlService;
import service.MovieService;
import service.ReaderService;

import java.io.BufferedReader;

public class App {

    public App(String[] params) {
        MovieService movieService = new MovieService();
        ReaderService readerService = new ReaderService(movieService);
        BufferedReader bufferedReader = readerService.readFile();
        readerService.getData(bufferedReader);

        ControlService controlService = new ControlService(movieService);

        controlService.parseParams(params);

        System.out.println("Passed params:");
        for (int i = 0; i < params.length; i++) {
            System.out.println("Parameter " + i + ": " + params[i]);
        }
    }
}
