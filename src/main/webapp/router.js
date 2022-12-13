import {Login} from "./components/Login.js";
import {Home} from "./components/Home.js";

const routes = [
    { path: '/', component: Home },
    { path: '/login', component: Login },
]

export const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})