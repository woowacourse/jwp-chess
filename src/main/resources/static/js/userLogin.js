import {logoutBtn} from "./userLogout.js";

let loginForm = document.getElementById("loginForm");
let loginBtn = document.getElementById("loginBtn");
let loginSubmit = document.getElementById("loginSubmit");
let loginCloseBtn = document.getElementById("loginCloseBtn");

let loginID = document.getElementById("loginID");
let loginPW = document.getElementById("loginPW");

let userId = document.getElementById("userId");

loginBtn.addEventListener("click", showLoginForm);
loginSubmit.addEventListener("click", login);
loginCloseBtn.addEventListener("click", close);

function showLoginForm() {
    loginForm.style.display = "flex";
}

function login() {
    let id = loginID.value;
    let pw = loginPW.value;

    const loginInfo = {
        id: id,
        password: pw
    }

    const postOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginInfo)
    }

    fetch("/login", postOption)
        .then(response => {
            if (!response.ok) {
                throw new Error(response.status)
            }
            return response.text();
        })
        .then(data => {
            console.log(data);
            alert("로그인에 성공했습니다. 게임 방을 생성하거나, 조인해주세요.");
            welcomeUser(id);
            close();
            logoutBtn.style.display = "block";
        })
        .catch(error => {
            alert("로그인에 실패했습니다.");
        })
}

function close() {
    loginForm.style.display = "none";
}

export function checkLoginUserWithSession() {
    fetch("/login")
        .then(response => {
            if (!response.ok) {
                throw new Error(response.status)
            }
            console.log(response)
            return response.text();
        })
        .then(data => {
            console.log(data)
            welcomeUser(data);
            logoutBtn.style.display = "block";
        })
        .catch(error => {
        });
}

function welcomeUser(id) {
    userId.innerText = "[Hello! " + String(id) + "]";
}

export function askUserToLogin() {
    userId.innerText = "[Please Login]";
    logoutBtn.style.display = "none";
}