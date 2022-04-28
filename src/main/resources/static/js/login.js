window.onload = function () {
    let modalButton = document.getElementById('room-modal-open');
    modalButton.onclick = function () {
        document.getElementById('room-modal').classList.add('show-modal');
    }

    let modal = document.getElementById('room-modal');
    Array.from(document.getElementsByClassName('modal-align')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? modal.classList.remove('show-modal') : false;
        })
    })

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
            window.location.href = res.url;
        })
        .catch(err => {
            showErrorMessage(err.message);
        })
}
