package utils.structures;

public class Action {

    private String type;
    private String page;
    private String feature;
    private Credentials credentials;
    private String startsWith;
    private Filters filters;
    private String movie;
    private int count;
    private int rate;
    private String subscribedGenre;

    private Movie addedMovie;
    private String deletedMovie;

    public Action() { }

    /**
     *
     * @return      command type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type      of command
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     *
     * @return      current application page
     */
    public String getPage() {
        return page;
    }

    /**
     *
     * @param page      of application
     */
    public void setPage(final String page) {
        this.page = page;
    }

    /**
     *
     * @return      command feature
     */
    public String getFeature() {
        return feature;
    }

    /**
     *
     * @param feature       for command
     */
    public void setFeature(final String feature) {
        this.feature = feature;
    }

    /**
     *
     * @return      the User information
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials       User information
     */
    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     *
     * @return      the first character from Movie title
     */
    public String getStartsWith() {
        return startsWith;
    }

    /**
     *
     * @param startsWith        movie first characters
     */
    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    /**
     *
     * @return      filter search
     */
    public Filters getFilters() {
        return filters;
    }

    /**
     *
     * @param filters       for the movie search
     */
    public void setFilters(final Filters filters) {
        this.filters = filters;
    }

    /**
     *
     * @return      movie title
     */
    public String getMovie() {
        return movie;
    }

    /**
     *
     * @param movie     title
     */
    public void setMovie(final String movie) {
        this.movie = movie;
    }

    /**
     *
     * @return      token count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count     of tokens
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     *
     * @return      User rate
     */
    public int getRate() {
        return rate;
    }

    /**
     *
     * @param rate      from User
     */
    public void setRate(final int rate) {
        this.rate = rate;
    }

    /**
     *
     * @return      subscribed genres
     */
    public String getSubscribedGenre() {
        return subscribedGenre;
    }

    /**
     *
     * @param subscribedGenre       subscribed genres
     */
    public void setSubscribedGenre(final String subscribedGenre) {
        this.subscribedGenre = subscribedGenre;
    }

    /**
     *
     * @return      added movie
     */
    public Movie getAddedMovie() {
        return addedMovie;
    }

    /**
     *
     * @param addedMovie        added movie
     */
    public void setAddedMovie(final Movie addedMovie) {
        this.addedMovie = addedMovie;
    }

    /**
     *
     * @return      deleted movie
     */
    public String getDeletedMovie() {
        return deletedMovie;
    }

    /**
     *
     * @param deletedMovie      deleted thing
     */
    public void setDeletedMovie(final String deletedMovie) {
        this.deletedMovie = deletedMovie;
    }
}
