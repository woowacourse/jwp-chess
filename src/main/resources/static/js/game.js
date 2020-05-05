const createImage = function img_create(src) {
    const img = document.createElement('IMG');
    img.src = src;
    img.classList.add("piece");
    return img;
};
const pieceImgMapper = {
    'P' : createImage("../chess-img/pawn_black.png"),
    'B' : createImage("../chess-img/bishop_black.png"),
    'R' : createImage("../chess-img/rook_black.png"),
    'N' : createImage("../chess-img/knight_black.png"),
    'Q' : createImage("../chess-img/queen_black.png"),
    'K' : createImage("../chess-img/king_black.png"),
    'p' : createImage("../chess-img/pawn_white.png"),
    'b' : createImage("../chess-img/bishop_white.png"),
    'r' : createImage("../chess-img/rook_white.png"),
    'n' : createImage("../chess-img/knight_white.png"),
    'q' : createImage("../chess-img/queen_white.png"),
    'k' : createImage("../chess-img/king_white.png")
};

const endGameButton = document.getElementById("end-game");
const newGameButton = document.getElementById("new-game");

const chessBoard = document.querySelector("#chess-board tbody");

const currentTurn = document.getElementById("current-turn");
const currentState = document.getElementById("current-state");

const gameId = document.getElementById("game-id").innerText;

const colorCell = function colorCell(i, j) {
    return (i + j) % 2 === 0 ? "white-cell" : "black-cell";
};

(function () {
    const MAX_POSITION_NUM = 8;
    const MIN_POSITION_NUM = 1;

    function calculatePositionCoordinate(colIdx, rowIdx) {
        return String.fromCharCode(colIdx + 'a'.charCodeAt(0) - 1) + rowIdx;
    }

    for (let colIdx = MAX_POSITION_NUM; colIdx >= MIN_POSITION_NUM; colIdx--) {
        const tableRow = document.createElement("TR");
        for (let rowIdx = MIN_POSITION_NUM; rowIdx <= MAX_POSITION_NUM; rowIdx++) {
            const tableColumn = document.createElement("TD");
            tableColumn.setAttribute("id", calculatePositionCoordinate(rowIdx, colIdx));
            tableColumn.classList.add("cell-size", "cell", colorCell(colIdx, rowIdx));
            tableRow.appendChild(tableColumn);
        }
        chessBoard.appendChild(tableRow);
    }
}());

const cells = document.querySelectorAll("td.cell");

const toggleCell = function toggleCell(node) {
    if (node.classList.contains("clicked")) {
        node.classList.remove("clicked");
    } else {
        node.classList.add("clicked");
    }
};

const findCountOfToggled = function findCountOfToggled(nodes) {
    return Array.from(nodes).filter(node => node.classList.contains("clicked"))
        .length
};

const removeAllClickedToggle = function removeAllClickedToggle(nodes) {
    nodes.forEach(node => node.classList.remove("clicked"));
};

const renderCurrentGameState = function renderCurrentGameState(currentTurnAndState) {
    const turn = currentTurnAndState["turn"];
    const state = currentTurnAndState["gameState"];
    currentTurn.innerText = turn;
    currentState.innerText = state;
};

const updateGameState = function updateGameState() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            renderCurrentGameState(data['data']);
        }
    };
    xhttp.open("GET", "/games/" + gameId + "/state", true);
    xhttp.send();
};

const requestMovePieces = function requestMovePieces(data) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            updatePieceOnBoard(data['data']);
            requestRecord();
            checkWhetherGameIsFinished();
            updateGameState();
        }
    };
    xhttp.open("PUT", "/games/" + gameId + "/board/pieces", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(data);
};

const checkWhetherGameIsFinished = function checkWhetherGameIsFinished() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            if (!data['data']) {
                requestWinner();
            }
        }
    };
    xhttp.open("GET", "/games/" + gameId + "/non-finish-status", true);
    xhttp.send();
};

const requestEndGame = function requestEndGame() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            updateGameState();
            requestWinner();
        }
    };
    xhttp.open("PUT", "/games/" + gameId + "/state", true);
    xhttp.send("end");
};

const requestWinner = function requestWinner() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            alert("승리 : " + data['data']);
        }
    };
    xhttp.open("GET", "/games/" + gameId + "/winner", true);
    xhttp.send();
};

const requestNewGame = function requestNewGame() {
    cleanBoard();
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            requestAllPieces();
            updateGameState();
        }
    };
    xhttp.open("PUT", "/games/" + gameId + "/state", true);
    xhttp.send("start");
};

const cellClickListener = function cellClickListener() {
    return e => {
        const clickedCell = e.currentTarget;
        toggleCell(clickedCell);
        const count = findCountOfToggled(cells);
        if (count === 0) {
            localStorage.clear();
        } else if (count === 1) {
            localStorage.setItem('from', clickedCell.id);
        } else if (count === 2) {
            const obj = {'from': localStorage.getItem('from'), 'to': clickedCell.id};
            localStorage.clear();
            removeAllClickedToggle(cells);
            requestMovePieces(JSON.stringify(obj));
        }
    };
};

cells.forEach(node => node.addEventListener('click', cellClickListener()));

const cleanBoard = function cleanBoard() {
    cells.forEach(cell => cell.innerHTML = '');
};

const updatePieceOnBoard = function updatePieceOnBoard(datas) {
    moveAllPieces(datas);
};

const moveAllPieces = function moveAllPieces(piecesToUpdate) {
    for (let i = 0; i < piecesToUpdate.length; i++) {
        movePiece(piecesToUpdate[i]);
    }
};

const movePiece = function movePiece(piecesToUpdate) {
    const position = piecesToUpdate["position"];
    const symbol = piecesToUpdate["symbol"];
    const element = document.querySelector("#" + position);
    renderPiece(element, symbol);
};

const renderPiece = function renderPiece(element, symbol) {
    element.innerHTML = '';
    if (symbol === '.') {
        return;
    }
    const mappingImageNode = pieceImgMapper[symbol].cloneNode(true);
    element.appendChild(mappingImageNode);
};

const requestRecord = function requestRecord() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            updateRecord(data['data']);
        }
    };
    xhttp.open("GET", "/games/" + gameId + "/scores", true);
    xhttp.send();
};

const updateRecord = function updateRecord(datas) {
    for (let i = 0; i < datas.length; i++) {
        let select = "";
        if (datas[i]["team"] === "black") {
            select = document.querySelector(".black-team");
        } else {
            select = document.querySelector(".white-team");
        }
        select.innerHTML = datas[i]["score"];
    }
};

const requestAllPieces = function requestAllPieces() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['data']);
                return;
            }
            updatePieceOnBoard(data['data']);
            requestRecord();
        }
    };
    xhttp.open("GET", "/games/" + gameId + "/board/pieces", true);
    xhttp.send();
};

const initialLoad = function initialLoad() {
    updateGameState();
    requestAllPieces();
};

endGameButton.onclick = requestEndGame;
newGameButton.onclick = requestNewGame;
window.onload = initialLoad;
