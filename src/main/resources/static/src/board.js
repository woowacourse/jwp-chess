const button = document.getElementById("button");
const status_button = document.getElementById("status-button");
const load_button = document.getElementById("load-button");
const squares = document.getElementsByClassName("piece");

source = ""
target = ""

async function startChess() {
    let session = document.getElementById("result-session");
    while (session.hasChildNodes()) {
        session.removeChild(session.firstChild);
    }
    var startUrl = window.location.pathname + "/start";
    let board = await fetch(startUrl, {
        method: "POST",
    });
    board = await board.json();
    putPieceInSquare(board);
}

async function endChess() {
    var endUrl = window.location.pathname + "/end";
    let board = await fetch( endUrl, {
        method: "POST",
    });
    board = await board.json();
    removePieceInSquare(board);
}

async function loadChess() {
    var loadUrl = window.location.pathname + "/load";
    let board = await fetch(loadUrl);
    board = await board.json();
    putPieceInSquare(board);
}

function putPieceInSquare(board) {
    Object.keys(board.values).forEach(function (key) {
        let position = document.getElementById(key);
         if (position.hasChildNodes()) {
              position.removeChild(position.firstChild);
         }
         if (board.values[key] !== "NONE_NONE") {
            const img = document.createElement("img");
            img.src = "images/" + board.values[key] + ".png";
            img.className = "piece-img"
            position.appendChild(img);
         }
    })
}

function removePieceInSquare(board) {
    Object.keys(board.values).forEach(function (key) {
         let position = document.getElementById(key);
         if (position.hasChildNodes()) {
            position.removeChild(position.firstChild);
         }
    })
}

function clickPosition(position) {
    if (source === "") {
        source = position;
        let nowPosition = document.getElementById(source);
        nowPosition.classList.add("selected");
        return;
    }
    if (source !== "" && target === "") {
        target = position;
        let nowPosition = document.getElementById(source);
        nowPosition.classList.remove("selected");
        movePiece(source, target);
        source = "";
        target = "";
    }
}

async function movePiece(source, target) {
    var moveUrl = window.location.pathname + "/move";
    let board = await fetch(moveUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            source: source,
            target: target,
        }),
    })
    board = await board.json();
    putPieceInSquare(board);
}

async function printResult() {
    var statusUrl = window.location.pathname + "/status";
    let board = await fetch(statusUrl);
    board = await board.json();
    let session = document.getElementById("result-session");
    const whiteScore = document.createElement("div");
    const blackScore = document.createElement("div");
    const winner = document.createElement("div");
    whiteScore.innerHTML = "화이트 점수 : " + board.score.WHITE;
    blackScore.innerHTML = "블랙 점수 : " + board.score.BLACK;
    winner.innerHTML = "승자 : " + board.winner;
    session.appendChild(whiteScore);
    session.appendChild(blackScore);
    session.appendChild(winner);
}

async function removeResult() {
    let session = document.getElementById("result-session");
    while(session.hasChildNodes()) {
        session.removeChild(session.firstChild);
    }
}

button.addEventListener("click", function () {
    const form = document.getElementById("form");
    if(button.innerText == "Start") {
        startChess();
        button.innerText = "End";
        return;
    }
    endChess();
    button.innerText = "Start";
});

status_button.addEventListener("click", function () {
    if (status_button.innerText === "Status") {
        status_button.innerText = "Close";
        printResult();
        return;
    }
    status_button.innerText = "Status";
    removeResult();
});

load_button.addEventListener("click", function () {
    loadChess()
});
