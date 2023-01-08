package utils.memento;

public class Memento {
    private String pageName;

    public Memento(String pageName) {
        this.pageName = pageName;
    }

    public String getState() {
        return pageName;
    }

    public void setState(String pageName) {
        this.pageName = pageName;
    }
}
