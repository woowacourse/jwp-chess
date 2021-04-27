let loginForm = document.getElementById("loginForm");
let loginBtn = document.getElementById("loginBtn");
let loginSubmit = document.getElementById("loginSubmit");
let loginCloseBtn = document.getElementById("loginCloseBtn");

loginBtn.addEventListener("click", showLoginForm);
loginSubmit.addEventListener("click", login);
loginCloseBtn.addEventListener("click", close);

function showLoginForm() {
    loginForm.style.display = "flex";
}

function login() {
    console.log("login");
}

function close() {
    loginForm.style.display = "none";
}
