package model.datenstruktur;

import java.nio.file.Path;

public class Account {
    private String username;
    private String email;
    private String password;
    private String dbPath;

    // Konstruktor
    public Account(String username, String email, String password, String dbPath) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dbPath = dbPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDBPath() {
        return dbPath;
    }

    public void setDBPath(String dbPath) {
        this.dbPath = dbPath;
    }
}
