const section = document.querySelector("section");
const buttonElement = document.getElementById('btn');

buttonElement.addEventListener('click', function (event) {
    fetch("/game/status")
        .then((res) => res.json())
        .then((json) => {
                let winnerName = document.getElementsByClassName("winner-name")[0];
                let whiteScore = document.getElementsByClassName("white-score")[0];
                let blackScore = document.getElementsByClassName("black-score")[0];

                winnerName.innerText = json.result.winner;
                whiteScore.innerText = json.result.whiteScore;
                blackScore.innerText = json.result.blackScore;
            }
        )
})

let fromInput;
let toInput;
let gameId;

section.addEventListener("mousedown", (event) => {
    saveId();
    fromInput = findTagId(event);
})

section.addEventListener("mouseup", (event) => {

    toInput = findTagId(event);

    fetch("/game/move", {
        method: "post",
        redirect: 'follow',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            source: fromInput,
            target: toInput,
            gameId: gameId
        }),
    })
        .then(status)
        .catch(error => {
            alert(error.message)
        })
})

function saveId() {
    let url = new URL(window.location.href);
    gameId = url.searchParams.get("gameId");

}

function findTagId(event) {
    if (event.target.nodeName === 'IMG') {
        return event.target.parentNode.id;
    }
    return event.target.id;
}

function status(res) {
    if (!res.ok) {
        return res.text().then(text => {
            throw new Error(text)
        })
    }
}
