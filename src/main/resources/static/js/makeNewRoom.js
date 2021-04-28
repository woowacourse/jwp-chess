import {changeTitleToRoomTitle, initializeChessBoard, saveRoomNumber} from "./firstPage.js";
import {checkFetchLogin} from "./loadRoom.js";

let makeRoomBtn = document.getElementById("makeRoomBtn");
makeRoomBtn.addEventListener("click", makeRoom);

function makeRoom() {
    let roomName = prompt("사용할 체스방의 이름을 입력해주세요");
    console.log(roomName)
    if (roomName === null) {
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
            if (!response.ok) {
                console.log(response.status)
                if (response.status === 401) {
                    alert("로그인을 먼저 해주세요");
                } else if (response.status === 409) {
                    alert("방 제목이 중복됩니다. 다른 방 제목을 사용해주세요.");
                } else {
                    alert("서버와의 통신에 실패했습니다.");
                }
                throw new Error(response.status);
            }
            return response.json();
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
