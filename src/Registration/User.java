package Registration;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty ID;
    private final StringProperty LOGIN;
    private final StringProperty PASSWORD;
    private final StringProperty EMAIL;

    public User(String id, String login, String password, String email) {
        this.ID = new SimpleStringProperty(id);
        this.LOGIN = new SimpleStringProperty(login);
        this.PASSWORD = new SimpleStringProperty(password);
        this.EMAIL = new SimpleStringProperty(email);
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getLOGIN() {
        return LOGIN.get();
    }

    public StringProperty LOGINProperty() {
        return LOGIN;
    }

    public void setLOGIN(String LOGIN) {
        this.LOGIN.set(LOGIN);
    }

    public String getPASSWORD() {
        return PASSWORD.get();
    }

    public StringProperty PASSWORDProperty() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD.set(PASSWORD);
    }

    public String getEMAIL() {
        return EMAIL.get();
    }

    public StringProperty EMAILProperty() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL.set(EMAIL);
    }
}
