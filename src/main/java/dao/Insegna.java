package dao;

public class Insegna {
    private int docente;
    private int corso;
    private boolean stato;

    public Insegna(int docente, int corso){
        this.corso = corso;
        this.docente = docente;
        this.stato = true;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
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
}
