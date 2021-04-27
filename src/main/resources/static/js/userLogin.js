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
            return response.body;
        })
        .then(data => {
            console.log(data);
            alert("로그인에 성공했습니다. 게임 방을 생성하거나, 조인해주세요.");
            welcomeUser(id);
            close();
        })
        .catch(error => {
            alert("로그인에 실패했습니다.");
        })
}

function close() {
    loginForm.style.display = "none";
}

function welcomeUser(id) {
    userId.innerText = "[Hello! " + String(id) + "]";
}