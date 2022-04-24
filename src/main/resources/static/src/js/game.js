const startButton = document.querySelector("#startButton");
let gameId = new URLSearchParams(window.location.search).get("id")

let gameOver = "";

let source = "";
let target = "";

window.onload= async function () {
    let board = startGame();
    await initializeBoard(board);
    startButton.textContent = "End";
}

startButton.addEventListener('click', async function () {
    let board = endGame();
    await initializeBoard(board);
    startButton.disabled = true;
})

async function startGame() {
    let savedBoard = await fetch("/api/chess/rooms/" + gameId)
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    savedBoard = await savedBoard.json();
    gameOver = savedBoard.gameOver;
    document.getElementById("roomName").innerText = "방 제목 : " + savedBoard.name
    return savedBoard.board.boards;
}

async function endGame() {
    await getScore()
    let savedBoard = await fetch("/api/chess/rooms/" + gameId,{ method:"PUT"})
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    window.location.href="http://localhost:8080/index.html"
}

async function initializeBoard(board) {
    board.then(res => Object.values(res).forEach(function (value) {
        let eachDiv = document.querySelector("#" + value.position);
        putPiece(eachDiv, res, value.piece);
    }))
}

function putPiece(eachDiv, board, value) {
    if (eachDiv.hasChildNodes()) {
        eachDiv.removeChild(eachDiv.firstChild);
    }
    const img = document.createElement("img");
    img.style.width = '30px';
    img.style.height = '40px';
    img.style.display = 'block';
    img.style.margin = 'auto';
    img.src = "/images/" + value + ".png";
    eachDiv.appendChild(img);
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
    const board = await sendMoveInformation(source, target);
    await updateBoard(board.board.boards);
    await checkGameOver(board.gameOver);
}

async function updateBoard(board) {
    Object.values(board).forEach(function (value) {
        let eachDiv = document.querySelector("#" + value.position);
        putPiece(eachDiv, board, value.piece);
    })
}

async function sendMoveInformation(source, target) {
    const bodyValue = {
        source: source,
        target: target
    }

    let movedBoard = await fetch("/api/chess/rooms/" + gameId + "/move", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    }).then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    movedBoard = await movedBoard.json();
    return movedBoard;
}

async function checkGameOver(gameOverMessage) {
    gameOver = gameOverMessage;
    if (gameOver === "true") {
        alert("게임이 종료되었습니다.");
        let board = endGame();
        await initializeBoard(board);
        startButton.textContent = "Start";
    }
}

async function getScore() {
    if (gameOver === "") {
        alert("게임이 시작되지 않아 선택 불가");
        return;
    }
    let score = await fetch("/api/chess/rooms/" + gameId + "/status")
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    score = await score.json();
    alert("Black 팀 점수 = " + score.blackScore + "\nWhite 팀 점수 = " + score.whiteScore + "\n 현재 승자 = " + score.winningTeam);

}

async function handleErrors(response) {
    if (!response.ok) {
        let message = await response.json();
        console.log(response)
        throw Error(message.errorMessage);
    }
    return response;
}
