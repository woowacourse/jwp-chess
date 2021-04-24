const $startBtn = document.getElementById('start-btn');
$startBtn.addEventListener('click', makeRoom);

async function makeRoom() {
    const roomId = await getRoomNumber();

    $roomList.insertAdjacentHTML("beforeend", addRoomButton(roomId))
    location.replace("/rooms/" + roomId)
}

function getRoomNumber() {
    return fetch("/makeRoom", {
        method: 'GET',
    }).then(res => res.json())
}

function addRoomButton(roomId) {
    return `<li><button>${roomId}</button></li>`;
}

const $roomList = document.getElementById('room-list');
$roomList.addEventListener('click', enter);

function enter(event) {
    const roomId = event.target.closest('button').innerText;
    location.replace("/rooms/" + roomId)
}

function getRoomList() {

}