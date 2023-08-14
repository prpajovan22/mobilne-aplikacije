package models;

public class Spojnice {

    private String a;
    private String aa;
    private String b;
    private String bb;
    private String c;
    private String cc;
    private String d;
    private String dd;

    public Spojnice(String a, String aa, String b, String bb, String c, String cc, String d, String dd) {
        this.a = a;
        this.aa = aa;
        this.b = b;
        this.bb = bb;
        this.c = c;
        this.cc = cc;
        this.d = d;
        this.dd = dd;
    }
    public Spojnice(){}

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }
}
