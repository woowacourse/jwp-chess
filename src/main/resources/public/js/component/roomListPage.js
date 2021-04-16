import { getRooms } from "../service/chessService.js";

export async function renderRoomListPage() {
    const $layoutWrap = document.getElementById("layout-wrap");
    const div = document.createElement("div");
    div.innerHTML = `
<div id="layout">
    <button class="btn">
        체스방 만들기
    </button>
    <div id="room-list">
    </div>
</div>
`
    $layoutWrap.appendChild(div);
    createRoomListElement();
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