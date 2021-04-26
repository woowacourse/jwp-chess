const $roomNameInput = document.getElementById("new-room");
$roomNameInput.addEventListener('keyup', makeRoom);

// const $startBtn = document.getElementById('start-btn');
// $startBtn.addEventListener('click', makeRoom);

async function makeRoom(event) {
    if (event.key === 'Enter' && event.target.value !== "") {
        const roomName = $roomNameInput.value;
        console.log(await insertRoom(roomName));
        $roomList.insertAdjacentHTML("beforeend", addRoomButton(roomName))
        // location.replace("/rooms/" + roomId)
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

function enter(event) {
    const roomId = event.target.closest('button').innerText;
    location.replace("/rooms/" + roomId)
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