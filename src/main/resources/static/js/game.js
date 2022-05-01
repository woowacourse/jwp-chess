const startButton = document.querySelector("#start-button");
const restartButton = document.querySelector("#restart-button");
const chessBoard = document.querySelector("table");
const whiteScore = document.querySelector("#white-score");
const blackScore = document.querySelector("#black-score");
const gameId = document.querySelector("#game-id").innerText;

startButton.addEventListener("click", onClickStartButton);
restartButton.addEventListener("click", onClickRestartButton);
chessBoard.addEventListener("click", onClickBoard);

async function onClickStartButton () {
    const response = await fetch("/start?gameId=" + gameId);

    if (response.ok) {
        const data = await response.json();
        loadBoard(data);
        return;
    }

    alert(await response.text());
}

function loadBoard (data) {
    removeAllPiece();
    Object.entries(data.positionsAndPieces).forEach(([key, value]) => {
        const block = document.getElementById(key.toLowerCase());
        block.appendChild(createPieceImage(value));
    });
    whiteScore.innerText = data.whiteScore.WHITE;
    blackScore.innerText = data.blackScore.BLACK;
    const result = data.result;
    if (result !== "EMPTY") {
        alert(result);
    }
}

function removeAllPiece () {
    chessBoard.querySelectorAll(".piece").forEach(e => e.remove());
}

function createPieceImage ({color, name}) {
    const image = document.createElement("img");
    image.src = `/images/pieces/${color}/${color}-${name}.svg`;
    image.width = 90;
    image.height = 90;
    image.classList.add("piece");
    return image;
}

async function onClickRestartButton () {
    const response = await fetch("/restart?gameId=" + gameId);

    if (response.ok) {
        const data = await response.json();
        loadBoard(data);
        return;
    }
     alert(await response.text());
}

function onClickBoard ({target: {classList, id, parentNode}}) {
    if (!hasFirstSelected() && isPiece(id)) {
        classList.add("first-selected");
        return;
    }

    if (hasFirstSelected() && !hasSecondSelected()) {
        classList.add("second-selected");
        onClickPiece(id);
    }
}

function hasFirstSelected () {
    return chessBoard.querySelector(".first-selected") !== null;
}

function isPiece(id) {
    if (id === "") {
        return true;
    }
    alert("기물을 선택해 주세요.");
    return false;
}

function hasSecondSelected () {
    return chessBoard.querySelector(".second-selected") !== null;
}

async function onClickPiece (id) {
    const from = chessBoard.querySelector(".first-selected").parentNode.id;
    const to = getSecondSelectedId(id);

    removeSelected();

    const response = await fetch("/move", {
                       method: "PATCH",
                       headers: {"Content-Type": "application/json"},
                       body: JSON.stringify({from: from, to: to, gameId: gameId})
                     });
    if (response.ok) {
        const data = await response.json();
        movePiece(from, to);
        loadBoard(data);
        return;
    }

    alert(await response.text());
}

function getSecondSelectedId (id) {
    if (id === "") {
        return chessBoard.querySelector(".second-selected").parentNode.id;
    }
    return id;
}

function removeSelected () {
    chessBoard.querySelector(".first-selected").classList.remove("first-selected");
    chessBoard.querySelector(".second-selected").classList.remove("second-selected");
}

function movePiece (from, to) {
    const fromPiece = document.getElementById(from).firstElementChild;
    const toPiece = document.getElementById(to).firstElementChild;
    if (toPiece !== null) {
        toPiece.remove();
    }
    fromPiece.remove();
    document.getElementById(to).appendChild(fromPiece);
}
