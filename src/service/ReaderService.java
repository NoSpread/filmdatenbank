package service;

import model.Actor;
import model.Director;
import model.Movie;
import utility.ReaderEventListener;
import utility.RunnableThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static utility.StopCodes.*;

public class ReaderService {
    private MovieService movieService;
    // reader listener
    private ReaderEventListener readerListener;
    // debug
    private Map<String, List<String>> dataSet = new HashMap<>();
    private Map<String, List<String>> dependData = new HashMap<>();

    public boolean actorDone = false;
    public boolean movieDone = false;
    public boolean directorDone = false;

    Thread t1;
    Thread t2;
    Thread t3;
    Thread t4;
    Thread t5;

    public ReaderService(MovieService movieService) {
        this.movieService = movieService;
    }

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

    private void registerReaderListener(ReaderEventListener listener) {
        this.readerListener = listener;
    }

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
        this.dataSet = dataSet;
        this.parseData(dataSet);
        //this.startChunk(dataSet.entrySet().iterator().next());
    }

    private void parseData(Map<String, List<String>> listHashMap) {

        List<String> actorList = listHashMap.get(ACTOR_STOP);
        List<String> movieList = listHashMap.get(MOVIE_STOP);
        List<String> directorList = listHashMap.get(DIRECTOR_STOP);
        List<String> actorMovieList = listHashMap.get(ACTOR_MOVIE_STOP);
        List<String> directorMovieList = listHashMap.get(DIRECTOR_MOVIE_STOP);

        this.t1 = new Thread(new RunnableThread(this, actorList) {
            @Override
            public void run() {
                System.out.println("Thread Started - 1");
                this.readerService.parseActors(actorList);
                this.readerService.actorDone = true;
                System.out.println("Thread exited - 1");
            }
        });

        this.t2 = new Thread(new RunnableThread(this, movieList) {
            @Override
            public void run() {
                System.out.println("Thread Started - 2");
                this.readerService.parseMovies(movieList);
                this.readerService.movieDone = true;
                System.out.println("Thread exited - 2");
            }
        });

        this.t3 = new Thread(new RunnableThread(this, directorList) {
            @Override
            public void run() {
                System.out.println("Thread Started - 3");
                this.readerService.parseDirectors(directorList);
                this.readerService.directorDone = true;
                System.out.println("Thread exited - 3");
            }
        });

        this.t1.start();
        this.t2.start();
        this.t3.start();

        while(!(actorDone && movieDone && directorDone)) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                System.exit(1);
            }
        }

        this.t4 = new Thread(new RunnableThread(this, actorMovieList) {
            @Override
            public void run() {
                System.out.println("Thread Started - 4");
                this.readerService.parseActorsInMovies(actorMovieList);
                System.out.println("Thread exited - 4");
            }
        });

        this.t5 = new Thread(new RunnableThread(this, actorMovieList) {
            @Override
            public void run() {
                System.out.println("Thread Started - 5");
                this.readerService.parseDirectorInMovie(directorMovieList);
                System.out.println("Thread exited - 5");
            }
        });
        this.t4.start();
        this.t5.start();
    }

//    private void startChunk(Map.Entry<String, List<String>> entry) {
//        if (this.dataSet.size() == 0 && this.dependData.size() == 0) { return; }
//        if (this.dataSet.size() == 0 && this.dependData.size() > 0) {
//            switch (entry.getKey()) {
//                case ACTOR_MOVIE_STOP:
//                    this.parseActorsInMovies(entry);
//                    this.dependData.remove(entry.getKey());
//                    if (this.dependData.size() == 0) { return; }
//                    this.startChunk(this.dependData.entrySet().iterator().next());
//                    return;
//                case DIRECTOR_MOVIE_STOP:
//                    this.parseDirectorInMovie(entry);
//                    this.dependData.remove(entry.getKey());
//                    if (this.dependData.size() == 0) { return; }
//                    this.startChunk(this.dependData.entrySet().iterator().next());
//                    return;
//            }
//        }
//
//        switch (entry.getKey()) {
//            case ACTOR_STOP:
//                this.parseActors(entry);
//                dataSet.remove(entry.getKey());
//                if (this.dataSet.size() == 0) { this.startChunk(this.dependData.entrySet().iterator().next()); }
//                this.startChunk(this.dataSet.entrySet().iterator().next());
//                return;
//            case DIRECTOR_STOP:
//                this.parseDirectors(entry);
//                dataSet.remove(entry.getKey());
//                if (this.dataSet.size() == 0) { this.startChunk(this.dependData.entrySet().iterator().next()); }
//                this.startChunk(this.dataSet.entrySet().iterator().next());
//                return;
//            case MOVIE_STOP:
//                this.parseMovies(entry);
//                dataSet.remove(entry.getKey());
//                if (this.dataSet.size() == 0) { this.startChunk(this.dependData.entrySet().iterator().next()); }
//                this.startChunk(this.dataSet.entrySet().iterator().next());
//            default:
//                this.dependData.put(entry.getKey(), entry.getValue());
//                this.dataSet.remove(entry.getKey());
//                this.startChunk(this.dataSet.entrySet().iterator().next());
//                break;
//        }
//    }

    //region chunking
/*    private void startChunk(Map<String, List<String>> dataSet) {
        if (dataSet.size() == 0) { return; }
        // create values

        for (Map.Entry<String, List<String>> entry : dataSet.entrySet()) {
            switch (entry.getKey()) {
                case ACTOR_STOP:
                    this.readerListener = null;
                    new Thread(new RunneableThread(this, dataSet) {
                        @Override
                        public void run() {
                            // start with the first chunk
                            this.readerService.parseActors(entry);
                            this.dataSet.remove(entry.getKey());
                            this.readerService.startChunk(this.dataSet);
                        }
                    }).start();
                    return;
                case MOVIE_STOP:
                    this.readerListener = null;
                    new Thread(new RunneableThread(this, dataSet) {
                        @Override
                        public void run() {
                            // start with the first chunk
                            this.readerService.parseMovies(entry);
                            this.dataSet.remove(entry.getKey());
                            this.readerService.startChunk(this.dataSet);
                        }
                    }).start();
                    return;
                case DIRECTOR_STOP:
                    this.readerListener = null;
                    new Thread(new RunneableThread(this, dataSet) {
                        @Override
                        public void run() {
                            // start with the first chunk
                            this.readerService.parseDirectors(entry);
                            this.dataSet.remove(entry.getKey());
                            this.readerService.startChunk(this.dataSet);
                        }
                    }).start();
                    return;
//                case DIRECTOR_MOVIE_STOP:
//                    this.readerListener = null;
//                    new Thread(new RunneableThread(this, dataSet) {
//                        @Override
//                        public void run() {
//                            // start with the first chunk
//                            this.readerService.parseDirectorInMovie(entry);
//                            this.dataSet.remove(entry.getKey());
//                            this.readerService.startChunk(this.dataSet);
//                        }
//                    }).start();
//                    return;
//                case ACTOR_MOVIE_STOP:
//                    this.readerListener = null;
//                    new Thread(new RunneableThread(this, dataSet) {
//                        @Override
//                        public void run() {
//                            // start with the first chunk
//                            this.readerService.parseActorsInMovies(entry);
//                            this.dataSet.remove(entry.getKey());
//                            this.readerService.startChunk(this.dataSet);
//                        }
//                    }).start();
//                    return;
                default:
                    return;
            }
        }
    }*/
    //endregion

    private void parseActors(List<String> entry) {
        // List<Actor> duplicateActors = new ArrayList<>();
        for (String data : entry) {
            String[] splitComma = data.split(",");
            if (splitComma.length == 2) {
                String name = splitComma[1].replace("\"", "").replaceAll("^\\s", "");
                int id = Integer.parseInt(splitComma[0].replace("\"", ""));
                this.movieService.addActor(new Actor().setId(id).setName(name));
                // Actor dupActor = this.movieService.addActor(new Actor().setId(id).setName(name));
                // if (dupActor != null) { duplicateActors.add(dupActor); }
            }
        }
//        if (duplicateActors.size() > 0) {
//            System.out.println("Found " + duplicateActors.size() + " duplicates (" + duplicateActors.size() + "/" + entry.size() + ").");
//        }
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
                        //System.out.println("Could not find object for IDs => { " + actorId + " / " + movieId + " }");
                        continue;
                    }
                    actorById.getMovies().add(movieById.getId());
                    movieById.getActors().add(actorById.getId());
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
                        //System.out.println("Could not find object for IDs => { " + directorId + " / " + movieId + " }");
                        continue;
                    }
                    directorById.getMovies().add(movieById.getId());
                    movieById.getDirectors().add(directorById.getId());
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
