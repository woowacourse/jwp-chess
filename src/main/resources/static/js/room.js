async function showRooms() {
    await axios.get('/rooms')
        .then(response => response.data.forEach(room => showRoom(room)))
        .catch(error => alert(error.response.data));
}

function showRoom(room) {
    const $rooms = document.getElementById('room-list');
    const $roomChild = document.createElement('li');
    const $url = document.createElement('a');
    $url.href = '/chessgame/' + room.id;
    $url.textContent = room.name;
    $roomChild.appendChild($url);
    $rooms.appendChild($roomChild);
}

function addSubmitButtonEvent() {
    const $submitButton = document.getElementById('submit-button');
    $submitButton.addEventListener('click', requestRoomRegistration);
}

const requestRoomRegistration = async () => {
    const $roomName = document.getElementById('room-name');
    await axios.post('/rooms', $roomName.value, {headers: {'Content-Type': 'application/json'}})
        .then(response => {
            showRoom(response.data);
            $roomName.value = '';
        }).catch(error => alert(error.response.data));
}

axios.default.url = 'http://localhost:8080';
showRooms();
addSubmitButtonEvent();

