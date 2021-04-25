const startBtn = document.getElementById('start-btn');
const roomListBtn = document.getElementById('room-list-btn');

startBtn.addEventListener('click', addRoom);

async function addRoom() {
    let roomName = prompt("방 이름을 입력해주세요.", "");

    if (roomName.length === 0) {
        alert("방 이름은 1자 이상으로 입력해주세요!");
        return;
    }
    let roomId = await fetch('/room', {
        method: 'POST',
        body: JSON.stringify({
            roomName: roomName
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    roomId = await roomId.json();
    window.location.href = "/chess/" + roomId;
}

roomListBtn.addEventListener('click', function () {
    window.location.href = '/room-list';
});