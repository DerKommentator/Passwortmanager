package model.datenstruktur;

public class AccountInfo {
    private String email;
    private String website;
    private String name;
    private String password;
    private String username;
    private long id;

    public AccountInfo(long id, String email, String website, String name, String password, String username) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.website = website;
        this.name = name;
        this.password = password;
    }

    public AccountInfo(String email, String website, String name, String password, String username) {
        this.username = username;
        this.email = email;
        this.website = website;
        this.name = name;
        this.password = password;
    }

    public AccountInfo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
