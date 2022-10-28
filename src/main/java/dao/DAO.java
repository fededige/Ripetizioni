package dao;

import java.sql.*;
import java.util.ArrayList;

public class DAO {
    private static String url1 = "";
    private static String user = "";
    private static String password = "";

    public DAO(String url, String user, String password){
        this.url1 = url;
        this.user = user;
        this.password = password;
    }

    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private static Connection openConnection(){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            /*if (conn1 != null) {
                //System.out.println("Connected to the database ripetizioni");
            }*/
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn1;
    }

    private static void closeConnection(Connection conn){
        if(conn != null){
            try{
                conn.close();
                //System.out.println("Connection closed to the database ripetizioni");
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static Corso getCorso(int codice){
        Connection c = openConnection();
        Corso corso = null;
        try{
            String sql = "SELECT * FROM corso where codice = ?";
            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, codice);
            ResultSet rs = st.executeQuery();
            corso = new Corso(rs.getInt("codice"),rs.getString("titolocorso"));
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
        return corso;
    }

    public static void insertCorsi(ArrayList<Corso> corsi){
        Connection c = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO corso(codice, titolocorso, stato) VALUES (?, ?, ?)";
            PreparedStatement st = c.prepareStatement(sql);
            for (i = 0; i < corsi.size(); i++) {
                st.setInt(1, corsi.get(i).getCodice());
                st.setString(2, corsi.get(i).getTitolo_corso());
                st.setBoolean(3, corsi.get(i).isStato());
                st.executeUpdate();
            }
            st.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 1062){ //tupla già presente
                corsi.remove(i);
                insertCorsi(corsi);
            }
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
    }

    public static ArrayList<Corso> mostraCorsi(){
        Connection conn = openConnection();
        ArrayList<Corso> corsi = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM corso WHERE stato=true");
            while (rs.next()){
                Corso c = new Corso(rs.getInt("codice"),rs.getString("titolocorso"));
                corsi.add(c);
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return corsi;
    }

    public static void rimuoviCorsi(ArrayList<Integer> codici){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE corso SET stato = false WHERE codice = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            for (Integer codice : codici) {
                st.setInt(1, codice);
                if(st.executeUpdate() == 0){ // forse gestire l'errore
                    System.out.println("executeUpdate: Materia inesistente");
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static Docente getDocente(int matricola){
        Connection c = openConnection();
        Docente docente = null;
        try{
            String sql = "SELECT * FROM docente where matricola = ?";
            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, matricola);
            ResultSet rs = st.executeQuery();
            docente = new Docente(rs.getInt("matricola"),rs.getString("nome"),rs.getString("cognome"));
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
        return docente;
    }

    public static void insertDocenti(ArrayList<Docente> docenti){
        Connection conn = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO docente(matricola, cognome, nome, stato) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (i = 0; i < docenti.size(); i++) {
                st.setInt(1, docenti.get(i).getMatricola());
                st.setString(2, docenti.get(i).getCognome());
                st.setString(3, docenti.get(i).getNome());
                st.setBoolean(4, docenti.get(i).isStato());
                st.executeUpdate();
            }
            st.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 1062){ //tupla già presente
                docenti.remove(i);
                insertDocenti(docenti);
            }
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static ArrayList<Docente> mostraDocenti(){
        Connection conn = openConnection();
        ArrayList<Docente> docenti = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM docente WHERE stato = true");
            while (rs.next()){
                Docente d = new Docente(rs.getInt("matricola"),rs.getString("nome"), rs.getString("cognome"));
                docenti.add(d);
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return docenti;
    }

    public static void rimuoviDocenti(ArrayList<Integer> docenti){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE docente SET stato = false WHERE matricola = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            for (Integer docente : docenti) {
                st.setInt(1, docente);
                if(st.executeUpdate() == 0){
                    System.out.println("executeUpdate: Docente inesistente");
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static void insertInsegnamento(ArrayList<Insegna> insegnamenti){
        Connection conn = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO insegna(corso, docente,stato) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (i = 0; i < insegnamenti.size(); i++) {
                st.setInt(1, insegnamenti.get(i).getCorso());
                st.setInt(2, insegnamenti.get(i).getDocente());
                st.setBoolean(3, insegnamenti.get(i).isStato());
                st.executeUpdate();
            }
            st.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 1062){ //tupla già presente
                insegnamenti.remove(i);
                insertInsegnamento(insegnamenti);
            }
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static ArrayList<Insegna> mostraInsegnamenti(){
        Connection conn = openConnection();
        ArrayList<Insegna> insegnamenti = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM insegna WHERE stato = true");
            while (rs.next()){
                Insegna insegna = new Insegna(rs.getInt("docente"),rs.getInt("corso"));
                insegnamenti.add(insegna);
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return insegnamenti;
    }


    public static void rimuoviInsegnamento(ArrayList<Insegna> insegnamenti){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE insegna SET stato = false WHERE docente = ? and corso= ?";
            PreparedStatement st = conn.prepareStatement(sql);
            for (Insegna insegna : insegnamenti) {
                st.setInt(1, insegna.getDocente());
                st.setInt(2, insegna.getCorso());
                if(st.executeUpdate() == 0){
                    System.out.println("executeUpdate: Insegnamento inesistente");
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static Utente getUtente(String nomeutente){
        Connection c = openConnection();
        Utente utente = null;
        try{
            String sql = "SELECT * FROM utente where nomeutente = ?";
            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, nomeutente);
            ResultSet rs = st.executeQuery();
            utente = new Utente(rs.getString("nomeutente"),rs.getString("password"),rs.getString("ruolo"));
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
        return utente;
    }

    public static void insertUtente(ArrayList<Utente> utenti){
        Connection conn = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO utente(nomeutente, password, ruolo, stato) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (i = 0; i < utenti.size(); i++) {
                st.setString(1, utenti.get(i).getNome_utente());
                st.setString(2, utenti.get(i).getPassword());
                st.setString(3, utenti.get(i).getRuolo());
                st.setBoolean(4, utenti.get(i).isStato());
                st.executeUpdate();
            }
            st.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 1062){ //tupla già presente
                utenti.remove(i);
                insertUtente(utenti);
            }
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static ArrayList<Utente> mostraUtenti(){
        Connection conn = openConnection();
        ArrayList<Utente> utenti = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM utente WHERE stato = true");
            while (rs.next()){
                Utente utente = new Utente(rs.getString("nomeutente"),rs.getString("password"),rs.getString("ruolo"));
                utenti.add(utente);
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return utenti;
    }

    public static void rimuoviUtente(ArrayList<String> utenti){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE utente SET stato = false WHERE nomeutente = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            for (String utente : utenti) {
                st.setString(1, utente);
                if(st.executeUpdate() == 0){
                    System.out.println("executeUpdate: Utente inesistente");
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static Utente utenteEsistente(String nomeutente, String password){
        Connection conn = openConnection();
        Utente usr = null;
        try{
            String sql = "SELECT * FROM utente WHERE nomeutente = '" + nomeutente + "'  AND password = '" + password + "' AND stato = 1";
            Statement st =  conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("pre next");
            if(rs.next()){
                System.out.println("SIAMO IN IF");
                usr = new Utente(rs.getString("nomeutente"),rs.getString("password"),rs.getString("ruolo"));
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return usr;
    }

    public static void insertPrenotazione(ArrayList<Prenotazione> prenotazioni){
        Connection conn = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO prenotazione(corso, docente, utente, giorno, ora, stato) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (i = 0; i < prenotazioni.size(); i++) {
                st.setInt(1, prenotazioni.get(i).getCorso());
                st.setInt(2, prenotazioni.get(i).getDocente());
                st.setString(3, prenotazioni.get(i).getUtente());
                st.setString(4, prenotazioni.get(i).getGiorno());
                st.setString(5, prenotazioni.get(i).getOra());
                st.setBoolean(6, prenotazioni.get(i).isStato());
                st.executeUpdate();
            }
            st.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 1062){ //tupla già presente
                prenotazioni.remove(i);
                insertPrenotazione(prenotazioni);
            }
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static ArrayList<Prenotazione> mostraPrenotazioni(){
        Connection conn = openConnection();
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE stato = true");
            while (rs.next()){
                Prenotazione prenotazione = new Prenotazione(rs.getInt("corso"),rs.getInt("docente"),rs.getString("utente"),rs.getString("giorno"),rs.getString("ora"));
                prenotazioni.add(prenotazione);
             }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally{
            closeConnection(conn);
        }
        return prenotazioni;
    }

    public static void rimuoviPrenotazioni(ArrayList<Prenotazione> prenotazioni){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE prenotazione SET stato = false WHERE (docente = ? and giorno = ? and ora = ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (Prenotazione prenotazione : prenotazioni) {
                st.setInt(1, prenotazione.getDocente());
                st.setString(2, prenotazione.getGiorno());
                st.setString(3, prenotazione.getOra());
                if(st.executeUpdate() == 0){
                    System.out.println("executeUpdate: Prenotazione inesistente");
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public static void svuota_tabelle(){
        Connection conn = openConnection();
        try{
            String sql = "DELETE FROM prenotazione";
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            sql ="DELETE FROM insegna";
            st.executeUpdate(sql);
            sql = "DELETE FROM corso";
            st.executeUpdate(sql);
            sql =  "DELETE FROM docente";
            st.executeUpdate(sql);
            sql ="DELETE FROM utente";
            st.executeUpdate(sql);
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }
}
