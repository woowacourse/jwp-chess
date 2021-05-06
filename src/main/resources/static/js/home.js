const $roomNameInput = document.getElementById("new-room");
$roomNameInput.addEventListener('keyup', makeRoom);

let roomNames;

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
    console.log(roomNames);
    const roomName = event.target.closest('button').innerText;
    const roomId = Object.keys(roomNames).find(key => roomNames[key] === roomName);
    location.href = "/rooms/" + roomId;
}

function findRoom(roomName) {
    return fetch("/rooms/" + roomName, {
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
    roomNames = roomList["roomNames"];
    Object.values(roomNames).forEach(element => $roomList.insertAdjacentHTML("beforeend", addRoomButton(element)));
}

renderRoomList();