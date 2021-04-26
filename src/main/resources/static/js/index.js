import {getFetch} from "./promise/fetches.js"

const $games = document.querySelector('#chess-game-list');
const $generatorButton = document.querySelector("#generator");

$generatorButton.addEventListener("click", askGenerateChessGame);

findGames();

function insertGameList(ele, games) {
    let chessGameLI = document.createElement('li');
    chessGameLI.className = "list-group-item";
    chessGameLI.innerHTML = `
            <div class="view" id = "${ele}">
            NO.${ele} Room
            </div>`;
    chessGameLI.addEventListener("click", function () {
        location.href = "/board?id=" + this.children[0].id;
    });
    $games.appendChild(chessGameLI);
}

async function findGames() {
    const responseData = await getFetch("/games");
    const games = responseData.runningGames;
    $games.innerHTML = "";
    Object.keys(games)
        .forEach(ele => insertGameList(ele, games));
}

async function askGenerateChessGame() {
    if (confirm("새로운 체스 게임방을 만드시겠습니까?")) {
        await getFetch("/game/start");
        location.reload();
    }
}







