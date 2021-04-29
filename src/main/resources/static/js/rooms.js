import ChessService from "./ChessService.js";

const apiService = new ChessService();

const $roomUl = document.querySelector(".room_ul");
const $createRoomBtn = document.querySelector(".create_room_btn");

function drawRooms(datas) {
    console.log(datas)
    let lists = ""
    for (let i in datas) {
        console.log(datas[i])
        lists +=
            `<li class="room_item">
                <div class="room_info" id=${datas[i].roomId}>
                    <strong>Title: ${datas[i].name}</strong> <span>${datas[i].isPlaying ? "진행중" : "종료"}</span>
                </div>
            </li>`
    }
    $roomUl.innerHTML = lists

    $roomUl.addEventListener("click", moveToPreviousBoard);
}

function moveToPreviousBoard(event) {
    const roomId = event.target.id
    console.log(roomId)
    location.href =`/rooms/${roomId}`
}

function moveToBoard() {
    const roomName = prompt('방 이름을 입력해주세요~ :)', 'no name');
    console.log(roomName)
    apiService.createRoom({roomName})
        .then(roomId => location.href = `/rooms/${roomId}`)
}

apiService.showRooms()
    .then(drawRooms)

$createRoomBtn.addEventListener("click", moveToBoard);