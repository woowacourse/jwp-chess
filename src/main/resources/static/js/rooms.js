import ChessService from "./ChessService.js";

const apiService = new ChessService();

const $moveBtn = document.querySelector(".move_btn");
const $roomUl = document.querySelector(".room_ul");

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
}

function moveToBoard() {
    window.location.href = `/game/1`
}

apiService.showRooms()
    .then(drawRooms)

$moveBtn.addEventListener("click", moveToBoard);