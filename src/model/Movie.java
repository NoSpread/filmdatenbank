package model;

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
    private List<Integer> actors;
    private List<Integer> directors;

    //region Getter/Setter
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
