import ChessService from "./ChessService.js";

const apiService = new ChessService();

const $roomUl = document.querySelector(".room_ul");
const $createRoomBtn = document.querySelector(".create_room_btn");

function drawRooms(datas) {
    let lists = ""
    for (let i in datas) {
        lists +=
            `<li class="room_item" id=${datas[i].roomId}>
                <div class="room_info" >
                    <strong>Title: ${datas[i].name}</strong> <span>${datas[i].isPlaying ? "진행중" : "종료"}</span>
                </div>
            </li>`
    }
    $roomUl.innerHTML = lists

    $roomUl.addEventListener("click", moveToPreviousBoard);
}

function moveToPreviousBoard(event) {
    const roomId = event.target.closest('li').id
    location.href = `/rooms/${roomId}`
}

function moveToBoard() {
    const roomName = prompt('방 이름을 입력해주세요~ :)', '80글자까지만 입력해주세요~');
    if (roomName === null) {
        return;
    }
    apiService.createRoom({roomName})
        .then(response => window.location.href = response.headers.get('Location'))
}

apiService.showRooms()
    .then(drawRooms)

$createRoomBtn.addEventListener("click", moveToBoard);