package model;

import java.util.ArrayList;
import java.util.List;

public class Actor {
    private int id;
    private String name;
    private List<Movie> movies = new ArrayList<>();

    // region Getter/Setter
    public int getId() {
        return id;
    }

    public Actor setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Actor setName(String name) {
        this.name = name;
        return this;
    }
    // endregion
}
