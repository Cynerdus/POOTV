package utils.memento;

public class Originator {
    private String pageName;

    public String getState() {
        return pageName;
    }

    public void setState(String pageName) {
        this.pageName = pageName;
    }

    public Memento saveToMemento() {
        return new Memento(pageName);
    }

    public void getStateFromMemento(Memento memento) {
        pageName = memento.getState();
    }
}
