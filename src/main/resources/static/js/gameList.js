const $url = "http://localhost:8080";
const $gameList = document.querySelector("#gameList");

window.addEventListener("load", loadGameList);
$gameList.addEventListener("mouseover", onGame);
$gameList.addEventListener("mouseout", outGame);

async function loadGameList() {
    await fetch($url + "/games/playing/true")
        .then(data => {
            if (!data.ok) {
                exceptionHandling(data.json());
                throw new Error(data.status);
            }
            return data.json();
        })
        .then(games => {
            gameList(games);
        })
        .catch(error => {
            console.log(error);
        })
}

function gameList(games) {
    for (const game of games) {
        addGame(game);
    }
}

function addGame(game) {
    document.querySelector("#gameList ol").insertAdjacentHTML("beforeend", renderGame(game));
    document.getElementById(game.id).addEventListener("click", joinGame);
}

function renderGame(game) {
    return `<li id="${game.id}" class="roomInfo">
                <dl>
                    <dt>
                        <span class="roomInfo-roomname">${game.roomName}</span>
                    </dt>
                </dl>
                <dl>
                    <dd>
                        <span class="roomInfo-username">${game.whiteUsername}</span>
                    </dd>
                    <dd>
                        <span id="roomInfo-blackname" class="roomInfo-username">${game.blackUsername}</span>
                    </dd>
                </dl>
            </li>`;
}

function onGame(event) {
    event.target.closest("li").classList.add("onGame");
}

function outGame(event) {
    event.target.closest("li").classList.remove("onGame");
}

function joinGame(event) {
    const $id = event.target.closest("li").id;
    location.href = $url + "/games/" + $id;
}

function exceptionHandling(error) {
    error.then(data => {
        console.log(data.exceptionMessage);
        alert(data.exceptionMessage);
    })
}