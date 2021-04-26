import {deleteFetch, getFetch, postFetch} from "./promise/fetches.js"

const $games = document.querySelector('#chess-game-list');
const $generatorButton = document.querySelector("#generator");

$generatorButton.addEventListener("click", askGenerateChessGame);

findGames();

function insertGameList(ele, games) {
    let chessGameLI = document.createElement('li');
    chessGameLI.className = "list-group-item";
    chessGameLI.innerHTML = `
            <div class="view" id = "${ele}">
            <label class = "label">NO.${ele} ${games[ele]} Room</label>
            <button class="destroy"></button>
            </div>`;
    chessGameLI.addEventListener("click", click);
    $games.appendChild(chessGameLI);
}

function click(e) {
    if (e.target.classList.contains("destroy")) {
        askDeleteGame(e.target.parentNode.id);
    } else {
        location.href = "/board?id=" + this.children[0].id;
    }
}

function askDeleteGame(id){
    if(confirm(`NO.${id} 게임 방을 지우시겠습니까?`)){
        deleteFetch(`/room/${id}`);
        location.reload();
    }
}

async function findGames() {
    const responseData = await getFetch("/rooms");
    const games = responseData.runningGames;
    $games.innerHTML = "";
    Object.keys(games)
        .forEach(ele => insertGameList(ele, games));
}

async function askGenerateChessGame() {
    const title = prompt("새로 만들 체스 게임 방 제목을 입력해주세요.");
    if (title) {
        await postFetch("/room", {title: title});
        location.reload();
    }
}







