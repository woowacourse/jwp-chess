import {getFetch, postFetch} from "./promise/fetches.js"

const $games = document.querySelector('#chess-game-list');
const $generatorButton = document.querySelector("#generator");

$generatorButton.addEventListener("click", askGenerateChessGame);

findGames();

function insertGameList(ele, games) {
    let chessGameLI = document.createElement('li');
    chessGameLI.className = "list-group-item";
    chessGameLI.innerHTML = `
            <div class="view" id = "${ele}">
            NO.${ele} ${games[ele]} Room
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
    const title = prompt("새로 만들 체스 게임 방 제목을 입력해주세요.");
    if (title) {
        await postFetch("/game/start", {title: title});
        location.reload();
    }
}







