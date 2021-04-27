const $url = "http://localhost:8080";
const $gameList = document.querySelector("#gameList");
const $mainBtn = document.querySelector("#main-btn");

window.addEventListener("load", loadGameList);
$gameList.addEventListener("mouseover", onGame);
$gameList.addEventListener("mouseout", outGame);
$mainBtn.addEventListener("click", main);

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
    const $username = document.querySelector("#username").value;
    const $password = document.querySelector("#password").value;

    const $userInfo = {
        id: $id,
        username: $username,
        password: $password
    }

    const option = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify($userInfo)
    }

    fetch($url + "/games/" + $id + "/join", option)
        .then(data => {
            if (!data.ok) {
                exceptionHandling(data.json())
                throw new Error(data.status);
            }
            return data.json();
        })
        .then(post => {
            location.href = $url + "/games/" + post.gameId;
        })
        .catch(error => {
            console.log(error)
        })
}

function main(event) {
    location.href = $url;
}

function exceptionHandling(error) {
    error.then(data => {
        console.log(data.exceptionMessage);
        alert(data.exceptionMessage);
    })
}