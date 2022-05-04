let targetRoomId;

window.onload = function () {
    let modalButton = document.getElementById('room-modal-open');
    modalButton.onclick = function () {
        document.getElementById('room-modal').classList.add('show-modal');
    }

    let roomModal = document.getElementById('room-modal');
    Array.from(document.getElementsByClassName('modal-align')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? roomModal.classList.remove('show-modal') : false;
        })
    })

    let passwordModal = document.getElementById('password-modal');
    Array.from(document.getElementsByClassName('modal-align')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? passwordModal.classList.remove('show-modal') : false;
        })
    })

    let roomModalClose = document.getElementById('close-room-modal');
    roomModalClose.onclick = function () {
        roomModal.classList.remove('show-modal');
    }
    let confirmModalClose = document.getElementById('close-confirm-modal');
    confirmModalClose.onclick = function () {
        passwordModal.classList.remove('show-modal');
    };

    fetchRooms();
}

function showErrorMessage(message) {
    document.getElementById('error-message').innerHTML = message;
}

function toRoomPage(id) {
    window.location.href = '/rooms/' + id;
}

function fetchRooms() {
    fetch('http://localhost:8080/rooms', {
        method: 'GET'
    })
        .then(res => res.json())
        .then(res => {
            for (const data of res) {
                let roomWrap = document.createElement('div');
                roomWrap.className = 'room-item-wrap';
                let room = document.createElement('li');
                room.className = 'room-item';
                room.id = 'room-item-' + data.id;
                room.innerHTML = data.name;
                room.addEventListener('click', (e) => {
                    e.target === room ? toRoomPage(data.id) : false;
                })
                let button = document.createElement('button');
                button.className = 'room-delete-button';
                button.id = 'room-delete-' + data.id;
                button.innerHTML = '삭제';
                button.onclick = function () {
                    targetRoomId = data.id;
                    document.getElementById('password-modal').classList.add('show-modal');
                }
                roomWrap.append(room);
                roomWrap.append(button);
                document.getElementById('rooms-list-container').append(roomWrap);
            }
        })
}

function fetchNewRoom() {
    let form = document.getElementById('room-form');
    fetch("http://localhost:8080/rooms" , {
        method: 'POST',
        body: new FormData(form)
    })
        .then(res => res.json())
        .then(res => {
            if (res.message) {
                throw new Error(res.message);
            }
            window.location.href = "/rooms/" + res.id;
        })
        .catch(err => {
            showErrorMessage(err.message);
        })
}

function fetchDeleteRoom() {
    let id = targetRoomId;
    let password = document.getElementById('confirm-password').value;
    fetch("http://localhost:8080/rooms/" + id + '?password=' + password, {
        method: 'DELETE'
    })
        .then(res => res.json())
        .then(res => {
            if (res.message) {
                throw new Error(res.message);
            }
            window.location.reload();
        })
        .catch(err => {
            document.getElementById('confirm-password').value = '';
            alert(err.message);
        })
}
