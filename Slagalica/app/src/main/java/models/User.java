package models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private int id;
    private String username;
/*
    private List<Game> numberOfGames;
*/

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }
    public User(){

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
}
