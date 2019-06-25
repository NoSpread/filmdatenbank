package service;

import model.Actor;
import model.Director;
import model.Movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static utility.StopCodes.*;

public class ReaderService {
    private MovieService movieService;

    public ReaderService(MovieService movieService) {
        this.movieService = movieService;
    }

    public void readFile() {
        try {
            File dbFile = new File("data.db");
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
                case "New_Entity: " + ACTOR_STOP: this.parseActors(entry.getValue()); break;
                case "New_Entity: " + MOVIE_STOP: this.parseMovies(entry.getValue()); break;
                case "New_Entity: " + DIRECTOR_STOP: this.parseDirectors(entry.getValue()); break;
                case "New_Entity: " + DIRECTOR_MOVIE_STOP: this.parseDirectorInMovie(entry.getValue()); break;
                case "New_Entity: " + ACTOR_MOVIE_STOP: this.parseActorsInMovies(entry.getValue()); break;
                default: return;
            }
        }
    }

    private void parseActors(List<String> dataList) {
        for (String data : dataList) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                String name = splitComma[1].replace("\"", "").replaceAll("^\\s", "");
                int id = Integer.parseInt(splitComma[0].replace("\"", ""));
                this.movieService.addActor(new Actor().setId(id).setName(name));
            }
        }
    }

    private void parseMovies(List<String> dataList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        for (String data : dataList) {
            String[] splitComma = data.split("\",\"");
            if (splitComma.length == 7) {
                int id = Integer.parseInt(splitComma[0].replace("\"", ""));
                for (int i = 0; i < 7; i++) {
                    try {
                       splitComma[i] = splitComma[i].replace("\"", "");
                    } catch (Exception ignored) {

                    }
                }
                String title = splitComma[1];
                String plot = splitComma[2];
                String genre = splitComma[3];
                Date release = new Date();
                double imdbRating;
                int imdbVotes;

                Movie movie = new Movie();
                try {
                    release = format.parse(splitComma[4]);
                    imdbRating = Double.parseDouble(splitComma[6]);
                    imdbVotes = Integer.parseInt(splitComma[5]);
                    movie.setImdbRating(imdbRating).setImdbVotes(imdbVotes);
                } catch (Exception ignored) { }
                this.movieService.addMovie(movie.setId(id).setTitle(title).setPlot(plot).setGenre(genre).setReleased(release));
            }
        }
    }

    private void parseDirectors(List<String> dataList) {
        for (String data : dataList) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                String name = splitComma[1].replace("\"", "").replaceAll("^\\s", "");
                int id = Integer.parseInt(splitComma[0].replace("\"", ""));
                this.movieService.addDirector(new Director().setId(id).setName(name));
            }
        }
    }

    private void parseActorsInMovies(List<String> dataList) {
        for (String data : dataList) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                int actorId = Integer.parseInt(splitComma[0].replace("\"", ""));
                int movieId = Integer.parseInt(splitComma[1].replace("\"", ""));
                if (actorId != 0 &&  movieId != 0) {
                    Actor actorById = this.movieService.getActorById(actorId);
                    Movie movieById = this.movieService.getMovieById(movieId);
                    actorById.getMovies().add(movieById.getId());

                    // TODO broken as well - both get*ById return null
                }
            }

        }
    }

    private void parseDirectorInMovie(List<String> dataList) {
        for (String data : dataList) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                int directorId = Integer.parseInt(splitComma[0].replace("\"", ""));
                int movieId = Integer.parseInt(splitComma[1].replace("\"", ""));
                if (directorId != 0 && movieId != 0) {
                    Director directorById = this.movieService.getDirectorById(directorId);
                    Movie movieById = this.movieService.getMovieById(movieId);
                    directorById.getMovies().add(movieById.getId());

                    // TODO FIX - both get*ById return null
                }
            }
        }
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
