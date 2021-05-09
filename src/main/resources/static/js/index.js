const $start = document.querySelector("#start-btn");
const $gameList = document.querySelector("#gameList-btn");
const $url = "http://localhost:8080";

$start.addEventListener("click", startGame);
$gameList.addEventListener("click", roomList);

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

    fetch($url + "/rooms", option)
        .then(data => {
            if (!data.ok) {
                exceptionHandling(data.json())
                throw new Error(data.status);
            }
            return data.json();
        })
        .then(post => {
            location.href = $url + "/rooms" + "/" + post.roomId;
        })
        .catch(error => {
            console.log(error);
        })
}

function roomList(event) {
    location.href = $url + "/rooms";
}

function exceptionHandling(error) {
    error.then(data => {
        console.log(data);
        alert(data.exceptionMessage);
    })
}