package dao;

import java.sql.*;
import java.util.ArrayList;

public class DAO {
    private String url1 = "";
    private String user = "";
    private String password = "";

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

    private Connection openConnection(){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            /*if (conn1 != null) {
                //System.out.println("Connected to the database ripetizioni");
            }*/
        }catch (SQLException e){
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

    public Corso getCorso(int codice){
        Connection c = openConnection();
        Corso corso = null;
        try{
            String sql = "SELECT * FROM corso WHERE codice = ? AND stato = true";
            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, codice);
            ResultSet rs = st.executeQuery();
            rs.next();
            corso = new Corso(rs.getInt("codice"),rs.getString("titolocorso"));
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
        return corso;
    }

    public void insertCorsi(ArrayList<Corso> corsi){
        Connection c = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO corso(titolocorso, stato) VALUES (?, ?)";
            PreparedStatement st = c.prepareStatement(sql);
            for (i = 0; i < corsi.size(); i++) {
                st.setString(1, corsi.get(i).getTitolo_corso());
                st.setBoolean(2, corsi.get(i).isStato());
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

    public boolean insertCorsi(Corso corso){
        Connection c = openConnection();
        try{
            String sql = "INSERT INTO corso(titolocorso, stato) VALUES (?, ?)";
            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, corso.getTitolo_corso());
            st.setBoolean(2, corso.isStato());
            st.executeUpdate();
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
        return true;
    }

    public ArrayList<Corso> mostraCorsi(int ID){
        Connection conn = openConnection();
        ArrayList<Corso> corsi = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM corso WHERE stato = true and codice > " + ID);
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

    public void rimuoviCorsi(ArrayList<Integer> codici){
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

    public boolean rimuoviCorsi(int codice){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE corso SET stato = false WHERE codice = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, codice);
            if(st.executeUpdate() == 0){ // forse gestire l'errore
                System.out.println("executeUpdate: Materia inesistente");
                return false;
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public Docente getDocente(int matricola){
        Connection c = openConnection();
        Docente docente = null;
        try{
            String sql = "SELECT * FROM docente where matricola = ?";
            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, matricola);
            ResultSet rs = st.executeQuery();
            rs.next();
            docente = new Docente(rs.getInt("matricola"),rs.getString("nome"),rs.getString("cognome"));
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(c);
        }
        return docente;
    }

    public void insertDocenti(ArrayList<Docente> docenti){
        Connection conn = openConnection();
        int i = 0;
        try{
            String sql = "INSERT INTO docente(cognome, nome, stato) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (i = 0; i < docenti.size(); i++) {
                st.setString(1, docenti.get(i).getCognome());
                st.setString(2, docenti.get(i).getNome());
                st.setBoolean(3, docenti.get(i).isStato());
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

    public boolean insertDocenti(Docente docente){
        Connection conn = openConnection();
        try{
            String sql = "INSERT INTO docente(cognome, nome, stato) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, docente.getCognome());
            st.setString(2, docente.getNome());
            st.setBoolean(3, docente.isStato());
            st.executeUpdate();
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public ArrayList<Docente> mostraDocenti(int ID){
        Connection conn = openConnection();
        ArrayList<Docente> docenti = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM docente WHERE stato = true and matricola > "+ID);
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

    public boolean rimuoviDocenti(ArrayList<Integer> docenti){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE docente SET stato = false WHERE matricola = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            for (Integer docente : docenti) {
                st.setInt(1, docente);
                if(st.executeUpdate() == 0){
                    System.out.println("executeUpdate: Docente inesistente");
                    return false;
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public boolean rimuoviDocenti(int docente){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE docente SET stato = false WHERE matricola = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, docente);
            if(st.executeUpdate() == 0){
                System.out.println("executeUpdate: Docente inesistente");
                return false;
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public void insertInsegnamento(ArrayList<Insegna> insegnamenti){
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

    public boolean insertInsegnamento(Insegna insegnamento){
        Connection conn = openConnection();
        try{
            String sql = "INSERT INTO insegna(corso, docente,stato) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, insegnamento.getCorso());
            st.setInt(2, insegnamento.getDocente());
            st.setBoolean(3, insegnamento.isStato());
            st.executeUpdate();
            st.close();
        }catch (SQLException e){
            System.out.println("insertInsegnamento(Insegna Insegnamento) " + e.getMessage());
            return false;
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public ArrayList<Insegna> mostraInsegnamenti(){
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

    public ArrayList<Insegnamento> getInsegnamenti(int lunghezza){
        Connection conn = openConnection();
        ArrayList<Insegnamento> insegnamenti = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet conta_insegnamenti = st.executeQuery("SELECT count(*) AS conta FROM insegna WHERE stato = true");
            System.out.println("prima count "+conta_insegnamenti);
            conta_insegnamenti.next();
            if(conta_insegnamenti.getInt("conta") != lunghezza) {
                ResultSet rs = st.executeQuery("SELECT * FROM insegna WHERE stato = true");
                while (rs.next()) {
                    Insegnamento insegnamento = new Insegnamento(getCorso(rs.getInt("corso")), getDocente(rs.getInt("docente")));
                    insegnamenti.add(insegnamento);
                }
                st.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return insegnamenti;
    }

    public ArrayList<Integer> mostraInsegnamenti(Corso c){
        Connection conn = openConnection();
        ArrayList<Integer> docenti = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM insegna WHERE stato = true AND corso=" + c);
            while (rs.next()){
                int matricola = rs.getInt("docente");
                docenti.add(matricola);
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return docenti;
    }

    public void rimuoviInsegnamento(ArrayList<Insegna> insegnamenti){
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

    public boolean rimuoviInsegnamento(Insegna insegnamento){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE insegna SET stato = false ";
            if(insegnamento.getCorso() == -1 || insegnamento.getDocente() == -1){
                sql += " WHERE";
                if (insegnamento.getDocente() != -1) {
                    sql += "  docente = ?";
                    PreparedStatement st = conn.prepareStatement(sql);
                    st.setInt(1, insegnamento.getDocente());
                    if(st.executeUpdate() == 0){
                        System.out.println("executeUpdate: Insegnamento inesistente");
                    }
                    st.close();
                }else if(insegnamento.getCorso() != -1){ // passo -1 per quando elimino solo il corso e quimdi devo rimuovere l'insegnamento
                    sql += "  corso = ?";
                    PreparedStatement st = conn.prepareStatement(sql);
                    st.setInt(1, insegnamento.getCorso());
                    if(st.executeUpdate() == 0){
                        System.out.println("executeUpdate: Insegnamento inesistente");
                    }
                    st.close();
                }
            }else if(insegnamento.getCorso() != -1 || insegnamento.getDocente() != -1){
                sql += " WHERE docente = ? and corso= ?";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, insegnamento.getDocente());
                st.setInt(2, insegnamento.getCorso());
                if(st.executeUpdate() == 0){
                    System.out.println("executeUpdate: Insegnamento inesistente");
                }
                st.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public Utente getUtente(String nomeutente){
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

    public boolean insertUtente(Utente utente){
        Connection conn = openConnection();
        int i = 0;
        boolean res = false;
        try{
            String sql = "INSERT INTO utente(nomeutente, password, ruolo, stato) VALUES (?, ?, ?, true)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, utente.getNome_utente());
            st.setString(2, utente.getPassword());
            st.setString(3, utente.getRuolo());
            int n = st.executeUpdate();
            System.out.println("n = " + n);
            if(n == 1){
                res = true;
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return res;
    }

    public boolean editPassword(String utente, String nuovaPassword){
        boolean res = false;
        Connection conn = openConnection();
        try{
            String sql = "UPDATE utente SET password = '"+nuovaPassword+"' WHERE nomeutente = '" + utente + "'";
            Statement st = conn.createStatement();
            if(st.executeUpdate(sql) == 1)
                res = true;
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return res;
    }



    public ArrayList<Utente> mostraUtenti(){
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

    public void rimuoviUtente(ArrayList<String> utenti){
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

    public void rimuoviUtente(String utente){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE utente SET stato = false WHERE nomeutente = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, utente);
            if(st.executeUpdate() == 0){
                System.out.println("executeUpdate: Utente inesistente");
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
    }

    public Utente utenteEsistente(String nomeutente, String password){
        Connection conn = openConnection();
        Utente usr = new Utente(null, null, null);
        usr.setStato(false); //utente inesistente
        try{
            String sql = "SELECT * FROM utente WHERE nomeutente = '" + nomeutente + "' AND stato = true";
            Statement st =  conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                //System.out.println("SIAMO IN IF")
                usr = new Utente(rs.getString("nomeutente"),rs.getString("password"),rs.getString("ruolo"));
                if(!usr.getPassword().equals(password)) {
                    usr = new Utente(null, null, null);
                    usr.setStato(true); //utente esistente ma password errata
                }
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return usr;
    }

    public ArrayList<Integer> insertPrenotazione(ArrayList<Prenotazione> prenotazioni){
        Connection conn = openConnection();
        ArrayList<Integer> res = new ArrayList<>();
        System.out.println("in insertPrenotazione");
        int i = 0;
        try{
            String sql = "INSERT INTO prenotazione(corso, docente, utente, giorno, ora, stato) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (i = 0; i < prenotazioni.size(); i++) {
                if(controlloPrenotazioniEsistenti(prenotazioni.get(i).getDocente().getMatricola(), prenotazioni.get(i).getGiorno().toLowerCase(), prenotazioni.get(i).getOra())){
                    st.setInt(1, prenotazioni.get(i).getCorso().getCodice());
                    st.setInt(2, prenotazioni.get(i).getDocente().getMatricola());
                    st.setString(3, prenotazioni.get(i).getUtente());
                    st.setString(4, prenotazioni.get(i).getGiorno().toLowerCase());
                    st.setString(5, prenotazioni.get(i).getOra());
                    st.setBoolean(6, prenotazioni.get(i).isStato());
                    st.executeUpdate();
                }else{
                    System.out.println("prenotazioneEsistente");
                    res.add(i);
                    //break;
                }
            }
            //System.out.println(st.toString());
            st.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 1062){ //tupla già presente
                System.out.println("***** qualcosa che non dove succedere *****");
//                prenotazioni.remove(i);
//                insertPrenotazione(prenotazioni);
            }
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return res;
    }

    public boolean controlloPrenotazioniEsistenti(int docente,String giorno,String ora){
        Connection conn = openConnection();
        try{
            String sql = "SELECT * FROM prenotazione WHERE docente = " + docente + " AND giorno = '" + giorno + "' AND ora = '" + ora + "' AND stato = true";
            System.out.println(sql);
            Statement st =  conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return false;
            }
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

//    public void insertPrenotazione(Prenotazione prenotazione){
//        Connection conn = openConnection();
//        int i = 0;
//        try{
//            String sql = "INSERT INTO prenotazione(corso, docente, utente, giorno, ora, stato) VALUES (?, ?, ?, ?, ?, ?)";
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setInt(1, prenotazione.getCorso().getCodice());
//            st.setInt(2, prenotazione.getDocente().getMatricola());
//            st.setString(3,prenotazione.getUtente());
//            st.setString(4, prenotazione.getGiorno());
//            st.setString(5, prenotazione.getOra());
//            st.setBoolean(6,prenotazione.isStato());
//            st.executeUpdate();
//            st.close();
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }finally {
//            closeConnection(conn);
//        }
//    }

    public ArrayList<Docente> docentiDisponibili(Integer corso, String giorno, String ora){
        Connection conn = openConnection();
        ArrayList<Docente> docentiDisp = new ArrayList<>();
        try{
            String queryInternaInterna1 = "SELECT docente FROM prenotazione p WHERE p.stato = 1 AND p.docente = i.docente AND p.ora = '"+ora+"' AND p.giorno='"+giorno +"'";
            String queryInternaInterna2 = "SELECT docente FROM prenotazione p WHERE p.stato = 1 AND P.corso ="+ corso +" AND p.ora = '"+ora+"' AND p.giorno= '"+giorno+"'";
            String queryInterna1 = "SELECT i.docente FROM insegna i WHERE i.stato  = 1 AND i.corso =" + corso + " AND NOT EXISTS( " + queryInternaInterna1 + ") ";
            String queryInterna2 = "SELECT dd.docente  FROM ( " + queryInternaInterna2 + ") dd ";
            String queryEsterna = "SELECT dl.docente FROM ( " + queryInterna1 + " ) dl WHERE dl.docente NOT IN(" + queryInterna2 + ")";
            //System.out.println(queryEsterna);
            PreparedStatement st = conn.prepareStatement(queryEsterna);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                docentiDisp.add(getDocente(rs.getInt("docente")));
            }
            System.out.println("docenti" + docentiDisp.size());
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        if(docentiDisp.isEmpty()) {
            System.out.println("ciao");
            return null;
        }
        System.out.println(docentiDisp.get(0));
        return docentiDisp;
    }

    // 0 == libero
    // 1 == docente occupato
    // 2 == utente occupato

    public int[][] prenotazioni_disp(Integer docente, Integer corso, String utente){

        System.out.println("prenotazioni_disp Param: " + docente + corso + utente);

        Connection conn = openConnection();
        ArrayList<Prenotazione> prenotazioni = null;
        int size = 5;
        int griglia[][] = new int[size - 1][size];

        //rendiamo disponibili tutte le lezio
        for(int i = 0; i < size- 1; i++){
            for(int j = 0; j < size; j++){
                griglia[i][j] = 0;
            }
        }
        try {
            prenotazioni = mostraPrenotazioni(docente, corso, utente);
            if(docente != null) {
                for (Prenotazione p : prenotazioni) {
                    int y = DayToIndex(p.getGiorno());
                    System.out.println("break1");
                    int x = HourToIndex(p.getOra());
                    System.out.println("break2");
                    if (utente != null && p.getUtente().equals(utente)) {
                        griglia[x][y] = 2;
                        System.out.println("break4");
                    }else if (p.getDocente().getMatricola() == docente) {
                        griglia[x][y] = 1;
                        System.out.println("break3");
                    }
                }
                System.out.println("in prenotazioni disp: dopo il for");
            }
            else if(corso != null){
                String[] giorni = {"lunedì","martedì","mercoledì","giovedì","venerdì"};
                String[] ore = {"15:00:00","16:00:00","17:00:00","18:00:00"};
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 4; j++){
                        if(docentiDisponibili(corso, giorni[i], ore[j]) == null){
                            griglia[j][i] = 1;
                        }
                    }
                }
                for (Prenotazione p : prenotazioni){
                    int y = DayToIndex(p.getGiorno());
                    int x = HourToIndex(p.getOra());
                    if (utente != null && p.getUtente().equals(utente)) {
                        griglia[x][y] = 2;
                    }
                }
                    /*
                    SELECT dl.docente //query totale: docenti di un corso liberi in un orario preciso
                    FROM ( // query interna fa : docenti che non insegnano un corso in un orario preciso
                        SELECT i.docente
                        FROM insegna i
                        WHERE i.corso = 1234 AND NOT EXISTS(
                            SELECT docente
                            FROM prenotazione p
                            WHERE p.docente = i.docente AND p.ora = '15:00:00' AND p.giorno='lunedì'
                        )
                    ) dl
                    WHERE dl.docente NOT IN(
                        SELECT dd.docente
                        FROM ( // query interna fa : docenti che insegnano un corso in un orario preciso
                            SELECT docente
                            FROM prenotazione p
                            WHERE P.corso = 1234 AND p.ora = '15:00:00' AND p.giorno='lunedì'
                        ) dd
   	                )
                    */

                    //docenti totali di informatica liberi lunedì alle 15 - (docenti occupati lunedì alle 15 che insegnano informatica)
            }
        }finally {
            closeConnection(conn);
        }
        return griglia;
    }

    private static int DayToIndex(String giorno){
        int indice = -1;
        giorno = giorno.toLowerCase();
        switch(giorno){
            case "lunedì":
                indice = 0;
                break;
            case "martedì":
                indice =  1;
                break;
            case "mercoledì":
                indice =  2;
                break;
            case "giovedì":
                indice = 3;
                break;
            case "venerdì":
                indice = 4;
                break;
        }
        return indice;
    }

    private static int HourToIndex(String ora){
        int indice = -1;
        switch(ora){
            case "15:00:00":
                indice = 0;
                break;
            case "16:00:00":
                indice =  1;
                break;
            case "17:00:00":
                indice =  2;
                break;
            case "18:00:00":
                indice = 3;
                break;
        }
        return indice;
    }

    public ArrayList<Prenotazione> mostraPrenotazioni(String usr,Integer ID){
        Connection conn = openConnection();
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        try{
            String sql= "SELECT * FROM prenotazione WHERE idPrenotazione > " + ID;
            if(usr != null){
                sql +=" and utente= '"+usr+"' ";
            }
            System.out.println(sql);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                Corso c=getCorso(rs.getInt("corso"));
                Docente d=getDocente(rs.getInt("docente"));
                Prenotazione prenotazione = new Prenotazione(rs.getInt("idPrenotazione"),c,d,rs.getString("utente"),rs.getString("giorno"),rs.getString("ora"),rs.getBoolean("stato"),rs.getBoolean("effettuata"));
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

    public ArrayList<Prenotazione> mostraPrenotazioni(Integer docente, Integer corso, String utente){
        String sql2 = "";
        Connection conn = openConnection();
        ArrayList<Prenotazione> prenotazioni = null;
        if(corso != null || docente != null){
            prenotazioni = new ArrayList<Prenotazione>();
            String sql = "SELECT * FROM prenotazione WHERE stato = true AND ( ";
            if(utente != null)
                sql += "utente = '" + utente + "' OR ";
            try{
                Statement st = conn.createStatement();
                System.out.println(corso);
                if(docente != null && corso != null){
                    sql2 = "SELECT * FROM insegna WHERE  docente = " + docente + " and corso = " + corso;
                    ResultSet rs2 = st.executeQuery(sql2);
                    if(rs2.next()){
                        sql += "docente= " + docente + ")";
                    }
                }
                else if(docente != null){
                    sql += "docente= " + docente + ")";
                }
                else if(corso != null){
                    sql += "corso = " + corso + ")";
                }
                System.out.println("mostraprenotazioni : " + sql);
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    Corso c=getCorso(rs.getInt("corso"));
                    Docente d=getDocente(rs.getInt("docente"));
                    Prenotazione prenotazione = new Prenotazione(rs.getInt("idPrenotazione"),c, d, rs.getString("utente"), rs.getString("giorno"), rs.getString("ora"),rs.getBoolean("stato"),rs.getBoolean("effettuata"));
                    prenotazioni.add(prenotazione);
                }
                st.close();
            }catch (SQLException e){
                System.out.println("mostraPrenotazioni:" + e.getMessage());
            }finally{
                closeConnection(conn);
            }
        }
        return prenotazioni;
    }

    public Boolean prenotazioneEff(Prenotazione p){
        Connection conn = openConnection();
        try{
            if(p!=null){
                String sql = "UPDATE prenotazione SET stato = false , effettuata = true WHERE (docente = "+p.getDocente().getMatricola() +" and giorno = '"+p.getGiorno() +"' and ora = '"+p.getOra()+"' and utente= '"+p.getUtente()+"')";
                System.out.println(sql);
                Statement st = conn.createStatement();
                st.executeUpdate(sql);
                st.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public void rimuoviPrenotazioni(ArrayList<Prenotazione> prenotazioni){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE prenotazione SET stato = false WHERE (docente = ? and giorno = ? and ora = ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            for (Prenotazione prenotazione : prenotazioni) {
                st.setInt(1, prenotazione.getDocente().getMatricola());
                st.setString(2, prenotazione.getGiorno().toLowerCase());
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

    public boolean rimuoviPrenotazioni(Prenotazione p){
        Connection conn = openConnection();
        try{
            String sql = "UPDATE prenotazione SET stato = false , effettuata = false WHERE (docente = "+p.getDocente().getMatricola() +" and giorno = '"+p.getGiorno() +"' and ora = '"+p.getOra()+"' and utente= '"+p.getUtente()+"')";
            System.out.println(sql);
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection(conn);
        }
        return true;
    }

    public void svuota_tabelle(){
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
