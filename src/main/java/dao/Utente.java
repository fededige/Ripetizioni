package dao;

public class Utente {
    private String nome_utente;
    private String password;
    private String ruolo;
    private boolean stato;

    public Utente(String nome_utente, String password, String ruolo){
        this.nome_utente=nome_utente;
        this.password=password;
        this.ruolo=ruolo;
        this.stato=true;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome_utente() {
        return nome_utente;
    }

    public void setNome_utente(String nome_utente) {
        this.nome_utente = nome_utente;
    }
}
