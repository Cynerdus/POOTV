package utils.structures;

public class Notification {
    private String movieName;
    private String message;

    public Notification() {
    }

    /**
     *
     * @return      notification movie
     */
    public String getMovieName() {
        return movieName;
    }

    /**
     *
     * @param movieName     motification movie
     */
    public void setMovieName(final String movieName) {
        this.movieName = movieName;
    }

    /**
     *
     * @return      notification message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message       notification message
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
