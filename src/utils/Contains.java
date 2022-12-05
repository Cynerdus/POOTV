package utils;

import java.util.ArrayList;

public class Contains {

    private ArrayList<String> actors;
    private ArrayList<String> genre;

    public Contains() { }

    /**
     *
     * @return      the actors preferences
     */
    public ArrayList<String> getActors() {
        return actors;
    }

    /**
     *
     * @param actors        actors preferences
     */
    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    /**
     *
     * @return      the genres option for filter
     */
    public ArrayList<String> getGenre() {
        return genre;
    }

    /**
     *
     * @param genre     genres option for filter
     */
    public void setGenre(final ArrayList<String> genre) {
        this.genre = genre;
    }
}
