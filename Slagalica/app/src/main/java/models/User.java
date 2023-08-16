package models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private int id;
    private String username;

    private String picturePath;

    private Integer token;
/*
    private List<Game> numberOfGames;
*/

    public User(int id, String username, String picturePath) {
        this.id = id;
        this.username = username;
        this.picturePath = picturePath;
    }

    public User() {

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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
