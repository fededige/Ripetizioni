package dao;

public class Corso {
    private int codice;
    private String titolo_corso;
    private boolean stato;

    public Corso(int codice, String titolo_corso){
        this.codice = codice;
        this.titolo_corso = titolo_corso;
        this.stato = true;
    }

    public String getTitolo_corso() {
        return titolo_corso;
    }

    public void setTitolo_corso(String titolo_corso) {
        this.titolo_corso = titolo_corso;
    }

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }
}
