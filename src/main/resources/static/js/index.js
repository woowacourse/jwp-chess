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

function onClickGameRoom(e) {
    if (e.target && e.target.classList.contains('room')) {
        location.href = `/room/${e.target.closest('.text').id}`;
    }
}

async function findGames() {
    const responseData = await getFetch("/room");
    const games = responseData.activeRooms;

    $games.innerHTML = "<div class='text rainbow'>{ </div>";
    Object.keys(games).forEach(ele => $games.insertAdjacentHTML("beforeend", `<div id="${ele}" class="text rainbow">&nbsp;&nbsp;<span class="room">{"roomId":"${ele}" , "roomName":"${games[ele]}"},</span><img class="imgg" src="../img/tenor.gif"/></div>`));
    $games.insertAdjacentHTML("beforeend", "<div class='text rainbow'>} </div>");
}