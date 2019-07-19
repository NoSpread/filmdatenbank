package service;

import model.Actor;
import model.Director;
import model.Movie;
import utility.RunnableThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utility.StopCodes.*;

public class ReaderService {
    private MovieService movieService;
    public ReaderService(MovieService movieService) {
        this.movieService = movieService;
    }

    // read database file
    public BufferedReader readFile() {
        try {
            File dbFile = new File("src/data.db");
            System.out.println("File successfully read!");
            return new BufferedReader(new FileReader(dbFile));
        } catch (Exception e) {
            System.out.println("File not found!");
            return null;
        }
    }

    //split database into sections and put them into a hashmap
    public void getData(BufferedReader bufferedReader) {
        Map<String, List<String>> dataSet = new HashMap<>();
        String prevLine = null;
        while (true) {
            String data = prevLine == null ? this.readMyLine(bufferedReader) : prevLine;
            prevLine = null;
            if (data == null) {
                break;
            }
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
        this.parseData(dataSet);
    }

    //parse the data we read from the database
    private void parseData(Map<String, List<String>> listHashMap) {

        List<String> actorList = listHashMap.get(ACTOR_STOP);
        List<String> movieList = listHashMap.get(MOVIE_STOP);
        List<String> directorList = listHashMap.get(DIRECTOR_STOP);
        List<String> actorMovieList = listHashMap.get(ACTOR_MOVIE_STOP);
        List<String> directorMovieList = listHashMap.get(DIRECTOR_MOVIE_STOP);

        /*
         * Prepare all threads for multi-threading (faster parsing)
         */
        Thread t1 = new Thread(new RunnableThread(this, actorList) {
            @Override
            public void run() {
                this.readerService.parseActors(actorList);
            }
        });

        Thread t2 = new Thread(new RunnableThread(this, movieList) {
            @Override
            public void run() {
                this.readerService.parseMovies(movieList);
            }
        });

        Thread t3 = new Thread(new RunnableThread(this, directorList) {
            @Override
            public void run() {
                this.readerService.parseDirectors(directorList);
            }
        });

        Thread t4 = new Thread(new RunnableThread(this, actorMovieList) {
            @Override
            public void run() {
                this.readerService.parseActorsInMovies(actorMovieList);
            }
        });

        Thread t5 = new Thread(new RunnableThread(this, directorMovieList) {
            @Override
            public void run() {
                this.readerService.parseDirectorInMovie(directorMovieList);
            }
        });

        // parse movies, actors and directors first
        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t4.start();
        t5.start();

        try {
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void parseActors(List<String> entry) {
        for (String data : entry) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                String name = splitComma[1].replace("\"", "").replaceAll("^\\s", "");
                int id = Integer.parseInt(splitComma[0].replace("\"", ""));
                this.movieService.addActor(new Actor().setId(id).setName(name));
            }
        }
    }

    private void parseMovies(List<String> entry) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        for (String data : entry) {
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

                if (splitComma[4].equals("")) splitComma[4] = "0000-00-00";
                if (splitComma[5].equals("")) splitComma[5] = "0";
                if (splitComma[6].equals("")) splitComma[6] = "0";

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

    private void parseDirectors(List<String> entry) {
        for (String data : entry) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                String name = splitComma[1].replace("\"", "").replaceAll("^\\s", "");
                int id = Integer.parseInt(splitComma[0].replace("\"", ""));
                this.movieService.addDirector(new Director().setId(id).setName(name));
            }
        }
    }

    private void parseActorsInMovies(List<String> entry) {
        for (String data : entry) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                int actorId = Integer.parseInt(splitComma[0].replace("\"", ""));
                int movieId = Integer.parseInt(splitComma[1].replace("\"", ""));
                if (actorId != 0 &&  movieId != 0) {
                    Actor actorById = this.movieService.getActorById(actorId);
                    Movie movieById = this.movieService.getMovieById(movieId);
                    if (actorById == null || movieById == null) {
                        continue;
                    }
                    actorById.getMovies().add(movieById);
                    movieById.getActors().add(actorById);
                }
            }

        }
    }

    private void parseDirectorInMovie(List<String> entry) {
        for (String data : entry) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                int directorId = Integer.parseInt(splitComma[0].replace("\"", ""));
                int movieId = Integer.parseInt(splitComma[1].replace("\"", ""));
                if (directorId != 0 && movieId != 0) {
                    Director directorById = this.movieService.getDirectorById(directorId);
                    Movie movieById = this.movieService.getMovieById(movieId);
                    if (directorById == null || movieById == null) {
                        continue;
                    }
                    directorById.getMovies().add(movieById);
                    movieById.getDirectors().add(directorById);
                }
            }
        }
    }


    private boolean hasStopCode(String data) {
        return data.contains(ACTOR_STOP) || data.contains(MOVIE_STOP) || data.contains(DIRECTOR_STOP) || data.contains(DIRECTOR_MOVIE_STOP) || data.contains(ACTOR_MOVIE_STOP);
    }

    private String readMyLine(BufferedReader bufferedReader) {
        try {
            return bufferedReader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

}
