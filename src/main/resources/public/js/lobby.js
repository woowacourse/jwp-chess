const gameHistory = document.getElementById("historyList");
if ($("#history").length === 0)
    gameHistory.innerText = "비어있습니다 - 텅!";

function playNewGame() {
    console.log("여기~");
    const name = getName();
    if (name == null) {
        window.location = "/rooms";
        return;
    }

    const password = getPassword();
    if (password == null) {
        window.location = "/rooms";
        return;
    }

    $.ajax({
        type: "POST",
        url: '/rooms',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            "name": name,
            "password" : password,
        }),
        success: redirect,
        error: showError,
    })
}

function getName () {
    const name = document.getElementById("roomName").value;
    if (name == null) {
        alert("방제는 필수로 입력하셔야 합니다😤");
    }
    return name;
}

function getPassword () {
    const password = prompt("비밀번호를 입력 해 주세요.");
    if (password == null) {
        alert("비밀번호는 필수로 입력하셔야 합니다😤");
    }
    return password;
}

function showError (response) {
    alert(response);
}

function redirect (response) {
    window.location = `/rooms/${response}`;
}