import ChessService from "./ChessService.js";

const apiService = new ChessService();

const $roomUl = document.querySelector(".room_ul");
const $createRoomBtn = document.querySelector(".create-room-btn");
// modals
const $modal = document.getElementById("myModal");
const $span = document.getElementsByClassName("close")[0];
const $roomName = document.getElementById("name");
const $password = document.getElementById("password");
const $createBtn = document.querySelector(".room-infos-submit");

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

function showModal() {
    $modal.style.display = "block";
    //const roomName = prompt('방 이름을 입력해주세요~ :)', '80글자까지만 입력해주세요~');
    // if (roomName === null) {
    //     return;
    // }
    // apiService.createRoom({roomName})
    //   .then(response => window.location.href = response.headers.get('Location'))
}

apiService.showRooms()
    .then(drawRooms)

$createRoomBtn.addEventListener("click", showModal);

// When the user clicks on <span> (x), close the modal
$span.onclick = function () {
    $modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target === $modal) {
        $modal.style.display = "none";
    }
}

$createBtn.addEventListener("click", function () {
        console.log("hi")
        const roomName = $roomName.value;
        const password = $password.value;
        if (roomName === null) {
             return;
        }
        apiService.createRoom({roomName, password})
           .then(response => window.location.href = response.headers.get('Location'))



    }
)
;