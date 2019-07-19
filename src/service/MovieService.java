package service;

import model.Actor;
import model.Movie;
import model.Director;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MovieService {
    private List<Movie> movies;
    private List<Actor> actors;
    private List<Director> directors;

    public MovieService() {
        this.movies = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.directors = new ArrayList<>();
    }

    public Movie getMovieById(int id) {
        for (Movie inListMovie : this.movies) {
            if (inListMovie.getId() == id) {
                return inListMovie;
            }
        }
        // no movie found
        return null;
    }

    public void getMovieByTitle(String title) {
        for (Movie inListMovie : this.movies) {
            if (inListMovie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                String[] movtitlearr = inListMovie.getTitle().split(", ");
                String movtitle = movtitlearr.length == 2 ? movtitlearr[1] + " " + movtitlearr[0] : inListMovie.getTitle();
                System.out.printf("TITLE => %s\n PLOT => %s\n GENRE => %s\n RELEASE => %s\n IMDB: \n  RATING => %.2f\n  VOTES => %d\n DIRECTORS => %s\n ACTORS => %s\n ID => %d\n", movtitle , inListMovie.getPlot(), inListMovie.getGenre(), inListMovie.getReleased(), inListMovie.getImdbRating(), inListMovie.getImdbVotes(), inListMovie.getDirectors().stream().map(Director::getName).collect(Collectors.toList()), inListMovie.getActors().stream().map(Actor::getName).collect(Collectors.toList()) ,inListMovie.getId());
            }
        }
    }

    public void getActorByName(String name) {
        for (Actor actor : this.actors) {
            if (actor.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println("NAME => " + actor.getName() + "ID => " + actor.getId());
                for (Movie movie : actor.getMovies()) {
                    System.out.println("  MOVIE => " + movie.getTitle());
                }
            }
        }
    }

    public Director getDirectorById(int id) {
        for (Director director : this.directors) {
            if (director.getId() == id) {
                return director;
            }
        }
        return null;
    }

    public Actor getActorById(int id) {
        for (Actor actor : this.actors) {
            if (actor.getId() == id) {
                return actor;
            }
        }
        return null;
    }


    public void addMovie(Movie movie) {
        if (!this.containsMovie(this.movies, 0, movie.getTitle())) {
            this.movies.add(movie);
        }
    }

    public void addActor(Actor actor) {
        if (!this.containsActor(this.actors, 0, actor.getName())) {
            this.actors.add(actor);
        }
    }

    public void addDirector(Director director) {
        if (!this.containsDirector(this.directors, 0, director.getName())) {
            this.directors.add(director);
        }
    }

    /*
    *   Pass either int value or string value to check if the id or the name is already present in the
    *   passed list.
     */
    private boolean containsActor(final List<Actor> list, final int intValue, final String stringValue) {
        if (intValue != 0) {
            return list.stream().anyMatch(o -> o.getId() == intValue);
        } else {
            return list.stream().anyMatch(o -> o.getName().toLowerCase().equals(stringValue.toLowerCase()));
        }
    }

    private boolean containsMovie(final List<Movie> list, final int intValue, final String stringValue) {
        if (intValue != 0) {
            return list.stream().anyMatch(o -> o.getId() == intValue);
        } else {
            return list.stream().anyMatch(o -> o.getTitle().toLowerCase().equals(stringValue.toLowerCase()));
        }
    }

    private boolean containsDirector(final List<Director> list, final int intValue, final String stringValue) {
        if (intValue != 0) {
            return list.stream().anyMatch(o -> o.getId() == intValue);
        } else {
            return list.stream().anyMatch(o -> o.getName().toLowerCase().equals(stringValue.toLowerCase()));
        }
    }

}
