package utility;

import service.ReaderService;

import java.util.List;

public class RunnableThread implements Runnable {
    public ReaderService readerService;
    public List<String> dataSet;

    public RunnableThread(ReaderService instance, List<String> mapList) {
        this.readerService = instance;
        this.dataSet = mapList;
    }

    public void run() {

    }
}
