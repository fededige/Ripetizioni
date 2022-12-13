export var Home = {
    template: `
        <div class="row justify-content-end" v-if="ruolo == 'ospite'">
            <div class="d-grid gap-2 d-md-block mx-4">
                <button class="btn btn-secondary" type="button" v-on:click="visualizzaPagina('login')">
                    Login
                </button>
            </div>
        </div>
        <div class="row justify-content-end" v-else>
            <div class="dropdown dimDrop" id="DropDownUser">
                <button v-on:click="myFunction('LoginDropDown')" class="dropbtn">
                    NOME UTENTE
                    <i class="fa fa-angle-down"></i>
                </button>
                <div id="LoginDropDown" class="dropdown-content pt-2">
                    <p v-on:click="transitCambioPassword" class="ml-2">Cambia Password</p>
                    <p v-on:click="logOut" class="ml-2">Logout</p>
                </div>
            </div>
        </div>
        <div class="event-schedule-area-two bg-color pad100">
            <div class="row bg-light">
                <nav class="navbar navbar-expand-lg navbar-light bg-light">

                    <button class="navbar-toggler" type="button" data-toggle="collapse"
                            data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                        <div class="navbar-nav sticky-bottom sticky-bottom bg-light">
                            <div class="container-fluid">
                                <a class="nav-item nav-link active show" aria-selected="true"
                                   v-on:click="visualizzaPagina('home')">Home <span class="sr-only">(current)</span></a>
                                <a class="nav-item nav-link" aria-selected="false" v-if="ruolo != 'ospite'"
                                   v-on:click="visualizzaPagina('prenotazioni')">Prenotazioni</a>
                                <a class="nav-item nav-link" aria-selected="false" v-if="ruolo != 'ospite'"
                                   v-on:click="visualizzaPagina('storico')">Storico</a>
                                <a class="nav-item nav-link" aria-selected="false" v-if="ruolo == 'admin'"
                                   v-on:click="visualizzaPagina('modDocente')">Modifica Docente</a>
                                <a class="nav-item nav-link" aria-selected="false" v-if="ruolo == 'admin'"
                                   v-on:click="visualizzaPagina('modCorso')">Modifica Corso</a>
                                <a class="nav-item nav-link" aria-selected="false" v-if="ruolo == 'admin'"
                                   v-on:click="visualizzaPagina('modInsegnamento')">Modifica Insegnamento</a>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
    `
}