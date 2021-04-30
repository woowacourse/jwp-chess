const startBtn = document.getElementById('start-btn');
const roomsBtn = document.getElementById('rooms-btn');

startBtn.addEventListener('click', addRoom);

async function addRoom() {
    let roomName = prompt("방 이름을 입력해주세요.", "");

    if (roomName.length === 0) {
        alert("방 이름은 1자 이상으로 입력해주세요!");
        return;
    }
    let roomIdDto = await fetch('/api/room', {
        method: 'POST',
        body: JSON.stringify({
            roomName: roomName
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    roomIdDto = await roomIdDto.json();
    window.location.href = "/chess/" + roomIdDto.roomId;
}

roomsBtn.addEventListener('click', function () {
    window.location.href = '/rooms';
});