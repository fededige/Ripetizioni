package dao;

public class Docente {
    private int matricola;
    private String nome;
    private String cognome;
    private boolean stato;
    public Docente(int matricola, String nome, String cognome){
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.stato = true;
    }

    public int getMatricola() {
        return matricola;
    }

    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }
}
