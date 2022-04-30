const id = new URL(window.location).searchParams.get('id')
let gameOver = "";
let source = "";
let target = "";

async function onloadGameBody() {
    let game = await fetch("/api/chess/rooms/" + id)
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    game = await game.json();
    let boards = game.board.boards;
    document.getElementById("roomName").innerText = game.name;
    gameOver = game.gameOver;
    const endButton = document.querySelector("#endButton");
    endButton.style.display = 'block';
    putPiece(boards);
}

function putPiece(boards) {
    for (let i = 0; i < boards.length; i++) {
        let position = boards[i].position;
        let piece = boards[i].piece;

        let div = document.querySelector("#" + position);
        if (div.hasChildNodes()) {
            div.removeChild(div.firstChild);
        }
        let img = document.createElement("img");
        img.style.width = '30px';
        img.style.height = '40px';
        img.style.display = 'block';
        img.style.margin = 'auto';
        img.src = "/images/" + piece + ".png";
        div.appendChild(img);
    }
}

async function restartGame() {
    let game = await fetch("/api/chess/rooms/" + id + "/restart")
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    game = await game.json();

    putPiece(game.board.boards)
}

async function handleErrors(response) {
    if (!response.ok) {
        let message = await response.json();
        throw Error(message.errorMessage);
    }
    return response;
}

async function initializeBoard(board) {
    board.then(res => Object.values(res).forEach(function (value) {
        let eachDiv = document.querySelector("#" + value.position);
        putPiece(eachDiv, res, value.piece);
    }))
}

function clickMovePosition(e) {
    if (gameOver === "true" || gameOver === "") {
        alert("게임이 시작되지 않아 선택 불가");
        return;
    }
    if (source === "") {
        source = e;
        document.getElementById(source).style.backgroundColor = 'yellow';
        return;
    }

    if (source !== "" && target === "") {
        target = e;
        document.getElementById(source).style.backgroundColor = '';
        movePiece(source, target);
        source = "";
        target = "";
    }
}

async function movePiece(source, target) {
    const game = await sendMoveInformation(source, target);
    putPiece(game.board.boards)
    await checkGameOver(game.gameOver);
}

async function sendMoveInformation(source, target) {
    const bodyValue = {
        source: source, target: target
    }
    let game = await fetch("/api/chess/rooms/" + id + "/move", {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    }).then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    game = await game.json();
    return game;
}

async function checkGameOver(gameOverMessage) {
    if (gameOverMessage === true) {
        alert("게임이 종료되었습니다.");

        const startButton = document.querySelector("#restartButton");
        startButton.style.display = 'block';
        const endButton = document.querySelector("#endButton");
        endButton.style.display = 'none';
    }
}

async function getScore() {
    if (gameOver === "") {
        alert("게임이 시작되지 않아 선택 불가");
        return;
    }
    let score = await fetch("/api/chess/rooms/" + id + "/status")
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    score = await score.json();
    alert("Black 팀 점수 = " + score.blackScore + "\nWhite 팀 점수 = " + score.whiteScore + "\n 현재 승자 = " + score.winningTeam);
}

async function endGame() {
    const bodyValue = {
        password: null
    }
    await fetch("/api/chess/rooms/" + id + "/end", {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        }, body: JSON.stringify(bodyValue)
    }).then(handleErrors)
        .catch(function (error) {
            alert(error.message)
        });

    await getScore();

    const startButton = document.querySelector("#restartButton");
    startButton.style.display = 'block';
    const endButton = document.querySelector("#endButton");
    endButton.style.display = 'none';
}
