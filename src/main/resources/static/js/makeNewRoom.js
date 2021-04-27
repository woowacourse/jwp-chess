import {changeTitleToRoomTitle, initializeChessBoard, saveRoomNumber} from "./firstPage.js";
import {checkFetchLogin} from "./loadRoom.js";

let makeRoomBtn = document.getElementById("makeRoomBtn");
makeRoomBtn.addEventListener("click", makeRoom);

function makeRoom() {
    let roomName = prompt("사용할 체스방의 이름을 입력해주세요");
    console.log(roomName)
    if (roomName === null ) {
        // alert("취소되었습니다");
    } else if (roomName == "") {
        alert("방 제목을 입력해주세요");
    } else {
        checkAvailableRoom(roomName);
    }
}

function checkAvailableRoom(userInputRoomName) {
    const roomInfo = {
        name: userInputRoomName
    };

    const postOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(roomInfo)
    };

    fetch("/games", postOption)
        .then(response => {
            return checkFetchLogin(response);
        })
        .then(data => {
            makeRoomAPIRequest(data);
        })
        .catch(error => {
        })
}

function makeRoomAPIRequest(data) {
    let id = data.roomId;
    let room_name = data.roomName;

    const postOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    let url = "/games/" + String(id);
    console.log(url);
    fetch(url, postOption)
        .then(response => {
            return checkFetchLogin(response);
        })
        .then(data => {
            initializeChessBoard(data);
            changeTitleToRoomTitle(room_name);
            saveRoomNumber(id);
        })
        .catch(error => {
        })
}
