export var Login = {
    template: `
        <script src="../app.js"></script>
        <div class="d-flex flex-column min-vh-100 justify-content-center  align-items-center">
            <form method="get">
                  <div class="mb-3">
                        <label for="InputNomeUtente" class="form-label">Nome utente</label>
                        <input v-model="InputNomeUtente" type="text" class="form-control" id="InputNomeUtente"
                               aria-describedby="emailHelp">
                </div>
                <div class="mb-3">
                    <label for="InputPassword" class="form-label">Password</label>
                    <input v-model="InputPassword" type="password" class="form-control" id="InputPassword">
                </div>
                <div class="row justify-content-center">
                    <button v-on:click="transit" type="button" class="btn btn-primary">Log in</button>
                </div>
            </form>
            <p>{{messaggio}}</p>
        </div>
    `
}