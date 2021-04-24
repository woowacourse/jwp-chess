export let chessRoomList = document.getElementById("chessRoomList");

export const title = document.getElementById("title");

import { chessBoard, gameResultWindow, initChessBoard } from "./initialize.js";
import { addChessBoardEvent, checkIsPlaying, player1, player2 } from "./movement.js";

loadFirstPage();

function loadFirstPage() {
    chessBoard.style.display = "none";
    gameResultWindow.style.display = "none";
    player1.style.display = "none";
    player2.style.display = "none";
}

export function closeRoomList() {
    chessRoomList.innerHTML = "";
    chessRoomList.style.display = "none";
}

chessRoomList.addEventListener("click", function(e) {
    if (e.target && e.target.nodeName == "BUTTON") {
        let chessRoom = e.target.closest(".chessRoom");
        let roomName = chessRoom.childNodes[0].innerText;
        joinRoomAPIRequest(chessRoom.id, roomName);
    }
})

function joinRoomAPIRequest(id, roomName) {
    fetch("games/" + String(id) + "/saved")
        .then(response => {
            if (!response.ok) {
                console.log(response.status);
                if (response.status === 404) {
                    alert("현재 저장된 게임이 없습니다.");
                } else {
                    alert("서버와의 통신이 실패하였습니다.");
                }
                throw new Error(response.status);
            }
            return response.json();
        })
        .then(data => {
            initializeChessBoard(data);
            checkIsPlaying(data);
            closeRoomList();
            changeTitleToRoomTitle(roomName);
            saveRoomNumber(id);
        })
        .catch(error => {
        })
}

export function initializeChessBoard(data) {
    console.log(data);
    initChessBoard(data);
    addChessBoardEvent();
    makeRoomBtn.style.display = "none";
    joinRoomBtn.style.display = "none";
    chessBoard.style.display = "flex";
    player1.style.display = "flex";
    player2.style.display = "flex";
}

export function changeTitleToRoomTitle(roomName) {
    title.innerText = roomName + " 🎁";
}

export function saveRoomNumber(id) {
    let roomNumber = document.createElement('div');
    roomNumber.setAttribute("id", "roomNumber");
    roomNumber.setAttribute("class", id);
    title.appendChild(roomNumber);
}
