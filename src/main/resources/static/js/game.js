import { PATH, HTTP_CLIENT } from "./http.js";

document.getElementById("restart").addEventListener("click", onRestart);

async function onRestart() {
    await HTTP_CLIENT.post(PATH.CHESS);
    window.location.href = '/view';
}
