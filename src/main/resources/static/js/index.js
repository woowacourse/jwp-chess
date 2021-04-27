const $start = document.querySelector("#start-btn");
const $gameList = document.querySelector("#gameList-btn");
const $url = "http://localhost:8080/games";

$start.addEventListener("click", startGame);
$gameList.addEventListener("click", gameList);

function startGame(event) {
    const $roomName = document.querySelector("#roomName").value;
    const $whiteUsername = document.querySelector("#white-username").value;
    const $whitePassword = document.querySelector("#white-password").value;

    const room = {
        roomName: $roomName,
        whiteUsername: $whiteUsername,
        whitePassword: $whitePassword
    }

    const option = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(room)
    }

    fetch($url, option)
        .then(data => {
            if (!data.ok) {
                exceptionHandling(data.json())
                throw new Error(data.status);
            }
            return data.json();
        })
        .then(post => {
            location.href = $url + "/" + post.gameId;
        })
        .catch(error => {
            console.log(error);
        })
}

function gameList(event) {
    location.href = $url;
}

function exceptionHandling(error) {
    error.then(data => {
        console.log(data);
        alert(data.exceptionMessage);
    })
}