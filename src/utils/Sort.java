package utils;

public class Sort {

    private String rating;
    private String duration;

    public Sort() { }

    /**
     *
     * @return      sorting option for rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating        sorting option for rating
     */
    public void setRating(final String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return      sorting option for duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     *
     * @param duration      sorting option for duration
     */
    public void setDuration(final String duration) {
        this.duration = duration;
    }
}
