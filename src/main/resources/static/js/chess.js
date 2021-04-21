const roomName = document.getElementById('roomName').value;
applicationStart();

let firstClickPosition = "";
let secondClickPosition = "";



async function applicationStart() {
    const chessBoard = document.querySelector(".chessboard");
    const idArr = ['a8', 'b8', 'c8', 'd8', 'e8', 'f8', 'g8', 'h8',
        'a7', 'b7', 'c7', 'd7', 'e7', 'f7', 'g7', 'h7',
        'a6', 'b6', 'c6', 'd6', 'e6', 'f6', 'g6', 'h6',
        'a5', 'b5', 'c5', 'd5', 'e5', 'f5', 'g5', 'h5',
        'a4', 'b4', 'c4', 'd4', 'e4', 'f4', 'g4', 'h4',
        'a3', 'b3', 'c3', 'd3', 'e3', 'f3', 'g3', 'h3',
        'a2', 'b2', 'c2', 'd2', 'e2', 'f2', 'g2', 'h2',
        'a1', 'b1', 'c1', 'd1', 'e1', 'f1', 'g1', 'h1'];

    let currentColor = "black";

    for (let i = 0; i < 64; i++) {
        let eachDiv = document.createElement("div");
        currentColor = getBoardColor(i, currentColor);
        eachDiv.className = currentColor;
        eachDiv.id = idArr[i];
        eachDiv.innerHTML = "";
        eachDiv.addEventListener('click', clickDiv);
        chessBoard.appendChild(eachDiv);
    }

    let savedBoardInformation = await loadSavedBoard();
    renewBoard(savedBoardInformation);
}

async function loadSavedBoard() {
    let url = "/board/" + roomName;
    let savedBoardInformation = await fetch(url)
    savedBoardInformation = await savedBoardInformation.json();
    return savedBoardInformation.data.boardInfo;
}

async function reStartGame() {
    const boardInfo = await resetBoard();
    renewBoard(boardInfo);
}

function clickDiv(e) {
    if (e.target || e.target.classList.contains("piece")) {
        if (firstClickPosition === "") {
            firstClickPosition = e.target.id;
            document.getElementById(firstClickPosition).style.backgroundColor = 'yellow';
            return;
        }
        if (firstClickPosition !== "" && secondClickPosition === "") {
            secondClickPosition = e.target.id;
            movePiece(firstClickPosition, secondClickPosition);
            document.getElementById(firstClickPosition).style.backgroundColor = '';
            firstClickPosition = "";
            secondClickPosition = "";
            return;
        }
    }
    return e.target;
}

async function movePiece(targetPosition, destinationPosition) {
    const boardInfo = await sendMoveInformation(targetPosition, destinationPosition);
    checkMoveCommandValid(boardInfo);
    checkGameOver(boardInfo.data.gameOverFlag);
    renewBoard(boardInfo.data.boardInfo);
}

function checkGameOver(gameOverFlag) {
    if (gameOverFlag === true) {
        alert("게임이 종료되었습니다. 체스판을 초기화 합니다.");
        reStartGame();
        return '';
    }
}

function checkMoveCommandValid(boardInfo) {
    if(boardInfo.code === "BAD_REQUEST") {
        alert(boardInfo.message);
    }
}

async function sendMoveInformation(targetPosition, destinationPosition) {
    const bodyValue = {
        target: targetPosition,
        destination: destinationPosition
    }

    let url = "/move/" + roomName;
    let boardInformation = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    })
    boardInformation = await boardInformation.json();
    return boardInformation;
}

function getBoardColor(index, color) {
    let resultColor = color;
    let white = "white";
    let black = "black";

    if (index % 8 === 0) {
        return color;
    }
    if (color === white) {
        resultColor = black;
    }
    if (color === black) {
        resultColor = white;
    }
    return resultColor;
}

async function renewBoard(boardInfo) {
    Object.keys(boardInfo).forEach(function (value) {
        let eachDiv = document.querySelector("#" + value);
        eachDiv.innerHTML = boardInfo[value];
    });
}

async function resetBoard() {
    let url = "/board/" + roomName;
    let initialBoardInformation = await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        }
    })
    initialBoardInformation = await initialBoardInformation.json();
    return initialBoardInformation.data.boardInfo;
}

async function alertScore() {
    let url = "/score/" + roomName;
    let scoreInformation = await fetch(url)
    scoreInformation = await scoreInformation.json();
    alert("백: " + scoreInformation.data.whiteScore + " 흑: " + scoreInformation.data.blackScore);
}