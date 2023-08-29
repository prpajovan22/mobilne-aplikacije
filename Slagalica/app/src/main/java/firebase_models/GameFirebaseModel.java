package firebase_models;

import java.io.Serializable;

public class GameFirebaseModel implements Serializable {

    private String spojnice,asocijacije,koZnaZna;

    private Integer bodovi1;

    private Integer bodovi2;

    public GameFirebaseModel(String spojnice, String asocijacije, String koZnaZna, Integer bodovi1, Integer bodovi2) {
        this.spojnice = spojnice;
        this.asocijacije = asocijacije;
        this.koZnaZna = koZnaZna;
        this.bodovi1 = bodovi1;
        this.bodovi2 = bodovi2;
    }

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
