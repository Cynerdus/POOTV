package utils;

public class User {

    private Credentials credentials;

    public User() { }

    /**
     *
     * @return          user information
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials       user information
     */
    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }
}
