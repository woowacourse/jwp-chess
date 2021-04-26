let rooms = document.querySelectorAll('.room-no');
for (let i = 0; i < rooms.length; ++i) {
    rooms[i].innerHTML = i + 1;
}

let users = document.querySelectorAll('.user-no');
for (let i = 0; i < users.length; ++i) {
    users[i].innerHTML = i + 1;
}

const chessAPI = "/api/v1/chess";
const chessURL = "/chess";

function createRoom() {
    let name = prompt("방 이름을 입력해주세요.");
    let password = prompt("플레이어 1의 비밀번호를 입력해주세요.");
    if (name !== "") {
        newGame(name);
    } else {
        alert("방 이름은 한 글자 이상이어야합니다.");
    }
}

function newGame(name) {
    let data = {
        "name": name
    }

    $.ajax({
        url: chessAPI + "/new-game",
        data: JSON.stringify(data),
        method: "POST",
        contentType: "application/json",
        dataType: "json"
    }).done(function (success) {
        if (success) {
            location.reload();
        }
    }).error(function (response) {
        const errorMessage = response.responseText;
        location.href = chessURL + "/error-page?error=" + errorMessage;
    });
}

function showNewGamePopup() {
    const popup = document.querySelector('.new-game');
    popup.classList.remove('hide');
}

function closeNewGamePopup() {
    const popup = document.querySelector('.new-game');
    popup.classList.add('hide');

    const roomName = popup.querySelector('#room-name');
    const userId = popup.querySelector('#user-id');
    const userPassword = popup.querySelector('#user-password');
    roomName.value = "";
    userId.value = "";
    userPassword.value = "";
}

function showEnterBlackPopup() {
    const popup = document.querySelector('.enter-black');
    popup.classList.remove('hide');
}

function closeEnterBlackPopup() {
    const popup = document.querySelector('.enter-black');
    popup.classList.add('hide');

    const userId = popup.querySelector('#user-id');
    const userPassword = popup.querySelector('#user-password');
    userId.value = "";
    userPassword.value = "";
}

function showEnterWhitePopup() {
    const popup = document.querySelector('.enter-white');
    popup.classList.remove('hide');
}

function closeEnterWhitePopup() {
    const popup = document.querySelector('.enter-white');
    popup.classList.add('hide');

    const userId = popup.querySelector('#user-id');
    const userPassword = popup.querySelector('#user-password');
    userId.value = "";
    userPassword.value = "";
}