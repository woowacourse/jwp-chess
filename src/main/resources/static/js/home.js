const $roomNameInput = document.getElementById("new-room");
$roomNameInput.addEventListener('keyup', makeRoom);

async function makeRoom(event) {
    if (event.key === 'Enter' && event.target.value !== "") {
        const roomName = $roomNameInput.value;
        const roomId = await insertRoom(roomName);
        $roomList.insertAdjacentHTML("beforeend", addRoomButton(roomName))
        location.href = "/rooms/" + roomId;
    }
}

function insertRoom(roomName) {
    return fetch("/rooms", {
        method: 'POST',
        body: roomName
    }).then(res => res.json())
}

function addRoomButton(roomName) {
    return `<li><button>${roomName}</button></li>`;
}

const $roomList = document.getElementById('room-list');
$roomList.addEventListener('click', enter);

async function enter(event) {
    const roomName = event.target.closest('button').innerText;
    const roomId = await findRoom(roomName);
    location.href = "/rooms/" + roomId;
}

function findRoom(roomName) {
    return fetch("/room/" + roomName, {
        method: 'GET'
    }).then(res => res.json())
}

function getRoomList() {
    return fetch("/rooms", {
        method: 'GET',
    }).then(res => res.json())
}

async function renderRoomList() {
    const roomList = await getRoomList();
    const roomNames = roomList["roomNames"];
    roomNames.forEach(element => $roomList.insertAdjacentHTML("beforeend", addRoomButton(element)));
}

renderRoomList();