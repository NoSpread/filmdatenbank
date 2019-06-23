import service.ReaderService;

public class App {
    private ReaderService readerService;

    public App(String[] params) {
        this.readerService = new ReaderService();
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
