const startButton = document.querySelector("#start-button");
const restartButton = document.querySelector("#restart-button");
const chessBoard = document.querySelector("table");
const whiteScore = document.querySelector("#white-score");
const blackScore = document.querySelector("#black-score");
<<<<<<< HEAD
=======
const gameId = document.querySelector("#game-id").innerText;
>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948

startButton.addEventListener("click", onClickStartButton);
restartButton.addEventListener("click", onClickRestartButton);
chessBoard.addEventListener("click", onClickBoard);

async function onClickStartButton () {
<<<<<<< HEAD
    const response = await fetch("/start");
    const data = await response.json();

    if (response.ok) {
=======
    const response = await fetch("/start?gameId=" + gameId);

    if (response.ok) {
        const data = await response.json();
>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948
        loadBoard(data);
        return;
    }

<<<<<<< HEAD
    alert(JSON.stringify(data));
=======
    alert(await response.text());
>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948
}

function loadBoard (data) {
    removeAllPiece();
<<<<<<< HEAD
=======
    console.log(data);
>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948
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
<<<<<<< HEAD
    const response = await fetch("/restart");
=======
    const response = await fetch("/restart?gameId=" + gameId);

debugger;
    if (response.ok === false) {
        alert(response.text());
        return;
    }

>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948
    const data = await response.json();

    if (response.ok) {
        loadBoard(data);
        return;
    }

<<<<<<< HEAD
    alert(JSON.stringify(data));
=======
    alert(data.text());
>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948
}

function onClickBoard ({target: {classList, id, parentNode}}) {
    if (!hasFirstSelected()) {
        classList.toggle("first-selected");
        return;
    }

    if (hasFirstSelected() && !hasSecondSelected()) {
        classList.add("second-selected");
        onClickPiece(id);
        return;
    }
}

function hasFirstSelected () {
    return chessBoard.querySelector(".first-selected") !== null;
}

function hasSecondSelected () {
    return chessBoard.querySelector(".second-selected") !== null;
}

async function onClickPiece (id) {
    const from = chessBoard.querySelector(".first-selected").parentNode.id;
    const to = getSecondSelectedId(id);

    removeSelected();

    const response = await fetch("/move", {
                       method: "put",
                       headers: {"Content-Type": "application/json"},
<<<<<<< HEAD
                       body: JSON.stringify({from: from, to: to})
                     });
    const data = await response.json();
    if (response.ok) {
        movePiece(from, to);
        loadBoard(data);
        return;
    }

    alert(JSON.stringify(data));
=======
                       body: JSON.stringify({from: from, to: to, gameId: gameId})
                     });
    if (response.ok) {
        const data = await response.json();
        movePiece(from, to);
//        loadBoard(data);
        return;
    }

    alert(await response.text());
>>>>>>> d3df5ae88fe4ceab707355d547f08cdd11dfa948
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
