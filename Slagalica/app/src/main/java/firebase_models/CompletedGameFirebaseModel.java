package firebase_models;

import java.io.Serializable;

public class CompletedGameFirebaseModel implements Serializable {

    private Integer bodovi1;

    private Integer bodovi2;

    public Integer getBodovi1() {
        return bodovi1;
    }

    public void setBodovi1(Integer bodovi1) {
        this.bodovi1 = bodovi1;
    }

    public Integer getBodovi2() {
        return bodovi2;
    }

    public void setBodovi2(Integer bodovi2) {
        this.bodovi2 = bodovi2;
    }
}
