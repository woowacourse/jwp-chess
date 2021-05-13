const $url = "http://localhost:8080";
const $gameList = document.querySelector("#gameList");
const $mainBtn = document.querySelector("#main-btn");

window.addEventListener("load", loadGameList);
$gameList.addEventListener("mouseover", onGame);
$gameList.addEventListener("mouseout", outGame);
$mainBtn.addEventListener("click", main);

async function loadGameList() {
    await fetch($url + "/rooms/playing")
        .then(data => {
            if (!data.ok) {
                exceptionHandling(data.json());
                throw new Error(data.status);
            }
            return data.json();
        })
        .then(rooms => {
            gameList(rooms);
        })
        .catch(error => {
            console.log(error);
        })
}

function gameList(rooms) {
    for (const game of rooms) {
        addGame(game);
    }
}

function addGame(room) {
    document.querySelector("#gameList ol").insertAdjacentHTML("beforeend", renderGame(room));
    document.getElementById(room.roomId).addEventListener("click", joinGame);
}

function renderGame(room) {
    return `<li id="${room.roomId}" class="roomInfo">
                <dl>
                    <dt>
                        <span class="roomInfo-roomname">${room.roomName}</span>
                    </dt>
                </dl>
                <dl>
                    <dd>
                        <span class="roomInfo-username">${room.whiteUserName}</span>
                    </dd>
                    <dd>
                        <span id="roomInfo-blackname" class="roomInfo-username">${room.blackUserName}</span>
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

    fetch($url + "/rooms/" + $id + "/join", option)
        .then(data => {
            if (!data.ok) {
                exceptionHandling(data.json())
                throw new Error(data.status);
            }
            return data.json();
        })
        .then(post => {
            location.href = $url + "/rooms/" + post.roomId;
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