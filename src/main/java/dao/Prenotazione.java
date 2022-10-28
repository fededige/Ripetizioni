package dao;

public class Prenotazione {
    private int corso;
    private int docente;
    private String utente;
    private String giorno;
    private String ora;
    private boolean stato;

    public Prenotazione(int corso, int docente, String utente, String giorno, String ora){
        this.corso = corso;
        this.docente = docente;
        this.utente = utente;
        this.giorno = giorno;
        this.ora = ora;
        this.stato = true;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public int getCorso() {
        return corso;
    }

    public void setCorso(int corso) {
        this.corso = corso;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }
}
