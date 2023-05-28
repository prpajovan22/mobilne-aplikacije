package models;

import java.util.List;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private List<Game> numberOfGames;

    public User(int id, String username, String password, String email,List<Game> numberOfGames) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.numberOfGames = numberOfGames;
    }

    public User(String username, String password, String email,List<Game> numberOfGames) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.numberOfGames = numberOfGames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Game> getNumberOfGames() { return numberOfGames; }

    public void setNumberOfGames(List<Game> numberOfGames) { this.numberOfGames = numberOfGames;}
}
