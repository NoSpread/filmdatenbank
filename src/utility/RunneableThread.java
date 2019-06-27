package utility;

import service.ReaderService;

import java.util.List;
import java.util.Map;

public class RunneableThread implements Runnable {
    public ReaderService readerService;
    public Map<String, List<String>> dataSet;

    public RunneableThread(ReaderService instance, Map<String, List<String>> mapList) {
        this.readerService = instance;
        this.dataSet = mapList;
    }

    public void run() {

    }
}
