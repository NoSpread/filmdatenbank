package model;

import java.util.List;

public class Actor {
    private int id;
    private String name;
    private List<Integer> movies;

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

    public Actor setName(String name) {
        this.name = name;
        return this;
    }
    // endregion
}
