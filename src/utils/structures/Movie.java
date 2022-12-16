package utils.structures;

import java.util.ArrayList;

public class Movie {

    private String name;
    private int year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;

    public Movie() { }

    /**
     *
     * @return      Movie name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name      of the Movie
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return      release year
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @param year      of release
     */
    public void setYear(final int year) {
        this.year = year;
    }

    /**
     *
     * @return      time in minutes for Movie
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     * @param duration      time in minutes for Movie
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     *
     * @return      list of Movie genres
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres        list of Movie genres
     */
    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    /**
     *
     * @return      list of starring actors
     */
    public ArrayList<String> getActors() {
        return actors;
    }

    /**
     *
     * @param actors        list of starring actors
     */
    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    /**
     *
     * @return      list of countries where Movie is banned
     */
    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    /**
     *
     * @param countriesBanned       list of countries where Movie is banned
     */
    public void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }
}
