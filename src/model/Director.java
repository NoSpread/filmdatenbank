package model;

public class Director {
    private int id;
    private String name;

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
    // endregion
}
