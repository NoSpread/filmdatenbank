package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utility.StopCodes.*;

public class ReaderService {

    public void readFile() {
        try {
            File dbFile = new File("../data.db");
            BufferedReader read = new BufferedReader(new FileReader(dbFile));
            System.out.println("File successfully read!");
            this.getData(read);
        } catch (Exception e) {
            System.out.println("File not found!");
        }
    }


    private void getData(BufferedReader bufferedReader) {
        Map<String, List<String>> dataSet = new HashMap<>();
        String prevLine = null;
        while (true) {
            String data = prevLine == null ? this.readMyLine(bufferedReader) : prevLine;
            prevLine = null;
            if (data == null) { break; }
            List<String> cDataList = new ArrayList<>();
            if (hasStopCode(data)) {
                dataSet.put(data, cDataList);
                while (true) {
                    String cRead = this.readMyLine(bufferedReader);
                    if (cRead == null || hasStopCode(cRead)) {
                        prevLine = cRead;
                        break;
                    }
                    cDataList.add(cRead);
                }
            }
        }
        for (Map.Entry<String, List<String>> entry : dataSet.entrySet()) {
            switch (entry.getKey()) {
                case ACTOR_STOP: this.parseActors(entry.getValue()); break;
                case MOVIE_STOP: this.parseMovies(entry.getValue()); break;
                case DIRECTOR_STOP: this.parseDirectors(entry.getValue()); break;
                case DIRECTOR_MOVIE_STOP: this.parseDirectorInMovie(entry.getValue()); break;
                case ACTOR_MOVIE_STOP: this.parseActorsInMovies(entry.getValue()); break;
                default: return;
            }
        }
    }

    private void parseActors(List<String> dataList) {

    }

    private void parseMovies(List<String> dataList) {

    }

    private void parseDirectors(List<String> dataList) {

    }

    private void parseActorsInMovies(List<String> dataList) {

    }

    private void parseDirectorInMovie(List<String> dataList) {

    }

    private boolean hasStopCode(String data) {
        if (data.contains(ACTOR_STOP) || data.contains(MOVIE_STOP) || data.contains(DIRECTOR_STOP) || data.contains(DIRECTOR_MOVIE_STOP) || data.contains(ACTOR_MOVIE_STOP)) {
            return true;
        } else {
            return false;
        }
    }

    private String readMyLine(BufferedReader bufferedReader) {
        try {
            return bufferedReader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

}
