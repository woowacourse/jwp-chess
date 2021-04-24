import {HTTP_CLIENT, PATH} from "./http.js";

document.addEventListener("DOMContentLoaded", onBuildRoom);
document.getElementById("new-game").addEventListener("click", onNewRoom);
document.querySelector(".continue").addEventListener("click", onEnterRoom);

async function onBuildRoom() {
    const response = await HTTP_CLIENT.get(PATH.ROOM);
    const data = await response.json();
    document.querySelector("tbody").insertAdjacentHTML("beforeend", rooms(data));
}

function rooms(data) {
    let cells = "";
    for (const room of data) {
        cells += `<tr>
                    <td class="room-id">${room.roomId}</td>
                    <td>${room.title}</td>
                    <td><button class="game-button">입장하기</button></td>
                 </tr>`
    }
    return cells;
}

async function onNewRoom() {
    const response = await HTTP_CLIENT.post(PATH.ROOM, prompt("방 제목을 입력해주세요", "EMPTY"));
    moveToChessView(response);
}

async function onEnterRoom(event) {
    const isEnterButton = event.target && event.target.nodeName === "BUTTON";
    if (!isEnterButton) {
        return;
    }

    const roomId = event.target.closest("tr").querySelector(".room-id").textContent;
    const response = await HTTP_CLIENT.get(PATH.ROOM + `/${roomId}/chess`);
    const data = await response.json();
    if (!data.status.includes("RUNNING")) {
        alert("이미 종료된 게임입니다.");
        return;
    }

    moveToChessView(response);
}

const moveToChessView = function (response) {
    console.log(response);
    window.location.href = response.headers.get('location');
}
