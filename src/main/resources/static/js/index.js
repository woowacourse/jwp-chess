import {getFetch, postFetch} from "./promise/fetches.js"

const $startBtn = document.querySelector('#startBtn');
const $games = document.querySelector('#games');

findGames();

$startBtn.addEventListener("click", onClickStartBtn);
$games.addEventListener("click", onClickGameRoom);

async function onClickStartBtn(e) {
    const roomName = prompt("방이름 입력해달라");
    isEmpty(roomName);
    const password = prompt("비밀번호를 입력해달라");
    isEmpty(password);

    await postFetch("/room", {roomName: roomName, password: password});
    findGames();
}

function isEmpty(obj) {
    if (!obj) {
        alert("무언가 입력하라");
        throw new Error("유저 입력 비어있음");
    }
}

async function onClickGameRoom(e) {
    if (e.target && e.target.classList.contains('room')) {
        const password = prompt("비밀번호를 입력하라");
        isEmpty(password);
        const roomId = e.target.closest('.text').id;
        console.log(roomId);
        await postFetch(`/room/${roomId}/password`, {roomId: roomId, password: password});
        location.href = `/room/${roomId}`;
    }
}

async function findGames() {
    const responseData = await getFetch("/room");
    const games = responseData.activeRooms;

    $games.innerHTML = "<div class='text rainbow'>{ </div>";
    Object.keys(games).forEach(roomId => $games.insertAdjacentHTML("beforeend", `<div id="${roomId}" class="text rainbow">&nbsp;&nbsp;<span class="room">{"roomId":"${roomId}" , "roomName":"${games[roomId].roomName}, "isFull":"${games[roomId].isFull}"},</span><img class="imgg" src="../img/tenor.gif"/></div>`));
    $games.insertAdjacentHTML("beforeend", "<div class='text rainbow'>} </div>");
}