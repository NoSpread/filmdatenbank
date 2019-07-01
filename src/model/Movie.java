package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String plot;
    private String genre;
    private Date released;
    private int imdbVotes;
    private double imdbRating;
    private List<Integer> actors = new ArrayList<>();
    private List<Integer> directors = new ArrayList<>();

    //region Getter/Setter
    public List<Integer> getActors() {
        return actors;
    }

    public Movie setActors(List<Integer> actors) {
        this.actors = actors;
        return this;
    }

    public List<Integer> getDirectors() {
        return directors;
    }

    public Movie setDirectors(List<Integer> directors) {
        this.directors = directors;
        return this;
    }

    public int getId() {
        return id;
    }

    public Movie setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPlot() {
        return plot;
    }

    public Movie setPlot(String plot) {
        this.plot = plot;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Movie setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public Date getReleased() {
        return released;
    }

    public Movie setReleased(Date released) {
        this.released = released;
        return this;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

    public Movie setImdbVotes(int imdbVotes) {
        this.imdbVotes = imdbVotes;
        return this;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public Movie setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
        return this;
    }
    //endregion


}
