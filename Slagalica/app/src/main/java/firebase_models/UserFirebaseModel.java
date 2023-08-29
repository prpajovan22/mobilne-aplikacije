package firebase_models;

import java.io.Serializable;

public class UserFirebaseModel implements Serializable {

    private String gameId;

    public UserFirebaseModel(){}

    public UserFirebaseModel(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
