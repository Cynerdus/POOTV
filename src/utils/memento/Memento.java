package utils.memento;

public class Memento {
    private String pageName;

    public Memento(final String pageName) {
        this.pageName = pageName;
    }

    /**
     *
     * @return      state
     */
    public String getState() {
        return pageName;
    }

    /**
     *
     * @param pageName      state
     */
    public void setState(final String pageName1) {
        pageName = pageName1;
    }
}
