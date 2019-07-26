package service;

import model.Actor;
import model.Director;
import model.Movie;
import utility.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControlService {
    private MovieService movieService;

    public ControlService(MovieService movieService) {
        this.movieService = movieService;
    }

    public void parseParams(String[] params) {
        // we just want to check the first parameter. Every other parameter can be ignored.
        // Tell the user that the params were ignored
        if (params.length > 1) {
            System.out.println("You passed too many arguments. The following arguments were ignored:");
            for (int i = 0; i < params.length; i++) {
                if (i != 0) {
                    System.out.println(i + ". -> " + params[i]);
                }
            }
            // check params
            this.checkParameter(params[0]);
        } else if (params.length == 1) {
            // check if the parameter is valid
            this.checkParameter(params[0]);
        } else {
            // no arguments, exit
            System.out.println("You did not pass any arguments. Program will exit now!");
            System.exit(1);
        }
    }

    private void checkParameter(String param) {
        // check the integrity of the parameter
        // it should look like this "--command=data"
        // data can either be a String or a integer
        if (param.contains("--")) {
            String noDashes = param.replace("--","");
            String[] splitAtEquals = noDashes.split("=");
            // this should be an array with 2 elements now. The first element should contain the command
            // and the second should contain the data passed to the program
            // Now check if we understand the command
            switch (splitAtEquals[0]) {
                case Parameters.MOVIESEARCH: // expects a string as data
                    List<Movie> movies = this.movieService.getMovieByTitle(this.checkForStringData(splitAtEquals[1], param));
                    for (Movie movie : movies){
                        String[] movtitlearr = movie.getTitle().split(", ");
                        String movtitle = movtitlearr.length == 2 ? movtitlearr[1] + " " + movtitlearr[0] : movie.getTitle();
                        System.out.printf("\nTITLE => %s\n PLOT => %s\n GENRE => %s\n RELEASE => %s\n IMDB: \n  RATING => %.2f\n  VOTES => %d\n DIRECTORS => %s\n ACTORS => %s\n ID => %d\n", movtitle , movie.getPlot(), movie.getGenre(), movie.getReleased(), movie.getImdbRating(), movie.getImdbVotes(), movie.getDirectors().stream().map(Director::getName).collect(Collectors.toList()), movie.getActors().stream().map(Actor::getName).collect(Collectors.toList()) ,movie.getId());
                    }
                    break;
                case Parameters.ACTORSEARCH: // expects a string as data
                    List<Actor> actors = this.movieService.getActorByName(this.checkForStringData(splitAtEquals[1],param));
                    for (Actor actor : actors) {
                        List<String> movieNames = new ArrayList<>();
                        for (Movie movie : actor.getMovies()){
                            String[] movtitlearr = movie.getTitle().split(", ");
                            movieNames.add(movtitlearr.length == 2 ? movtitlearr[1] + " " + movtitlearr[0] : movie.getTitle());
                        }
                        System.out.printf("\nNAME => %s\n ID => %d\n MOVIES => %s\n", actor.getName(), actor.getId(),  movieNames);
                    }

                    break;
                case Parameters.ACTORNETWORK: // expects a int as data
                    int dat = this.checkForIntData(splitAtEquals[1], param);
                    Actor actor = this.movieService.getActorById(dat);
                    for (Movie movie : actor.getMovies()) {
                        String[] movtitlearr = movie.getTitle().split(", ");
                        String movtitle = movtitlearr.length == 2 ? movtitlearr[1] + " " + movtitlearr[0] : movie.getTitle();
                        System.out.println("\nMovie => " + movtitle);
                        for(Actor subActor : movie.getActors()) {
                            System.out.println("  Actor => " + subActor.getName());
                        }
                    }
                    break;
                case Parameters.MOVIENETWORK: // expects a int as data
                    int data = this.checkForIntData(splitAtEquals[1], param);
                    Movie movie = this.movieService.getMovieById(data);
                    for (Actor act : movie.getActors()) {
                        System.out.println("\nActor => " + act.getName());
                        for (Movie subMovie : act.getMovies()) {
                            String[] movtitlearr = subMovie.getTitle().split(", ");
                            String movtitle = movtitlearr.length == 2 ? movtitlearr[1] + " " + movtitlearr[0] : subMovie.getTitle();
                            System.out.println("  Movie => " + movtitle);
                        }
                    }
                    break;
                default: damagedParameter(param);
            }
        } else {
            this.damagedParameter(param);
        }
    }

    private int checkForIntData(String data, String param) {
        if (data != null) {
            return Integer.parseInt(data);
        } else {
            this.damagedParameter(param);
            return 0; // will never be called
        }
    }

    private String checkForStringData(String data, String param) {
        if (data != null) {
            return data.replace("\"", "");
        } else {
            this.damagedParameter(param);
            return ""; // will never be called
        }
    }

    private void damagedParameter(String param) {
        System.out.println("\nThe passed parameter: " + param + " could not be read by the program.");
        System.out.println("Exiting now ...");
        System.exit(1);
    }
}
