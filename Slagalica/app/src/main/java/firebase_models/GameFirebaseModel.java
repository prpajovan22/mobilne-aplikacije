package firebase_models;

import java.io.Serializable;

public class GameFirebaseModel implements Serializable {

    private String spojnice,asocijacije,koZnaZna;

    public GameFirebaseModel(String spojnice, String asocijacije, String koZnaZna) {
        this.spojnice = spojnice;
        this.asocijacije = asocijacije;
        this.koZnaZna = koZnaZna;
    }

    public GameFirebaseModel(){}

    public String getSpojnice() {
        return spojnice;
    }

    public void setSpojnice(String spojnice) {
        this.spojnice = spojnice;
    }

    public String getAsocijacije() {
        return asocijacije;
    }

    public void setAsocijacije(String asocijacije) {
        this.asocijacije = asocijacije;
    }

    public String getKoZnaZna() {
        return koZnaZna;
    }

    public void setKoZnaZna(String koZnaZna) {
        this.koZnaZna = koZnaZna;
    }
}
