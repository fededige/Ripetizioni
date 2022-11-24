package dao;

public class Prenotazione {
    private Corso corso;
    private Docente docente;
    private String utente;
    private String giorno;
    private String ora;
    private boolean stato;

    private boolean effettuata;

    public Prenotazione(Corso corso, Docente docente, String utente, String giorno, String ora,boolean stato,boolean effettuata){
        this.corso = corso;
        this.docente = docente;
        this.utente = utente;
        this.giorno = giorno;
        this.ora = ora;
        this.stato = stato;
        this.effettuata=effettuata;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public Corso getCorso() {
        return corso;
    }

    public void setCorso(Corso corso) {
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

    public void setEffettuata(boolean effettuata){
        this.effettuata=effettuata;
    }

    public boolean getEffettuata(){
        return  this.effettuata;
    }
}
