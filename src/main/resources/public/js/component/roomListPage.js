import { getRooms } from "../service/chessService.js";
import * as chessPage from "./chessPage.js";
import {store} from "../store.js";

export async function renderRoomListPage() {
    const $layoutWrap = document.getElementById("layout-wrap");
    const div = document.createElement("div");
    div.innerHTML = `
<div id="layout" class="room-list-page">
    <button class="btn" id="createChessRoomBtn">
        체스방 만들기
    </button>
    <div id="room-list">
    </div>
</div>
`
    $layoutWrap.appendChild(div);
    createRoomListElement();
    addEvent();
}

async function createRoomListElement() {
    const res = await getRooms();
    const rooms = res.data.data.rooms;
    const $roomList = document.getElementById("room-list");
    for (let i = 0; i < rooms.length; i++) {
        const $room = document.createElement("div");
        $room.innerHTML = `<button class="btn" id="${rooms[i].roomId}">${rooms[i].roomName}</button>`
        $roomList.appendChild($room);
    }
}

function addEvent() {
    const $roomList = document.getElementById("room-list");
    $roomList.addEventListener("click", enterRoom);

    const $createChessRoomBtn = document.getElementById("createChessRoomBtn");
    $createChessRoomBtn.addEventListener("click", createRoom);
}

function enterRoom(event) {
    const $clickedButton = event.target.closest("button");
    const roomName = $clickedButton.textContent;
    store.roomName = roomName;
    chessPage.createChessBoard(roomName);
}

function createRoom() {
    let roomName;
    do {
        roomName = prompt('입장할 방의 이름을 입력해주세요.');
        store.roomName = roomName;
        chessPage.createChessBoard(roomName);
    } while (!roomName)
}