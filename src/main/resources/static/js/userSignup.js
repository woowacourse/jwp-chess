let signupForm = document.getElementById("signupForm");
let signupBtn = document.getElementById("signupBtn");
let signupSubmit = document.getElementById("signupSubmit");
let signupCloseBtn = document.getElementById("signupCloseBtn");

let signupID = document.getElementById("signupID");
let signupPW = document.getElementById("signupPW");

signupBtn.addEventListener("click", showSignupForm);
signupSubmit.addEventListener("click", signup);
signupCloseBtn.addEventListener("click", close);

function showSignupForm() {
    signupForm.style.display = "flex";
}

function signup() {
    let id = signupID.value;
    let pw = signupPW.value;

    const signupInfo = {
        id: id,
        password: pw
    }

    const postOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(signupInfo)
    }

    fetch("/signup", postOption)
        .then(response => {
            return checkDuplicate(response);
        })
        .then(data => {
            console.log(data);
            alert("회원 가입에 성공했습니다. 로그인 해주세요.");
            close();
        })
        .catch(error => {
            alert("회원 가입에 실패했습니다.");
        })
}

export function checkDuplicate(response) {
    if (!response.ok) {
        if (response.status === 409) {
            alert("회원 아이디가 중복됩니다. 다른 아이디를 사용해주세요");
        } else {
            alert("회원 가입에 실패했습니다.");
        }
        throw new Error(response.status)
    }
    return response.text();
}

function close() {
    signupForm.style.display = "none";
}
