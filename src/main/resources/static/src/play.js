let gamePassword = new URLSearchParams(window.location.search).get("password");
let gameUri = new URLSearchParams(window.location.search).get("location");

let source = "";
let target = "";

window.onload = async function () {
    await refreshAndDisplayBoard();
}

async function refreshAndDisplayBoard() {
    await refreshBoard().then(displayBoard);
}

async function refreshBoard() {
    const board = document.getElementsByClassName("chess-ui")[0].childNodes;
    board.forEach(await function (value) {
        if (value.hasChildNodes()) {
            value.removeChild(document.getElementById("piece_img"));
        }
    })
}

async function displayBoard() {
    Array.from(await getBoard()).forEach(
        function (element) {
            let position = document.getElementById(element.position);
            if (position.hasChildNodes()) {
                position.removeChild(document.getElementById("piece_img"));
            }
            const imgTeg = document.createElement("img");
            imgTeg.setAttribute("id", "piece_img");
            imgTeg.setAttribute("class", "piece");

            const imgPath = `images/${element.color}_${element.name}.png`;

            imgTeg.setAttribute("src", imgPath);
            document.getElementById(element.position).appendChild(imgTeg);
        }
    )
}

function getBoard() {
    return fetch(gameUri + "?password=" + gamePassword)
        .then(response => handlingException(response))
        .then((response) => response.json())
        .catch(error => {
            alert(error.message);
            location.href = "/";
        });
}

async function startChessGame() {
    await fetch("/chessgames", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    }).then(response => handlingException(response))
        .then(function (response) {
            gameUri = response.headers.get('Location');
        })
        .then(refreshAndDisplayBoard)
        .catch(error => {
            alert(error.message);
        });
}

function checkOnlyOne(element) {
    const checkboxes = document.getElementsByName("promotion");
    checkboxes.forEach((cb) => {
        cb.checked = false;
    })
    element.checked = true;
}

async function promotionButton() {
    const query = 'input[name="promotion"]:checked';
    const selectedEl = document.querySelector(query);
    const promotionPiece = selectedEl.value;
    const promotion = {
        promotionValue: promotionPiece
    }

    await fetch(gameUri + "/promotion", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(promotion)
    }).then(response => handlingException(response))
        .then(refreshAndDisplayBoard)
        .catch(error => {
            alert(error.message);
        });

    const checkboxes = document.getElementsByName("promotion");
    checkboxes.forEach((cb) => {
        cb.checked = false;
    })
}

function move(element) {
    if (source === "") {
        if (element.childNodes.length === 0) {
            return;
        }
        source = element.id;
        document.getElementById(source).style.backgroundColor = 'orange';
        return;
    }

    if (source !== "" && target === "") {
        target = element.id;
        document.getElementById(source).style.backgroundColor = '';
        movePiece(source, target);
        source = "";
        target = "";
    }
}

function movePiece(source, target) {
    if (source === target) {
        return;
    }
    const move = {
        source: source,
        target: target
    }
    fetch(gameUri + "/move", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(move)
    }).then(response => handlingException(response))
        .then(refreshAndDisplayBoard)
        .then(checkEndGame)
        .catch(error => {
            alert(error.message);
        });
}

async function checkEndGame() {
    const gameStatus = await fetch(gameUri + "/status")
        .then((response) => response.json())

    if (gameStatus["end"]) {
        await fetch(gameUri + "/winner")
            .then(response => handlingException(response))
            .then(response => displayWinner(response))
            .catch(error => {
                alert(error.message);
            });
    }
}

async function displayWinner(response) {
    const result = await response.json();
    alert(`우승자는 ${result.winner}입니다.`);
}

async function scoreButton() {
    await fetch(gameUri + "/score")
        .then(handlingException)
        .then((response) => response.json())
        .then(showScore)
        .catch(error => {
            alert(error.message);
        });
}

function showScore(value){
    if (gameUri !== "") {
        alert(`${value[0].color}의 점수는 ${value[0].score}\n${value[1].color}의 점수는 ${value[1].score}`);
    } else {
        alert("게임을 로드하지 않았습니다.");
    }
}

async function endButton() {
    await fetch(gameUri + "/end", {
        method: 'PATCH',
    }).then(handlingException)
        .catch(error => {
            alert(error.message);
        });
}
