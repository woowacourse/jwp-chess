const start = document.getElementById('start-button');
const status = document.getElementById('status-button');
const IMAGE_PATH = "./images/";
const BOARD = document.querySelector("#board");
const CURRENT_TEAM = document.querySelector("#current-team");
const SYMBOL_TO_IMAGE_PATH = {
    "p": IMAGE_PATH + "whitePawn.png",
    "P": IMAGE_PATH + "blackPawn.png",
    "r": IMAGE_PATH + "whiteRook.png",
    "R": IMAGE_PATH + "blackRook.png",
    "n": IMAGE_PATH + "whiteKnight.png",
    "N": IMAGE_PATH + "blackKnight.png",
    "b": IMAGE_PATH + "whiteBishop.png",
    "B": IMAGE_PATH + "blackBishop.png",
    "q": IMAGE_PATH + "whiteQueen.png",
    "Q": IMAGE_PATH + "blackQueen.png",
    "k": IMAGE_PATH + "whiteKing.png",
    "K": IMAGE_PATH + "blackKing.png"
};
const GAME_RUNNING = false;
const GAME_FINISHED = true;

let boardInfo = "";
let isChoiced = false;
let currentTurn = "";
let isFinished = GAME_RUNNING;

function showStatusButton() {
    status.style.visibility = 'visible';
}

function initBoard() {
    fetch('/api/restart')
        .then(res => res.json())
        .then(imageSetting)
}

start.addEventListener('click', function () {
    if (start.textContent === "START") {
        initBoard();
        loadBoard();
        move();
        start.textContent = "RESTART";
        isFinished = GAME_RUNNING;
        return
    }
    window.alert("성공적으로 게임이 재시작되었습니다!")
    initBoard();
})

function getStatus(scoreResponse) {
    const blackScore = scoreResponse["blackTeamScore"];
    const whiteScore = scoreResponse["whiteTeamScore"];

    window.alert("블랙팀 점수 :" + blackScore + ", 하얀팀 점수 : " + whiteScore);
}

status.addEventListener('click', function () {
    fetch('/api/status')
        .then(res => res.json())
        .then(getStatus)
})

function loadBoard() {
    fetch('/api/load')
        .then(res => res.json())
        .then(imageSetting)
}

function imageSetting(response) {
    const divs = BOARD.querySelectorAll("div");
    boardInfo = response;
    pieces = response["board"];
    for (const div of divs) {
        const key = div.getAttribute("id");

        if (SYMBOL_TO_IMAGE_PATH[pieces[key]] !== undefined) {
            div.style.backgroundImage = "url(" + SYMBOL_TO_IMAGE_PATH[pieces[key]] + ")";
        } else {
            div.style.backgroundImage = null;
        }
    }
    turnSetting(response)
}

function turnSetting(response) {
    if (response["finish"] === true) {
        document.querySelector("#view-type").textContent = "승리자 :ㅤ";
        isFinished = GAME_FINISHED;
        alert("게임이 종료되었습니다. 승리자는 : " + CURRENT_TEAM.textContent + "입니다.");
        return;
    }

    document.querySelector("#view-type").textContent = "현재 턴 :ㅤ"
    currentTurn = getTurnByResponse(response);
    CURRENT_TEAM.textContent = currentTurn
}

function getTurnByResponse(response) {
    if (response["turn"] === "white") {
        return "하얀색";
    }
    return "검정색";
}

let toTarget = "";
let fromTarget = "";

function initPosition() {
    fromTarget = "";
    toTarget = "";
}

function eventMove(event) {
    const turn = boardInfo["turn"];
    if (boardInfo["isFinish"] === true) {
        window.alert("게임이 종료되었습니다. RESTART 또는 STATUS만 눌러주세요.");
        return;
    }
    if (isChoiced === false) {
        fromTarget = event.target;
        const position = fromTarget.id

        if (pieces[position] === ".") {
            window.alert("기물을 선택하세요!");
            return
        }
        if (turn === "white" && pieces[position] !== pieces[position].toLowerCase() ||
            turn === "black" && pieces[position] !== pieces[position].toUpperCase()) {
            window.alert("자신의 기물을 선택하세요!");
            return;
        }
        event.target.style.backgroundColor = "skyblue";
        isChoiced = true;
        return;
    }
    if (isChoiced === true) {
        toTarget = event.target
        isChoiced = false;
    }
    if (toTarget.id !== "") {
        fromTarget.style.backgroundColor = "";
        if (fromTarget.id === toTarget.id) {
            initPosition();
            return;
        }
        movePiece(fromTarget.id, toTarget.id)
    }
}

function movePiece(from, to) {
    if (isFinished) {
        alert("게임이 종료되었습니다.");
        return;
    }

    const request = {
        from: from,
        to: to
    }

    fetch('/api/move', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
        .then(res => imageSetting(res));
}

function move() {

    const divs = BOARD.querySelectorAll("div");

    for (const div of divs) {
        div.addEventListener("click", eventMove);
    }
}
