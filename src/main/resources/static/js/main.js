let rooms = document.querySelectorAll('.room-no');
for (let i = 0; i < rooms.length; ++i) {
    rooms[i].innerHTML = i + 1;
}

let users = document.querySelectorAll('.user-no');
for (let i = 0; i < users.length; ++i) {
    users[i].innerHTML = i + 1;
}

function showPopup() {
    const popup = document.querySelector('#popup');
    popup.classList.remove('hide');
}

function closePopup() {
    const roomName = document.querySelector('#room-name');
    const userId = document.querySelector('#user-id');
    const userPassword = document.querySelector('#user-password');
    roomName.value = "";
    userId.value = "";
    userPassword.value = "";
    const popup = document.querySelector('#popup');
    popup.classList.add('hide');
}

function showEnterPopup() {
    const popup = document.querySelector('#enter-popup');
    popup.classList.remove('hide');
}

function closeEnterPopup() {
    const userId = document.querySelector('#black-user-id');
    const userPassword = document.querySelector('#black-user-password');
    userId.value = "";
    userPassword.value = "";
    const popup = document.querySelector('#enter-popup');
    popup.classList.add('hide');
}
