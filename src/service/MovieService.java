package service;

import model.Actor;
import model.Movie;
import model.Director;

import java.util.List;

public class MovieService {
    private List<Movie> movies;
    private List<Actor> actors;
    private List<Director> directors;

    /*
    - getMovie by Id
    - getMovie by Title
    - getMovie series if exists

    // network
    - getMovies by Actor
    - getActor by Movies
    - getActor by Id

    -> get all actors in movie, get all movies every actor took part in
    -> get all movies actor played a role in, get all actors in those movies
    // actors
    - getActor by Name (first + last)
     */

    public Movie getMovieById(String id) {
        return new Movie();
    }

    public Movie getMovieByTitle(String title) {
        return new Movie();
    }

    public void addMovie(Movie movie) {
        for (Movie inListMovie : movies) {
            if (inListMovie.getId() != movie.getId()) {
                this.movies.add(movie);
            } else {
                System.out.println("Duplicate entry for id " + movie.getId());
            }
        }
    }

    public void addActor(Actor actor) {
        for (Actor inListActors : actors) {
            if (inListActors.getId() != actor.getId()) {
                this.actors.add(actor);
            } else {
                System.out.println("Duplicate entry for id " + actor.getId());
            }
        }
    }

    public void addDirector(Director director) {
        for (Director inListDirectors : directors) {
            if (inListDirectors.getId() != director.getId()) {
                this.directors.add(director);
            } else {
                System.out.println("Duplicate entry for id " + director.getId());
            }
        }
    }


}
