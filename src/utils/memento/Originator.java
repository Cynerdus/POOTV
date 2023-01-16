package utils.memento;

public class Originator {
    private String pageName;

    /**
     *
     * @return      state / page name
     */
    public String getState() {
        return pageName;
    }

    /**
     *
     * @param pageName      new state
     */
    public void setState(final String pageName1) {
        pageName = pageName1;
    }

    /**
     *
     * @return      new memento
     */
    public Memento saveToMemento() {
        return new Memento(pageName);
    }

    /**
     *
     * @param memento       memento
     */
    public void getStateFromMemento(final Memento memento) {
        pageName = memento.getState();
    }
}
