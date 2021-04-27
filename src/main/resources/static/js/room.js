async function showRooms() {
    await axios.get('/rooms')
        .then(response => {
            response.data.roomDtos.forEach(room => showRoom(room));
            addPagination(response.data.pagination);
        }).catch(error => alert(error.response.data));
}

function addPagination(pagination) {
    const $pagination = document.getElementById('pagination');
    for (let i = 1; i <= pagination.pageCounts; i++) {
        const page = document.createElement('div');
        page.className = 'page';
        page.textContent = i;
        $pagination.appendChild(page);
        page.addEventListener('click', (event) => requestChangePage(i));
    }
}

const requestChangePage = async (i) => {
    await axios.get('/rooms?pageIndex=' + i)
        .then(response => {
            Array.from(document.getElementsByClassName('room')).forEach(room => room.remove());
            response.data.roomDtos.forEach(room => showRoom(room));
        }).catch(error => alert(error.response.data));
};

function showRoom(room) {
    const $rooms = document.getElementById('room-list');
    const $roomChild = document.createElement('li');
    $roomChild.className = 'room';
    const $url = document.createElement('a');
    $url.href = '/game/' + room.id;
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
    const $roomAdminPassword = document.getElementById('room-admin-password');
    const requestData = JSON.stringify({"name": $roomName.value, "password": $roomAdminPassword.value});
    await axios.post('/rooms', requestData, {headers: {'Content-Type': 'application/json'}})
        .then(response => {
            showRoom(response.data);
            $roomName.value = '';
            $roomAdminPassword.value = '';
        }).catch(error => alert(error.response.data));
}

axios.default.url = 'http://localhost:8080';
showRooms();
addSubmitButtonEvent();

