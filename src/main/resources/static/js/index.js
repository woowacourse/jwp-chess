import {getFetch} from "./promise/fetches.js"

const $startBtn = document.querySelector('#startBtn');
const $games = document.querySelector('#games');

findGames();

$startBtn.addEventListener("click", onClickStartBtn);
$games.addEventListener("click", onClickGameRoom);

async function onClickStartBtn(e) {
    await getFetch("/game/start", 0);
    findGames();
}

function onClickGameRoom(e) {
    if (e.target && e.target.classList.contains('room')) {
        location.href = `/game/${e.target.closest('.text').id}`;
    }
}

async function findGames() {
    const responseData = await getFetch("/room");
    const games = responseData.runningGames;

    $games.innerHTML = "<div class='text rainbow'>{ </div>";
    Object.keys(games).forEach(ele => $games.insertAdjacentHTML("beforeend", `<div id="${ele}" class="text rainbow">&nbsp;&nbsp;<span class="room">{"gameID":"${ele}" , "next_turn":"${games[ele]}"},</span><img class="imgg" src="../img/tenor.gif"/></div>`));
    $games.insertAdjacentHTML("beforeend", "<div class='text rainbow'>} </div>");
}