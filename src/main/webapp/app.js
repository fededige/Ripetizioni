var app = new Vue({
el: '#spa',
data: {
paginaHome: true,
paginaLogIn: false,
paginaPrenotazioni: false,
paginaStorico: false,
paginaCambioPwd: false,
paginaAggiungiDocente: false,
paginaEliminaDocente: false,
paginaAggiungiCorso: false,
paginaEliminaCorso: false,
paginaAggiungiInsegnamenti: false,
paginaEliminaInsegnamenti: false,
registrati: false,
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
docenteSceltoDaEliminare: {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true},
corsoSceltoDaEliminare: {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true},
insegnamentoSceltoDaEliminare: [{
"corso": {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true},
"docente": {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true},
"stato": true
}],
docenteSceltoDaAggiungere: {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true},
corsoSceltoDaAggiungere: {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true},
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
giornoSelezionato: "Scegli Giorno",
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
},
methods: {
transit: function () {
var self = this;
self.messaggio = "";
if (self.InputNomeUtente == null && self.InputPassword == null || self.InputNomeUtente === "" || self.InputPassword === "") {
self.messaggio = "i campi sono vuoti";
} else {
$.get("http://localhost:8081/Ripetizioni_war_exploded/ServletAuth", {
login: self.InputNomeUtente,
password: self.InputPassword
},
function (data) {
console.log("prova");
console.log("prova"+data);
if(data === "UtenteInesistente"){
self.messaggio = "utente inesistente";
} else if(data === "PasswordErrata"){
self.messaggio = "Password errata";
} else if(data != null){
self.utente = {"nome_utente": self.InputNomeUtente, "password": self.InputPassword, "ruolo": data.split(";")[1], "session": data.split(";")[0]};
self.visualizzaPagina("home");
self.ruolo = self.utente.ruolo.toLowerCase();
self.giornoSelezionato = "Lunedì";
self.getPrenotazioni();
} else {
self.messaggio = "errore richiesta";
}
}
);
}
},
getInsegnamenti: function () {
var self = this;
var numero_insegnamenti = 0;
if(self.insegnamenti.length > 0){
numero_insegnamenti = self.insegnamenti.length;
}
$.get("http://localhost:8081/Ripetizioni_war_exploded/ServletInsegnamenti", {id : numero_insegnamenti},
function (data) {
if(data.length > 0) {
self.insegnamenti = data;
let i;
let flag;
for (i = 0; i < self.insegnamenti.length; i++) {
flag = true;
for (let j = 1; j < self.docenti.length; j++) {
if (self.docenti[j].matricola === self.insegnamenti[i].docente.matricola) {
flag = false;
break;
}
}
if (flag) {
self.docenti.push(self.insegnamenti[i].docente);
}
}
for (i = 0; i < self.insegnamenti.length; i++) {
flag = true;
for (let j = 1; j < self.corsi.length; j++) {
if (self.corsi[j].codice === self.insegnamenti[i].corso.codice) {
flag = false;
break;
}
}
if (flag) {
self.corsi.push(self.insegnamenti[i].corso);
}
}
self.docentiL.splice(1, self.docentiL.length);
self.corsiL.splice(1, self.corsiL.length);
for (i = 1; i < self.docenti.length; i++) {
self.docentiL.push(self.docenti[i]);
}

for (i = 1; i < self.corsi.length; i++) {
self.corsiL.push(self.corsi[i]);
}
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
if (id.split(" ")[0] === "DDC") { // Dropdown Docenti content
for (let i = 0; i < self.docenti.length; i++) {
if (self.docenti[i].matricola === chiave) {
//self.docenteScelto = self.docenti[i]; //da commentare
document.getElementById(id).classList.toggle("show");
self.labelDocentiDropDown[self.hourToIndex(id.split(" ")[1])] = self.docenti[i];
/*console.log("label 387 " + self.docenti[i].cognome);
console.log("label 388 " + self.labelDocentiDropDown[self.hourToIndex(id.split(" ")[1])]);*/
self.temp = self.labelDocentiDropDown[self.hourToIndex(id.split(" ")[1])];
self.temp = null;
break;
}
}
self.aggiornaCorsi(chiave);
} else if (id.split(" ")[0] === "DCC") {//Dropdown Corsi content
console.log("errore");
for (let i = 0; i < self.corsi.length; i++) {
if (self.corsi[i].codice === chiave) {
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
} else if (id.split(" ")[0] === "DocentiDropDown") {
for (let i = 0; i < self.docenti.length; i++) {
if (self.docenti[i].matricola === chiave) {
self.docenteScelto = self.docenti[i];
document.getElementById(id).classList.toggle("show");
self.labelDocentiDropDown[4] = self.docenteScelto;
break;
}
}
self.aggiornaCorsi(chiave);
} else if (id.split(" ")[0] === "CorsiDropDown") {
for (let i = 0; i < self.corsi.length; i++) {
if (self.corsi[i].codice === chiave) {
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
for (let i = 0; i < self.labelCorsiDropDown.length; i++) {
console.log(self.labelCorsiDropDown[i]);
}
console.log("fine stampa corsi");
console.log("stampa docenti");
for (let i = 0; i < self.labelDocentiDropDown.length; i++) {
console.log(self.labelDocentiDropDown[i]);
}
console.log("fine stampa docenti");
},
aggiornaDocenti: function (codice) {
var self = this;
if (self.corsoScelto != null) {
if (self.corsoScelto.codice !== 0) {
self.docentiL = [{"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}];
for (let i = 0; i < self.insegnamenti.length; i++) {
if (self.insegnamenti[i].corso.codice === self.corsoScelto.codice) {
self.docentiL.push(self.insegnamenti[i].docente);
}
}
} else {
self.corsoScelto = null;
for (let i = 0; i < self.docenti.length; i++) {
let flag = true;
for (let j = 0; j < self.docentiL.length; j++) {
if (self.docentiL[j].matricola === self.docenti[i].matricola) {
flag = false;
break;
}
}
if (flag) {
self.docentiL.push(self.docenti[i]);
}
}
}
}
console.log("430" + self.corsiL[0]);
},
aggiornaCorsi: function (matricola) {
var self = this;
let flag = true;
if (self.docenteScelto != null) {
if (self.docenteScelto.matricola !== 0) {
self.corsiL = [{"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}];
for (let i = 0; i < self.insegnamenti.length; i++) {
if (self.insegnamenti[i].docente.matricola === self.docenteScelto.matricola) {
self.corsiL.push(self.insegnamenti[i].corso);
}
}
} else {
self.docenteScelto = null;
for (let i = 0; i < self.corsi.length; i++) {
flag = true;
for (let j = 0; j < self.corsiL.length; j++) {
if (self.corsiL[j].codice === self.corsi[i].codice) {
flag = false;
break;
}
}
if (flag) {
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
txtValue = a[i].textContent || a[i].innerText;
if (txtValue.toUpperCase().indexOf(filter) > -1) {
a[i].style.display = "";
} else {
a[i].style.display = "none";
}
}
},
caricaPrenotazioni: function () {
var self = this;
for (let i = 0; i < self.labelCorsiDropDown.length - 1; i++) {
self.labelCorsiDropDown[i] = {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true};
self.labelDocentiDropDown[i] = {
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
};
}
if ((self.corsoScelto != null && self.corsoScelto.codice !== 0) || (self.docenteScelto != null && self.docenteScelto.matricola !== 0)) {
var matricolaDoce = "null";
var codCorso = "null";
var usr = "null";
console.log("utente ospite" + usr);

if (self.docenteScelto != null) {
matricolaDoce = self.docenteScelto.matricola;
}
if (self.corsoScelto != null) {
codCorso = self.corsoScelto.codice;
}
if (self.utente != null) {
usr = self.utente.session;
}
console.log("utente ospite dopo" + usr);


$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletPrenotazioni", {
docente: matricolaDoce,
corso: codCorso,
utente: usr
},
function (data) {
self.orariLun = [];
self.orariMar = [];
self.orariMer = [];
self.orariGio = [];
self.orariVen = [];
self.prenotazioni = data;
for (let i = 0; i < self.prenotazioni.length; i++) {
for (let j = 0; j < self.prenotazioni[i].length; j++) {
switch (j) {
case 0:
if (self.prenotazioni[i][j] === 0) {
self.orariLun.push(self.indexToHour(i));
}
break;
case 1:
if (self.prenotazioni[i][j] === 0) {
self.orariMar.push(self.indexToHour(i));
}
break;
case 2:
if (self.prenotazioni[i][j] === 0) {
self.orariMer.push(self.indexToHour(i));
}
break;
case 3:
if (self.prenotazioni[i][j] === 0) {
self.orariGio.push(self.indexToHour(i));
}
break;
case 4:
if (self.prenotazioni[i][j] === 0) {
self.orariVen.push(self.indexToHour(i));
}
break;
}
}
}
if (self.giornoSelezionato === "Scegli Giorno") {
self.giornoSelezionato = "Lunedì";
}
self.aggiornaOrariDisp(self.giornoSelezionato);
self.labelCorsoScelto = self.corsoScelto;
self.labelDocenteScelto = self.docenteScelto;
}
);
}
},
indexToHour: function (indice) {
let orario = "";
switch (indice) {
case 0:
orario = "15:00:00";
break;
case 1:
orario = "16:00:00";
break;
case 2:
orario = "17:00:00";
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
switch (ora) {
case "15:00:00":
index = 0;
break;
case "16:00:00":
index = 1;
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
console.log(index);
return index;
},
aggiornaOrariDisp: function (giorno) {
var self = this;
self.aggiornaFlagBottoni(giorno, null, false);
for (let i = 0; i < self.labelCorsiDropDown.length - 1; i++) {
self.labelCorsiDropDown[i] = {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true};
self.labelDocentiDropDown[i] = {
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
};
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
for (let i = 0; i < self.orariDisp.length; i++) {
console.log(self.orariDisp[i]);
}
},
aggiornaFlagBottoni: function (giorno, ora, visibility) {
var self = this;
switch (giorno.toLowerCase()) {
case 'lunedì':
if (ora != null)
self.flagBottoneLun[self.hourToIndex(ora)] = visibility;
if (!visibility)
self.flagBottoni = self.flagBottoneLun;
break;
case 'martedì':
if (ora != null)
self.flagBottoneMar[self.hourToIndex(ora)] = visibility;
if (!visibility)
self.flagBottoni = self.flagBottoneMar;
break;
case 'mercoledì':
if (ora != null)
self.flagBottoneMer[self.hourToIndex(ora)] = visibility;
if (!visibility)
self.flagBottoni = self.flagBottoneMer;
break;
case 'giovedì':
if (ora != null)
self.flagBottoneGio[self.hourToIndex(ora)] = visibility;
if (!visibility)
self.flagBottoni = self.flagBottoneGio;
break;
case 'venerdì':
if (ora != null)
self.flagBottoneVen[self.hourToIndex(ora)] = visibility;
if (!visibility)
self.flagBottoni = self.flagBottoneVen;
break;
default:
console.log("errore in aggiornaFlagBottoni");
}
},
transitCambioPassword: function () {
var self = this;
self.visualizzaPagina("password");
},
mostraPassword: function (id) {
var self = this;
if (id === "vecchiaPassword") {
self.mostraVp = !self.mostraVp;
} else if (id === "nuovaPassword" || id === "password" || id === "passwordRegistrazione") {
self.mostraNp = !self.mostraNp;
} else if (id === "CnuovaPassword" || id === "confermaPasswordRegistrazione") {
self.mostraCNp = !self.mostraCNp;
}
document.getElementById(id).setAttribute("type", document.getElementById(id).getAttribute("type") === "password" ? "text" : "password");
},
postCambiaPwd: function (idVp, idNp, idCNp) {
var self = this;
var vp = document.getElementById(idVp).value;
var np = document.getElementById(idNp).value;
var cnp = document.getElementById(idCNp).value;
if(np.toString() !== cnp.toString()){
self.messaggio = "Le password non corrispondono";
return;
}
console.log("session: " + self.utente.session);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletImpostazioni", {
vecchiaPassword: vp.toString(),
nuovaPassword: np.toString(),
session: self.utente.session,
},
function (data) {
if (data) {
self.messaggio = "Cambio andato a buonfine";
self.visualizzaPagina("home");
} else {
self.messaggio = "Errore in cambio password";
}
}
);
},
logOut: function () {
var self = this;
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletLogOut", {session: self.utente.session},
function (data) {
if (data) {
self.orariDisp = [];
self.orariLun = [];
self.orariMar = [];
self.orariMer = [];
self.orariGio = [];
self.orariVen = [];
self.docenteScelto = null;
self.corsoScelto = null;
self.labelDocentiDropDown = [{
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
}, {
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
}, {
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
}, {
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
}, {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true}];
self.labelCorsiDropDown = [{
"codice": 0,
"titolo_corso": "Scegli Corso",
"stato": true
}, {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}, {
"codice": 0,
"titolo_corso": "Scegli Corso",
"stato": true
}, {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true}, {
"codice": 0,
"titolo_corso": "Scegli Corso",
"stato": true
}];
self.labelDocenteScelto = "Scegli Docente";
self.labelCorsoScelto = "Scegli Corso";
self.visualizzaPagina("home");
self.ruolo = "ospite";
self.mostraCards = false;
self.utente = null;
}
}
);
},
aggiungiCarrello: function (orario, giorno) {
var self = this;
console.log("INZIO AGGIUNGI CARRELLO+ " + orario + " " + giorno);
var ripetizione = {
"utente": self.utente.nome_utente,
"giorno": giorno,
"ora": orario,
"docente": {"matricola": 0, "nome": "", "cognome": "", "stato": true},
"corso": {"codice": 0, "titolo_corso": "", "stato": true},
"stato": true,
"effettuata": false
};
if (self.labelDocenteScelto != null && self.labelDocenteScelto.matricola !== 0) {
ripetizione.docente.cognome = self.labelDocenteScelto.cognome;
ripetizione.docente.nome = self.labelDocenteScelto.nome;
ripetizione.docente.matricola = self.labelDocenteScelto.matricola;
} else if (self.labelDocentiDropDown[self.hourToIndex(orario)].matricola !== 0) {
ripetizione.docente.cognome = self.labelDocentiDropDown[self.hourToIndex(orario)].cognome;
ripetizione.docente.nome = self.labelDocentiDropDown[self.hourToIndex(orario)].nome;
ripetizione.docente.matricola = self.labelDocentiDropDown[self.hourToIndex(orario)].matricola;
} else {
return;
}

if (self.labelCorsoScelto != null && self.labelCorsoScelto.matricola !== 0) {
ripetizione.corso.titolo_corso = self.labelCorsoScelto.titolo_corso;
ripetizione.corso.codice = self.labelCorsoScelto.codice;
} else if (self.labelCorsiDropDown[self.hourToIndex(orario)].codice !== 0) {
ripetizione.corso.titolo_corso = self.labelCorsiDropDown[self.hourToIndex(orario)].titolo_corso;
ripetizione.corso.codice = self.labelCorsiDropDown[self.hourToIndex(orario)].codice;
} else {
return;
}
self.carrelloRipetizioni.push(ripetizione);
self.aggiornaFlagBottoni(giorno, orario, false);
},
rimuoviRipetizione: function (ripetizione) {
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
mostraCarrello: function () {
var self = this;
self.carrelloPop = true;
},
confermaRipetizioni: function () {
var self = this;
var myJson = JSON.stringify(self.carrelloRipetizioni);
console.log("carello: " + myJson);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletInserimentoRipetizioni",
{prenotazioni: myJson, session: self.utente.session},
function (data) {
console.log("confermaRipetizioni: " + data);
if(data.length === 0){
self.carrelloRipetizioni = [];
console.log("tutto ok");
self.messaggio = "Prenotazione andata a buon fine";
} else {
self.messaggio = "Le ripetizioni rimaste nel carrello non sono andate a buon fine";
var j = data.length - 1;
for(var i = self.carrelloRipetizioni.length - 1; i >= 0; i--){
if(data[j] !== i){
self.carrelloRipetizioni.splice(i);
} else {
if(j > 0){
j--;
} else {
self.carrelloRipetizioni.splice(0, i);
break;
}
}
}
}
self.caricaPrenotazioni();
});
},
visualizzaPagina: function (pagina) {
var self = this;
self.paginaHome = false;
self.paginaLogIn = false;
self.paginaPrenotazioni = false;
self.paginaStorico = false;
self.paginaCambioPwd = false;
self.paginaAggiungiDocente = false;
self.paginaEliminaDocente = false;
self.paginaAggiungiCorso = false;
self.paginaEliminaCorso = false;
self.paginaAggiungiInsegnamenti = false;
self.paginaEliminaInsegnamenti = false;
self.registrati = false;
self.docenteSceltoDaEliminare = {
"matricola": 0,
"nome": "",
"cognome": "Scegli Docente",
"stato": true
};
self.corsoSceltoDaEliminare = {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true};
self.insegnamentoSceltoDaEliminare = [{
"corso": {
"codice": 0,
"titolo_corso": "Scegli Corso",
"stato": true
},
"docente": {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true},
"stato": true
}];
self.messaggio = "";
switch (pagina) {
case "home":
self.paginaHome = true;
self.getInsegnamenti();
break;
case "login":
self.paginaLogIn = true;
break;
case "prenotazioni":
self.paginaPrenotazioni = true;
break;
case "storico":
self.paginaStorico = true;
break;
case "password":
self.paginaCambioPwd = true;
break;
case "aggiungiDocente":
self.caricaDocenti();
self.paginaAggiungiDocente = true;
break;
case "eliminaDocente":
self.caricaDocenti();
self.paginaEliminaDocente = true;
break;
case "aggiungiCorso":
self.caricaCorsi();
self.paginaAggiungiCorso = true;
break;
case "eliminaCorso":
self.caricaCorsi();
self.paginaEliminaCorso = true;
break;
case "aggiungiInsegnamento":
self.caricaCorsi();
self.caricaDocenti();
self.paginaAggiungiInsegnamenti = true;
break;
case "eliminaInsegnamento":
self.caricaCorsi();
self.caricaDocenti();
self.paginaEliminaInsegnamenti = true;
break;
case "registrati":
self.registrati = true;
}
if (self.paginaPrenotazioni || self.paginaStorico) {
self.getPrenotazioni();
}
},
getPrenotazioni: function () {
var self = this;
var ultimo_id = 0;
if(self.ripetizioniEffettuate.length === 0 && self.ripetizioniDaFare.length === 0 && self.ripetizioniCancellate.length === 0){
ultimo_id = 0;
}else{
var ultimo_codice_ripetizioniEffettuate =0;
if(self.ripetizioniEffettuate.length > 0)
ultimo_codice_ripetizioniEffettuate = self.ripetizioniEffettuate[self.ripetizioniEffettuate.length-1].codice;

var ultimo_codice_ripetizioniDaFare=0;
if(self.ripetizioniDaFare.length > 0)
ultimo_codice_ripetizioniDaFare = self.ripetizioniDaFare[self.ripetizioniDaFare.length-1].codice;

var ultimo_codice_ripetizioniCancellate=0
if(self.ripetizioniCancellate.length>0)
ultimo_codice_ripetizioniCancellate = self.ripetizioniCancellate[self.ripetizioniCancellate.length-1].codice;

ultimo_id = Math.max(ultimo_codice_ripetizioniEffettuate,ultimo_codice_ripetizioniDaFare,ultimo_codice_ripetizioniCancellate);
}
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRipetizionieff",{session: self.utente.session,id : ultimo_id},
function (data) {
for (let i = 0; i < data.length; i++) {
if (data[i].stato === true && data[i].effettuata === false) {
self.ripetizioniDaFare.push(data[i]);
} else if (data[i].stato === false && data[i].effettuata === true) {
self.ripetizioniEffettuate.push(data[i]);
} else if (data[i].stato === false && data[i].effettuata === false) {
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
if (dest === "trash") {
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRipetizioneCancellata", {prenotazione: myJson.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("cancellazione andata a buon fine");
} else {
console.log("errore in modificaListaPren, trash");
}
var indice = self.ripetizioniDaFare.indexOf(ripetizione);
self.ripetizioniDaFare.splice(indice);
self.ripetizioniCancellate.push(ripetizione);
self.aggiornaFlagBottoni(ripetizione.giorno, ripetizione.ora, true);
self.getPrenotazioni();
}
);
} else if (dest === "check") {
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRipetizioniEffettuate", {prenotazione: myJson.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("prenotazione andata a buon fine");
} else {
console.log("errore in modificaListaPren, check");
}
var indice = self.ripetizioniDaFare.indexOf(ripetizione);
self.ripetizioniDaFare.splice(indice);
self.ripetizioniEffettuate.push(ripetizione);
self.aggiornaFlagBottoni(ripetizione.giorno, ripetizione.ora, true);
self.getPrenotazioni();
}
);
} else {
console.log("errore in modificaListaPren, stringa non riconosciuta");
}
},
caricaDocenti: function () {
var self = this;
console.log("Sono in carica totali Docenti");
var ultimo_id=0;
if(self.docentiTot.length === 0)
ultimo_id=0
else
ultimo_id= self.docentiTot[self.docentiTot.length-1].matricola;
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletCaricaDocentiTotali", {session: self.utente.session , id: ultimo_id},
function (data) {
if(self.docentiTot.length === 0){
self.docentiTot = data;
}
else{
for(var i=0;i<data.length;i++){
console.log(data[i]);
self.docentiTot.push(data[i]);
}
}
}
);
},
caricaCorsi: function () {
var self = this;
console.log("Sono in carica totali Corsi");
var ultimo_id=0
if(self.corsiTot.length === 0)
ultimo_id=0
else
ultimo_id= self.corsiTot[self.corsiTot.length-1].codice;
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletCaricaCorsiTotali", {session: self.utente.session,  id: ultimo_id},
function (data) {
console.log("data"+self.corsiTot.length);
if(self.corsiTot.length === 0){
self.corsiTot = data;
}
else{
for(var i=0;i<data.length;i++){
console.log(data[i]);
self.corsiTot.push(data[i]);
}
}
}
);
},
rimuoviDocente: function () {
var self = this;
console.log("Sono in rimuovi Docenti" + self.docenteSceltoDaEliminare);
var myJson = JSON.stringify(self.docenteSceltoDaEliminare);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRimuoviDocenti", {docente: myJson.toString(), session: self.utente.session},
function (data) {
if (data) {
self.messaggio="Docente cancellato correttamente";
var indice = self.docentiTot.indexOf(self.docenteSceltoDaEliminare);
self.docentiTot.splice(indice);
self.rimuoviInsegnamento(self.docenteSceltoDaEliminare,null);
self.caricaDocenti();
self.docenteSceltoDaEliminare= {"matricola": 0, "nome": "", "cognome": "Scegli Docente", "stato": true};

} else {
self.messaggio="Errore!!! Docente non cancellato";
}
}
);
},
rimuoviCorso: function () {
var self = this;
console.log("Sono in rimuovi Corso " + self.corsoSceltoDaEliminare);
var myJson = JSON.stringify(self.corsoSceltoDaEliminare);
console.log("1239" + myJson);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRimuoviCorso", {corso: myJson.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("cancellazione andata a buon fine");
self.messaggio="Corso cancellato correttamente";
var indice = self.corsiTot.indexOf(self.corsoSceltoDaEliminare);
self.corsiTot.splice(indice);
self.rimuoviInsegnamento(null,self.corsoSceltoDaEliminare);
self.caricaCorsi();
self.corsoSceltoDaEliminare= {"codice": 0, "titolo_corso": "Scegli Corso", "stato": true};

} else {
console.log("errore in rimuoviDocente, check");
self.messaggio="Errore!!! Corso non cancellato";
}

}
);
},
rimuoviInsegnamento: function ( d, c) {
var self = this;
var flag = false;
console.log("Sono in rimuovi Insegnamento " + self.insegnamentoSceltoDaEliminare);
var insegnamentoDaEliminare=[];
if(d !== null || c !== null){
flag=true;
if(d !== null){
insegnamentoDaEliminare = {
"corso": -1,
"docente": d.matricola,
"stato" : true
};
self.messaggio="Docente cancellato correttamente"; // scrivo qui perche vengo chiamato da rimuoviDOcente e messaggio si azzera
}else if( c !== null){
insegnamentoDaEliminare = {
"corso": c.codice,
"docente":-1,
"stato" : true
};
self.messaggio="Corso cancellato correttamente";
}
}else{
insegnamentoDaEliminare = {
"corso": self.insegnamentoSceltoDaEliminare.corso.codice,
"docente": self.insegnamentoSceltoDaEliminare.docente.matricola,
"stato": true
};
self.messaggio="PROVA";
}

var myJson = JSON.stringify(insegnamentoDaEliminare);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRimuoviInsegnamento", {insegnamento: myJson.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("cancellazione andata a buon fine");
if(flag === false)
self.messaggio="Insegnamento cancellato correttamente";
} else {
console.log("errore in rimuoviInsegnamento, check");
if(flag == false)
self.messaggio="Errore!! Insegnamento non cancellato";
}
self.getInsegnamenti();
}
);
},
insertDocente: function () {
var self = this;
if (self.InputNomeDocente == null || self.InputCognomeDocente == null) {
self.messaggio="Campi vuoti";
return;
}
self.NuovoDocente = {
"matricola": self.MatricolaDocente,
"nome": self.InputNomeDocente,
"cognome": self.InputCognomeDocente,
"stato": true
};
self.MatricolaDocente = null;
self.InputNomeDocente = null;
self.InputCognomeDocente = null;
var myJson = JSON.stringify(self.NuovoDocente);
console.log("1255" + myJson.toString());
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletInsertDocente", {docente: myJson.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("aggiunta andata a buon fine");
self.messaggio="Docente aggiunto correttamente";
} else {
console.log("errore in insertDocente, check");
self.messaggio="Errore!!! Docente non aggiunto ";
}
self.caricaDocenti();
}
);
},
insertCorso: function () {
var self = this;
if (self.InputCodiceCorso == null) {
return;
}
self.NuovoCorso = {
"codice": self.CodiceUltimoCorso,
"titolo_corso": self.InputCodiceCorso,
"stato": true
};
self.CodiceUltimoCorso = null;
self.InputCodiceCorso = null;
var myJ = JSON.stringify(self.NuovoCorso);
console.log("1168" + myJ);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletInsertCorso", {corso: myJ.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("insermineto andata a buon fine");
self.messaggio="Corso aggiunto correttamente";
} else {
console.log("errore in insert Corso, check");
self.messaggio="Errore!!! Corso non aggiunto";
}
self.caricaCorsi();
}
);
},
FunDocenteSceltoDaEliminare: function (docente, id) {
var self = this;
self.docenteSceltoDaEliminare = docente;
document.getElementById(id).classList.toggle("show");
},
FunCorsoSceltoDaEliminare: function (corso, id) {
var self = this;
self.corsoSceltoDaEliminare = corso;
document.getElementById(id).classList.toggle("show");
},
FunInsegnamentoSceltoDaEliminare: function (insegnamento, id) {
var self = this;
self.insegnamentoSceltoDaEliminare = insegnamento;
self.corsoSceltoDaEliminare = self.insegnamentoSceltoDaEliminare.corso;
self.docenteSceltoDaEliminare = self.insegnamentoSceltoDaEliminare.docente;
document.getElementById(id).classList.toggle("show");
},
FunCorsoSceltoDaAggiungere: function (corso, id) {
var self = this;
self.corsoSceltoDaAggiungere = corso;
document.getElementById(id).classList.toggle("show");
},
FunDocenteSceltoDaAggiungere: function (docente, id) {
var self = this;
self.docenteSceltoDaAggiungere = docente;
document.getElementById(id).classList.toggle("show");
},
insertInsegnamenti: function () {
var self = this;
var nuovoInsegnamento = {
"corso": self.corsoSceltoDaAggiungere.codice,
"docente": self.docenteSceltoDaAggiungere.matricola,
"stato": true
};
var myJs = JSON.stringify(nuovoInsegnamento);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletInserimentoInsegnamenti",
{insegnamento: myJs.toString(), session: self.utente.session},
function (data) {
if (data) {
console.log("inserimento andato a buon fine");
self.messaggio="Inserimento andato a buon fine";
} else {
console.log("errore in insertInsegnamenti");
self.messaggio="Inserimento non andato a buon fine";
}
self.getInsegnamenti();
}
);
},
registraUtente: function (){
var self = this;
var nomeUtente = document.getElementById("InputNomeUtenteRegistazione").value;
var password = document.getElementById("passwordRegistrazione").value;
var confermaPassword = document.getElementById("confermaPasswordRegistrazione").value;
if(password === confermaPassword){
var nuovoUtente = {"nome_utente": nomeUtente, "password": password, "ruolo": "cliente"};
var myJson = JSON.stringify(nuovoUtente);
console.log(myJson);
$.post("http://localhost:8081/Ripetizioni_war_exploded/ServletRegistrazione", {"nuovoUtente": myJson},
function (data) {
self.utente = nuovoUtente;
self.utente.session = data.split(";")[0];
self.visualizzaPagina("home");
self.ruolo = self.utente.ruolo.toLowerCase();
self.giornoSelezionato = "Lunedì";
self.getPrenotazioni();
}
);
}
}
},
mounted() {
this.getInsegnamenti();
},
})