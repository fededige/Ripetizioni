package dao;

public class Insegnamento {
    Corso corso;
    Docente docente;

    public Insegnamento(Corso c,Docente d){
        this.corso=c;
        this.docente=d;
    }

    public Corso getCorso(){
        return this.corso;
    }

    public void setCorso(Corso corso){
        this.corso = corso;
    }


    public void setDocente(Docente d){
        this.docente=d;
    }

    public Docente getDocente(){
        return this.docente;
    }
}
