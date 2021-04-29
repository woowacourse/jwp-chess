import ChessService from "./ChessService.js";

const apiService = new ChessService();

const $moveBtn = document.querySelector(".move_btn");
const $roomUl = document.querySelector(".room_ul");
const $createRoomBtn = document.querySelector(".create_room_btn");

function drawRooms(datas) {
    console.log(datas)
    let lists = ""
    for (let i in datas) {
        console.log(datas[i])
        lists +=
            `<li>
                <div class="room_info" id=${datas[i].roomId}>
                    Title: ${datas[i].name}, 진행상태: ${datas[i].isPlaying ? "진행중" : "종료"}
                </div>
            </li>`
    }
    $roomUl.innerHTML = lists

    $roomUl.addEventListener("click", moveToPreviousBoard);
}

function moveToPreviousBoard(event) {
    const roomId = event.target.id
    console.log(roomId)
    //todo: 방 클릭시 기존 게임으로 이동
    // window.location.href = `/game/${roomId}`
}

function moveToBoard() {
    const roomName = prompt('방 이름을 입력해주세요~ :)', 'no name');
    console.log(roomName)
    apiService.createRoom({roomName})
        .then(roomId => {
                console.log("이동하고싶어요", roomId);
                location.href = `/rooms/${roomId}`
            }
        )
}

apiService.showRooms()
    .then(drawRooms)

$createRoomBtn.addEventListener("click", moveToBoard);