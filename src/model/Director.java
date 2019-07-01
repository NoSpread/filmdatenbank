package model;

import java.util.ArrayList;
import java.util.List;

public class Director {
    private int id;
    private String name;
    private List<Integer> movies = new ArrayList<>();

    // region Getter/Setter
    public int getId() {
        return id;
    }

    public Director setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Director setName(String name) {
        this.name = name;
        return this;
    }

    public List<Integer> getMovies() {
        return movies;
    }

    public Director setMovies(List<Integer> movies) {
        this.movies = movies;
        return this;
    }

    // endregion
}
