document.getElementById("existingRoom").addEventListener("click", validBlackSubmit);
document.getElementById("start").addEventListener("click", validWhiteSubmit);

function validWhiteSubmit() {
    let form = document.getElementById("whitegame");

    if (form.roomName.value == "" || form.wPassword.value == "") {
        alert("빈칸이 있습니다");
        return false;
    }
}

function validBlackSubmit(event) {
    let form = event.target;

    if (form.bPassword.value == "") {
        alert("빈칸이 있습니다");
        return false;
    }
}