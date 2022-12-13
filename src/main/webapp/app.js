import {router} from "./router.js";


var app = Vue.createApp({
    el: '#app',
    data() {
        return {
            paginaHome: true,
            paginaLogIn: false,
            paginaPrenotazioni: false,
            paginaStorico: false,
            paginaCambioPwd: false,
            paginaModificaDocente: false,
            paginaModificaCorso: false,
            paginaModificaInsegnamenti: false,
            ruolo: "ospite",
            utente: null,
            InputNomeUtente: null,
            InputPassword: null,
            InputNomeDocente: null,
            InputCognomeDocente: null,
            InputCodiceCorso: null,
            messaggio: "",
            carrelloPop: false,
            insegnamenti: [],
            docenti: [{"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}],
            docentiL: [{"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}],
            NuovoDocente: {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true},
            NuovoCorso: {"codice": 0, "titolo_corso": " ", "stato": true},
            docenteScelto: null,
            labelDocenteScelto: null,
            docenteSceltoDaEliminare: [{"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}],
            corsoSceltoDaEliminare: [{"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}],
            insegnamentoSceltoDaEliminare: [{
                "corso": {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true},
                "docente": {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true},
                "stato": true
            }],
            docenteSceltoDaAggiungere: [{"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}],
            corsoSceltoDaAggiungere: [{"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}],
            corsi: [{"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}],
            corsiL: [{"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}],
            corsoScelto: null,
            labelCorsoScelto: null,
            labelDocentiDropDown: [{
                "matricola": 0,
                "nome": "",
                "cognome": "Scegli Docente",
                "stato": true
            }, {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}, {
                "matricola": 0,
                "nome": "",
                "cognome": "Scegli Docente",
                "stato": true
            }, {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}, {
                "matricola": 0,
                "nome": "",
                "cognome": "Scegli Docente",
                "stato": true
            }],
            labelCorsiDropDown: [{"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}, {
                "codice": 0,
                "titolo_corso": "Scegli Corso",
                "stato": true
            }, {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}, {
                "codice": 0,
                "titolo_corso": "Scegli Corso",
                "stato": true
            }, {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}],
            labelOraDropDown: "Scegli Ora",
            prenotazioni: null,
            giornoSelezionato: "Lunedì",
            mostraCards: false,
            temp: " ",
            orariDisp: [],
            orariLun: [],
            orariMar: [],
            orariMer: [],
            orariGio: [],
            orariVen: [],
            flagBottoni: [true, true, true, true, true],
            flagBottoneLun: [true, true, true, true, true],
            flagBottoneMar: [true, true, true, true, true],
            flagBottoneMer: [true, true, true, true, true],
            flagBottoneGio: [true, true, true, true, true],
            flagBottoneVen: [true, true, true, true, true],
            mostraVp: true,
            mostraNp: true,
            mostraCNp: true,
            carrelloRipetizioni: [],
            ripetizioniDaFare: [],
            ripetizioniEffettuate: [],
            ripetizioniCancellate: [],
            MatricolaDocente: 0,
            CodiceUltimoCorso: 0,
            CodiceCorso: 0,
            docentiTot: [],
            corsiTot: []
        }
    },
    methods: {
        transit: function () {
            var self = this;
            self.messaggio = "";
            if (self.InputNomeUtente == null && self.InputPassword == null || self.InputNomeUtente === "" || self.InputPassword === "") {
                self.messaggio = "i campi sono vuoti";
            } else {
                $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletAuth", {login: self.InputNomeUtente, password: self.InputPassword},
                    function (data) {
                        self.utente = data;
                        if (self.utente != null) {
                            if (self.utente.nome_utente == null) {
                                if (self.utente.stato === true) {
                                    self.messaggio = "password errata";
                                } else {
                                    self.messaggio = "utente inesistente";
                                }
                            } else {
                                self.visualizzaPagina("home");
                                self.ruolo = self.utente.ruolo.toLowerCase();
                                self.giornoSelezionato = "Lunedì";
                                self.getPrenotazioni();
                                // self.paginaLogIn = false;
                                // self.paginaHome = true;
                                //self.getInsegnamenti();
                            }
                        } else {
                            self.messaggio = "errore richiesta";
                        }
                    }
                );
            }
        },
        getInsegnamenti: function () {
            var self = this;
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletInsegnamenti", {},
                function (data) {
                    let i;
                    let flag;
                    console.log(data);
                    self.insegnamenti = data;
                    for(i = 0; i < self.insegnamenti.length; i++){
                        // console.log(JSON.stringify(self.insegnamenti[i]));
                        flag = true;
                        for(let j = 1; j < self.docenti.length; j++){
                            if(self.docenti[j].matricola === self.insegnamenti[i].docente.matricola){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            self.docenti.push(self.insegnamenti[i].docente);
                        }
                    }

                    for(i = 0; i < self.insegnamenti.length; i++){
                        flag=true;
                        for(let j = 1; j < self.corsi.length; j++){
                            if(self.corsi[j].codice === self.insegnamenti[i].corso.codice){
                                flag=false;
                                break;
                            }
                        }
                        if(flag){
                            self.corsi.push(self.insegnamenti[i].corso);
                        }
                    }
                    self.docentiL.splice(1, self.docentiL.length);
                    self.corsiL.splice(1, self.corsiL.length);
                    for(i = 1; i < self.docenti.length; i++){
                        self.docentiL.push(self.docenti[i]);
                    }

                    for(i = 1; i < self.corsi.length; i++){
                        self.corsiL.push(self.corsi[i]);
                    }
                }
            );
        },
        myFunction: function (id) {
            document.getElementById(id).classList.toggle("show");
        },
        getElementoSel: function (chiave, id) {
            var self = this;
            console.log(chiave);
            console.log(id);
            console.log(id.split(" ")[0]);
            if(id.split(" ")[0] === "DDC" ){ // Dropdown Docenti content
                for(let i = 0; i < self.docenti.length; i++){
                    if(self.docenti[i].matricola === chiave){
                        document.getElementById(id).classList.toggle("show");
                        self.labelDocentiDropDown[self.hourToIndex(id.split(" ")[1])] = self.docenti[i];
                        self.temp = self.labelDocentiDropDown[self.hourToIndex(id.split(" ")[1])];
                        self.temp = null;
                        break;
                    }
                }
                self.aggiornaCorsi(chiave);
            }else if(id.split(" ")[0] === "DCC"){//Dropdown Corsi content
                console.log("errore");
                for(let i = 0; i < self.corsi.length; i++){
                    if(self.corsi[i].codice === chiave){
                        //self.corsoScelto = self.corsi[i];
                        //console.log(self.corsoScelto.titolo_corso);
                        document.getElementById(id).classList.toggle("show");
                        self.labelCorsiDropDown[self.hourToIndex(id.split(" ")[1])] = self.corsi[i];
                        /*console.log("label 409 " + self.corsi[i].titolo_corso);
                        console.log("label 410" + self.labelCorsiDropDown[self.hourToIndex(id.split(" ")[1])]);*/
                        self.temp = self.labelCorsiDropDown[self.hourToIndex(id.split(" ")[1])];
                        self.temp = null;
                        break;
                    }
                }
                self.aggiornaDocenti(chiave);
            }else if(id.split(" ")[0] === "DocentiDropDown"){
                for(let i = 0; i < self.docenti.length; i++){
                    if(self.docenti[i].matricola === chiave){
                        self.docenteScelto = self.docenti[i];
                        document.getElementById(id).classList.toggle("show");
                        self.labelDocentiDropDown[4] = self.docenteScelto;
                        break;
                    }
                }
                self.aggiornaCorsi(chiave);
            }else if(id.split(" ")[0] === "CorsiDropDown"){
                for(let i = 0; i < self.corsi.length; i++){
                    if(self.corsi[i].codice === chiave){
                        self.corsoScelto = self.corsi[i];
                        console.log(self.corsoScelto.titolo_corso);
                        document.getElementById(id).classList.toggle("show");
                        self.labelCorsiDropDown[4] = self.corsoScelto;
                        break;
                    }
                }
                self.aggiornaDocenti(chiave);
            }
            console.log("stampa corsi");
            for(let i = 0; i < self.labelCorsiDropDown.length; i++){
                console.log(self.labelCorsiDropDown[i]);
            }
            console.log("fine stampa corsi");
            console.log("stampa docenti");
            for(let i = 0; i < self.labelDocentiDropDown.length; i++){
                console.log(self.labelDocentiDropDown[i]);
            }
            console.log("fine stampa docenti");
        },
        aggiornaDocenti: function (codice) {
            var self = this;
            if(self.corsoScelto != null){
                if(self.corsoScelto.codice !== 0){
                    self.docentiL = [{"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true}];
                    for(let i = 0; i < self.insegnamenti.length; i++){
                        if(self.insegnamenti[i].corso.codice === self.corsoScelto.codice){
                            self.docentiL.push(self.insegnamenti[i].docente);
                        }
                    }
                } else {
                    self.corsoScelto = null;
                    for(let i = 0; i < self.docenti.length; i++){
                        let flag = true;
                        for(let j = 0; j < self.docentiL.length; j++){
                            if(self.docentiL[j].matricola === self.docenti[i].matricola){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            self.docentiL.push(self.docenti[i]);
                        }
                    }
                }
            }
            console.log("430"+ self.corsiL[0]);
        },
        aggiornaCorsi : function (matricola){
            var self = this;
            let flag = true;
            if(self.docenteScelto != null){
                if(self.docenteScelto.matricola !== 0){
                    self.corsiL=[{"codice":0,"titolo_corso":"Scegli Corso","stato":true}];
                    for(let i=0;i<self.insegnamenti.length;i++){
                        if(self.insegnamenti[i].docente.matricola === self.docenteScelto.matricola){
                            self.corsiL.push(self.insegnamenti[i].corso);
                        }
                    }
                }else{
                    self.docenteScelto=null;
                    for(let i=0;i<self.corsi.length;i++){
                        flag=true;
                        for(let j = 0; j < self.corsiL.length; j++){
                            if(self.corsiL[j].codice === self.corsi[i].codice){
                                flag=false;
                                break;
                            }
                        }
                        if(flag){
                            self.corsiL.push(self.corsi[i]);
                        }
                    }
                }
            }
        },
        filterFunction: function (idInput, idDD) {
            var input, filter, a, i, div;
            input = document.getElementById(idInput); // "CorsoInput"
            filter = input.value.toUpperCase();
            div = document.getElementById(idDD); //"CorsiDropDown"
            a = div.getElementsByTagName("a");
            for (i = 0; i < a.length; i++) {
                var txtValue = a[i].textContent || a[i].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    a[i].style.display = "";
                } else {
                    a[i].style.display = "none";
                }
            }
        },
        caricaPrenotazioni: function () {
            var self = this;
            for(let i = 0; i < self.labelCorsiDropDown.length - 1; i++){
                self.labelCorsiDropDown[i] = {"codice":0,"titolo_corso":"Scegli Corso","stato":true};
                self.labelDocentiDropDown[i] = {"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true};
            }
            if((self.corsoScelto != null && self.corsoScelto.codice !== 0) || (self.docenteScelto != null && self.docenteScelto.matricola !== 0)) {
                var matricolaDoce = "null";
                var codCorso = "null";
                var usr = "null";

                if(self.docenteScelto !=null ){
                    matricolaDoce = self.docenteScelto.matricola;
                }
                if(self.corsoScelto != null){
                    codCorso = self.corsoScelto.codice;
                }
                if(self.utente != null){
                    usr = self.utente.nome_utente;
                }


                $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletPrenotazioni", {docente: matricolaDoce, corso: codCorso, utente: usr},
                    function (data) {
                        self.orariLun = [];
                        self.orariMar = [];
                        self.orariMer = [];
                        self.orariGio = [];
                        self.orariVen=[];
                        self.prenotazioni = data;
                        for(let i = 0; i < self.prenotazioni.length; i++){
                            for(let j = 0; j < self.prenotazioni[i].length; j++){
                                switch (j) {
                                    case 0:
                                        if( self.prenotazioni[i][j] === 0){
                                            self.orariLun.push(self.indexToHour(i));
                                        }
                                        break;
                                    case 1:
                                        if( self.prenotazioni[i][j] === 0){
                                            self.orariMar.push(self.indexToHour(i));
                                        }
                                        break;
                                    case 2:
                                        if( self.prenotazioni[i][j] === 0){
                                            self.orariMer.push(self.indexToHour(i));
                                        }
                                        break;
                                    case 3:
                                        if( self.prenotazioni[i][j] === 0){
                                            self.orariGio.push(self.indexToHour(i));
                                        }
                                        break;
                                    case 4:
                                        if( self.prenotazioni[i][j] === 0){
                                            self.orariVen.push(self.indexToHour(i));
                                        }
                                        break;
                                }
                            }
                        }
                        self.aggiornaOrariDisp(self.giornoSelezionato);
                        self.labelCorsoScelto = self.corsoScelto;
                        self.labelDocenteScelto = self.docenteScelto;
                    }
                );
            }
        },
        indexToHour: function (indice){
            let orario = "";
            switch(indice){
                case 0:
                    orario = "15:00:00";
                    break;
                case 1:
                    orario =  "16:00:00";
                    break;
                case 2:
                    orario =  "17:00:00";
                    break;
                case 3:
                    orario = "18:00:00";
                    break;
                default:
                    console.log("errore traduzione da indice a ora");
            }
            return orario;
        },
        hourToIndex: function (ora) {
            let index = null;
            console.log(ora);
            switch(ora){
                case "15:00:00":
                    index = 0;
                    break;
                case "16:00:00":
                    index =  1;
                    break;
                case "17:00:00":
                    index = 2;
                    break;
                case "18:00:00":
                    index = 3;
                    break;
                default:
                    console.log("703 " + ora);
                    console.log("errore traduzione da ora a indice");
            }
            return index;
        },
        aggiornaOrariDisp: function (giorno){
            var self = this;
            self.aggiornaFlagBottoni(giorno, null, false);
            for(let i = 0; i < self.labelCorsiDropDown.length - 1; i++){
                self.labelCorsiDropDown[i] = {"codice":0,"titolo_corso":"Scegli Corso","stato":true};
                self.labelDocentiDropDown[i] = {"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true};
            }
            self.giornoSelezionato = giorno;
            switch (giorno) {
                case 'Lunedì':
                    self.orariDisp = self.orariLun;
                    break;
                case 'Martedì':
                    self.orariDisp = self.orariMar;
                    break;
                case 'Mercoledì':
                    self.orariDisp = self.orariMer;
                    break;
                case 'Giovedì':
                    self.orariDisp = self.orariGio;
                    break;
                case 'Venerdì':
                    self.orariDisp = self.orariVen;
                    break;
            }
            self.mostraCards = self.orariDisp.length > 0;
            for(let i = 0; i < self.orariDisp.length; i++){
                console.log(self.orariDisp[i]);
            }
        },
        aggiornaFlagBottoni : function(giorno, ora, visibility){
            var self = this;
            switch (giorno.toLowerCase()) {
                case 'lunedì':
                    if(ora != null)
                        self.flagBottoneLun[self.hourToIndex(ora)] = visibility;
                    if(!visibility)
                        self.flagBottoni = self.flagBottoneLun;
                    break;
                case 'martedì':
                    if(ora != null)
                        self.flagBottoneMar[self.hourToIndex(ora)] = visibility;
                    if(!visibility)
                        self.flagBottoni = self.flagBottoneMar;
                    break;
                case 'mercoledì':
                    if(ora != null)
                        self.flagBottoneMer[self.hourToIndex(ora)] = visibility;
                    if(!visibility)
                        self.flagBottoni = self.flagBottoneMer;
                    break;
                case 'giovedì':
                    if(ora != null)
                        self.flagBottoneGio[self.hourToIndex(ora)] = visibility;
                    if(!visibility)
                        self.flagBottoni = self.flagBottoneGio;
                    break;
                case 'venerdì':
                    if(ora != null)
                        self.flagBottoneVen[self.hourToIndex(ora)] = visibility;
                    if(!visibility)
                        self.flagBottoni = self.flagBottoneVen;
                    break;
                default:
                    console.log("errore in aggiornaFlagBottoni");
            }
        },
        transitCambioPassword: function () {
            var self = this;
            console.log("ciao");
            self.visualizzaPagina("password");
        },
        mostraPassword: function (id){
            var self = this;
            if(id === "vecchiaPassword"){
                self.mostraVp = !self.mostraVp;
            } else if(id === "nuovaPassword"){
                self.mostraNp = !self.mostraNp;
            } else if(id === "CnuovaPassword"){
                self.mostraCNp = !self.mostraCNp;
            }
            document.getElementById(id).setAttribute("type", document.getElementById(id).getAttribute("type") === "password" ? "text" : "password");
        },
        postCambiaPwd: function (idVp, idNp, idCNp){
            var self = this;
            var vp = document.getElementById(idVp).value;
            var np = document.getElementById(idNp).value;
            var cnp = document.getElementById(idCNp).value;
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletImpostazioni", {nomeUtente: self.utente.nome_utente, vecchiaPassword: vp.toString(), nuovaPassword: np.toString(), confermaNuovaPassword: cnp.toString()},
                function (data) {
                    if(data){
                        self.messaggio="Cambio andato a buonfine";
                        self.visualizzaPagina("home");
                    } else {
                        self.messaggio="Errore in cambio password";
                    }
                }
            );
        },
        logOut: function (){
            var self = this;
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletLogOut", {},
                function (data) {
                    if(data){
                        self.orariDisp = [];
                        self.docenteScelto = null;
                        self.corsoScelto = null;
                        self.labelDocentiDropDown = [{"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true}, {"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true}, {"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true}, {"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true}, {"matricola":0,"nome":"","cognome":"Scegli Docente","stato":true}];
                        self.labelCorsiDropDown = [{"codice":0,"titolo_corso":"Scegli Corso","stato":true}, {"codice":0,"titolo_corso":"Scegli Corso","stato":true}, {"codice":0,"titolo_corso":"Scegli Corso","stato":true}, {"codice":0,"titolo_corso":"Scegli Corso","stato":true}, {"codice":0,"titolo_corso":"Scegli Corso","stato":true}];
                        self.labelDocenteScelto = "Scegli Docente";
                        self.labelCorsoScelto = "Scegli Corso";
                        self.visualizzaPagina("home");
                        self.ruolo = "ospite";
                    }
                }
            );
        },
        aggiungiCarrello: function (orario, giorno){
            var self = this;
            console.log("INZIO AGGIUNGI CARRELLO");
            var ripetizione = {"utente": self.utente.nome_utente ,"giorno": giorno, "ora": orario, "docente": {"matricola":0,"nome":"","cognome":"","stato":true}, "corso": {"codice":0,"titolo_corso":"","stato":true}, "stato": true, "effettuata": false};
            if(self.labelDocenteScelto != null && self.labelDocenteScelto.matricola !== 0){
                ripetizione.docente.cognome = self.labelDocenteScelto.cognome;
                ripetizione.docente.nome = self.labelDocenteScelto.nome;
                ripetizione.docente.matricola = self.labelDocenteScelto.matricola;
            } else if(self.labelDocentiDropDown[self.hourToIndex(orario)].matricola !== 0){
                ripetizione.docente.cognome = self.labelDocentiDropDown[self.hourToIndex(orario)].cognome;
                ripetizione.docente.nome = self.labelDocentiDropDown[self.hourToIndex(orario)].nome;
                ripetizione.docente.matricola = self.labelDocentiDropDown[self.hourToIndex(orario)].matricola;
            } else {
                return;
            }

            if(self.labelCorsoScelto != null && self.labelCorsoScelto.matricola !== 0){
                ripetizione.corso.titolo_corso=self.labelCorsoScelto.titolo_corso;
                ripetizione.corso.codice=self.labelCorsoScelto.codice;
            } else if(self.labelCorsiDropDown[self.hourToIndex(orario)].codice !== 0){
                ripetizione.corso.titolo_corso=self.labelCorsiDropDown[self.hourToIndex(orario)].titolo_corso;
                ripetizione.corso.codice=self.labelCorsiDropDown[self.hourToIndex(orario)].codice;
            } else {
                return;
            }
            self.carrelloRipetizioni.push(ripetizione);
            console.log("fine aggiungiCarrello");
            self.aggiornaFlagBottoni(giorno, orario, false);
        },
        rimuoviRipetizione: function (ripetizione){
            var self = this;
            var giorno = ripetizione.giorno;
            var ora = ripetizione.ora;
            console.log("trash");
            console.log(giorno);
            console.log(ora);
            console.log("fine trash");
            self.aggiornaFlagBottoni(giorno, ora, true);
            self.carrelloRipetizioni.splice(self.carrelloRipetizioni.indexOf(ripetizione), 1);
        },
        mostraCarrello: function (){
            var self = this;
            self.carrelloPop = true;
        },
        confermaRipetizioni: function (){
            var self = this;
            var myJson = JSON.stringify(self.carrelloRipetizioni);
            console.log(myJson);
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletInserimentoRipetizioni", {prenotazioni: myJson.toString()},
                function (data) {
                    if(data){
                        self.carrelloRipetizioni=[];
                        self.caricaPrenotazioni();
                        console.log("tutto ok");
                    }
                });
        },
        visualizzaPagina: function (pagina) {
            console.log("pagina: ");
            console.log(pagina);
            var self = this;
            self.paginaHome = false;
            self.paginaLogIn = false;
            self.paginaPrenotazioni = false;
            self.paginaStorico = false;
            self.paginaCambioPwd = false;
            self.paginaModificaDocente = false;
            self.paginaModificaCorso= false;
            self.paginaModificaInsegnamenti = false;
            switch (pagina) {
                case "home": self.paginaHome = true;
                    self.getInsegnamenti();
                    break;
                case "login": self.paginaLogIn = true;
                    break;
                case "prenotazioni": self.paginaPrenotazioni = true;
                    break;
                case "storico": self.paginaStorico = true;
                    break;
                case "password": self.paginaCambioPwd = true;
                    break;
                case "modDocente":
                    self.caricaDocenti();
                    self.paginaModificaDocente = true;
                    break;
                case "modCorso":
                    self.caricaCorsi();
                    self.paginaModificaCorso = true;
                    break;
                case "modInsegnamento":
                    self.caricaCorsi();
                    self.caricaDocenti();
                    self.paginaModificaInsegnamenti = true;
                    break;
            }
            if(self.paginaPrenotazioni || self.paginaStorico){
                self.getPrenotazioni();
            }
        },
        getPrenotazioni: function () {
            var self=this;
            console.log(self.utente.ruolo);
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletRipetizionieff",{utente: self.utente.ruolo === "admin" ? null : self.utente.nome_utente},
                function (data){
                    self.ripetizioniEffettuate=[];
                    self.ripetizioniDaFare=[];
                    self.ripetizioniCancellate=[];
                    // console.log("903 "+data.length);
                    for(let i=0;i<data.length;i++){
                        // console.log("inizio eff");
                        // console.log(data[i].stato);
                        // console.log(data[i].effettuata);
                        // console.log(data[i].stato);
                        if(data[i].stato === true && data[i].effettuata===false){
                            // console.log("dentro")
                            // console.log(data[i]);
                            self.ripetizioniDaFare.push(data[i]);
                        }else if(data[i].stato === false && data[i].effettuata===true){
                            self.ripetizioniEffettuate.push(data[i]);
                        }else if(data[i].stato === false && data[i].effettuata===false){
                            self.ripetizioniCancellate.push(data[i]);
                        }
                    }
                }
            );
        },
        modificaListaPren: function (dest, ripetizione) {
            var self = this;
            var myJson = JSON.stringify(ripetizione);
            console.log(myJson);
            if(dest === "trash"){
                $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRipetizioneCancellata", {prenotazione: myJson.toString()},
                    function (data) {
                        if(data){
                            console.log("cancellazione andata a buon fine");
                        } else {
                            console.log("errore in modificaListaPren, trash");
                        }
                        self.aggiornaFlagBottoni(ripetizione.giorno, ripetizione.ora, true);
                        self.getPrenotazioni();
                    }
                );
            } else if(dest === "check"){
                $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRipetizioniEffettuate", {prenotazione: myJson.toString()},
                    function (data) {
                        if(data){
                            console.log("prenotazione andata a buon fine");
                        } else {
                            console.log("errore in modificaListaPren, check");
                        }
                        self.aggiornaFlagBottoni(ripetizione.giorno, ripetizione.ora, true);
                        self.getPrenotazioni();
                    }
                );
            } else {
                console.log("errore in modificaListaPren, stringa non riconosciuta");
            }
        },
        caricaDocenti : function (){
            var self=this;
            console.log("Sono in carica totali Docenti");
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletCaricaDocentiTotali",{},
                function (data){
                    self.docentiTot=data;
                    self.MatricolaDocente = (self.docentiTot[self.docentiTot.length - 1].matricola + 1);
                }
            );
        },
        caricaCorsi : function (){
            var self=this;
            console.log("Sono in carica totali Corsi");
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletCaricaCorsiTotali",{},
                function (data){
                    self.corsiTot=data;
                    self.CodiceUltimoCorso = (self.corsiTot[self.corsiTot.length - 1].codice + 1);
                }
            );
        },
        rimuoviDocente: function (){
            var self=this;
            console.log("Sono in rimuovi Docenti"+ self.docenteSceltoDaEliminare);
            var myJson = JSON.stringify(self.docenteSceltoDaEliminare);
            console.log("1168"+myJson);
            $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRimuoviDocenti", {docente: myJson.toString()},
                function (data) {
                    if(data){
                        console.log("cancellazione andata a buon fine");
                    } else {
                        console.log("errore in rimuoviDocente, check");
                    }
                    self.caricaDocenti();
                }
            );
        },
        rimuoviCorso: function (){
            var self=this;
            console.log("Sono in rimuovi Corso "+ self.corsoSceltoDaEliminare);
            var myJson = JSON.stringify(self.corsoSceltoDaEliminare);
            console.log("1239"+myJson);
            $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRimuoviCorso", {corso: myJson.toString()},
                function (data) {
                    if(data){
                        console.log("cancellazione andata a buon fine");
                    } else {
                        console.log("errore in rimuoviDocente, check");
                    }
                    self.caricaCorsi();
                }
            );
        },
        rimuoviInsegnamento: function (){
            var self=this;
            console.log("Sono in rimuovi Corso "+ self.insegnamentoSceltoDaEliminare);
            var insegnamentoDaEliminare = {"corso": self.insegnamentoSceltoDaEliminare.corso.codice, "docente": self.insegnamentoSceltoDaEliminare.docente.matricola, "stato": true};
            var myJson = JSON.stringify(insegnamentoDaEliminare);
            console.log("rimuovi insegnamento: " + myJson);
            $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRimuoviInsegnamento", {insegnamento: myJson.toString()},
                function (data) {
                    if(data){
                        console.log("cancellazione andata a buon fine");
                    } else {
                        console.log("errore in rimuoviInsegnamento, check");
                    }
                    self.getInsegnamenti();
                }
            );
        },
        insertDocente: function (){
            var self=this;
            self.NuovoDocente= {"matricola":self.MatricolaDocente,"nome":self.InputNomeDocente,"cognome":self.InputCognomeDocente,"stato":true};
            var myJson = JSON.stringify(self.NuovoDocente);
            console.log("1255"+ myJson.toString());
            $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletInsertDocente", {docente: myJson.toString()},
                function (data) {
                    if(data){
                        console.log("aggiunta andata a buon fine");
                    } else {
                        console.log("errore in insertDocente, check");
                    }
                    self.caricaDocenti();
                }
            );
        },
        insertCorso: function (){
            var self=this;
            self.NuovoCorso = {"codice":self.CodiceUltimoCorso,"titolo_corso":self.InputCodiceCorso,"stato":true};
            var myJ = JSON.stringify(self.NuovoCorso);
            console.log("1168"+myJ);
            $.post("http://localhost:8081/Ripetizioni_war_exploded/ServletInsertCorso", {corso: myJ.toString()},
                function (data) {
                    if(data){
                        console.log("cancellazione andata a buon fine");
                    } else {
                        console.log("errore in insert Corso, check");
                    }
                    self.caricaCorsi();
                }
            );
        },
        FunDocenteSceltoDaEliminare: function(docente){
            var self=this;
            self.docenteSceltoDaEliminare = docente;
        },
        FunCorsoSceltoDaEliminare: function(corso){
            var self=this;
            self.corsoSceltoDaEliminare = corso;
        },
        FunInsegnamentoSceltoDaEliminare: function(insegnamento){
            var self=this;
            console.log(insegnamento.corso.titolo_corso);
            self.insegnamentoSceltoDaEliminare = insegnamento;
            self.corsoSceltoDaEliminare = self.insegnamentoSceltoDaEliminare.corso;
            self.docenteSceltoDaEliminare = self.insegnamentoSceltoDaEliminare.docente;
            console.log(self.insegnamentoSceltoDaEliminare);
            console.log(self.insegnamentoSceltoDaEliminare.corso.titolo_corso);
        },
        FunCorsoSceltoDaAggiungere: function (corso){
            var self=this;
            self.corsoSceltoDaAggiungere = corso;
        },
        FunDocenteSceltoDaAggiungere: function (docente){
            var self=this;
            self.docenteSceltoDaAggiungere = docente;
        },
        insertInsegnamenti: function (){
            var self=this;
            var nuovoInsegnamento = {"corso": self.corsoSceltoDaAggiungere.codice, "docente": self.docenteSceltoDaAggiungere.matricola, "stato": true};
            var myJs = JSON.stringify(nuovoInsegnamento);
            $.get("http://localhost:8081/Ripetizioni_war_exploded/ServletInserimentoInsegnamenti", {insegnamento: myJs.toString()},
                function (data) {
                    if(data){
                        console.log("inserimento andato a buon fine");
                    } else {
                        console.log("errore in insertInsegnamenti");
                    }
                }
            );
        }
    },
    mounted(){
        this.getInsegnamenti();
    },
})

app.use(router);
app.mount("#app");

