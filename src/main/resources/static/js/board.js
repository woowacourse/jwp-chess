import { COOKIE, HTTP_CLIENT, PATH } from "./http.js";
import PIECES from "./piece.js";

const FILES = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
const WHITE_SQUARE = "white-square";
const BLACK_SQUARE = "black-square";

document.addEventListener("DOMContentLoaded", buildBoard);

async function buildBoard() {
    let $board = document.getElementById("board");
    if ($board == null) {
        document.querySelector("body").insertAdjacentHTML("afterbegin", build());
        $board = document.getElementById("board");
    }
    $board.addEventListener("click", onMove);
    $board.addEventListener("mouseover", onMouseOverSquare);
    $board.addEventListener("mouseout", onRevertSquareColor);

    const data = await showChessInfo();
    const boardDto = data.boardDto;
    console.log(boardDto);
    inputImageAtBoard(boardDto.pieceDtos);
    $blackScore.textContent = boardDto.blackScore;
    $whiteScore.textContent = boardDto.whiteScore;
    borderCurrentTurn(data.turn);
}

function borderCurrentTurn(turn) {
    if (turn === 'BLACK') {
        document.getElementById("black-versus").classList.add("currentTurn");
        document.getElementById("white-versus").classList.remove("currentTurn");
        return;
    }

    document.getElementById("white-versus").classList.add("currentTurn");
    document.getElementById("black-versus").classList.remove("currentTurn");
}

const $whiteScore = document.getElementById("white-score");
const $blackScore = document.getElementById("black-score");

async function showChessInfo() {
    const chessId = COOKIE("chessId");
    const response = await HTTP_CLIENT.get(PATH.CHESS + '?chessId=' + chessId);
    return await response.json();
}

function inputImageAtBoard(pieces) {
    Array.from(pieces)
        .filter(piece => piece.name !== "BLANK")
        .forEach(piece => {
            const position = piece.position;
            const pieceName = piece.color + "_" + piece.name;
            document.getElementById(position).innerHTML = PIECES[pieceName];
        });
}

async function onMove(event) {
    const $position = document.getElementsByClassName("selected");
    if (!$position.length) {
        onChangeColorOfSquare(event);
        return;
    }

    if (event.target.closest("div").classList.contains("selected")) {
        event.target.closest("div").classList.remove("selected");
        return;
    }

    const source = $position[0].id;
    const target = event.target.closest("div").id;
    const chessId = COOKIE('chessId');
    const moved = await patchMovePiece(chessId, source, target);
    revertSquareColor($position);
    if (!moved) {
        alert("해당 위치로 이동할 수 없습니다.");
        return;
    }

    movePiece(source, target);

    const moveResult = await showChessInfo();
    borderCurrentTurn(moveResult.turn);
    if (moveResult.status.includes("KING_DEAD")) {
        alert(`왕이 죽었습니다. \n\n ${winner(moveResult.turn)}의 승리! \n\n 게임을 종료합니다.`);
        window.location.href = "/";
    }
}

function winner(turn) {
    if (turn === 'WHITE') {
        return 'BLACK';
    }

    return 'WHITE';
}

async function patchMovePiece(chessId, source, target) {
    const response = await HTTP_CLIENT.patch(PATH.CHESS + `?chessId=${chessId}&source=${source}&target=${target}`);
    return response.ok;
}

function movePiece(source, target) {
    const $sourcePosition = document.getElementById(source);
    const $targetPosition = document.getElementById(target);

    $targetPosition.innerHTML = $sourcePosition.innerHTML;
    $sourcePosition.innerHTML = "";
}

function onChangeColorOfSquare(event) {
    let squareClassList = event.target.closest("div").classList;
    if (squareClassList.contains("selected")) {
        squareClassList.remove("selected");
    } else {
        squareClassList.add("selected");
    }
}

function revertSquareColor($position) {
    for (const $positionElement of $position) {
        $positionElement.classList.remove("selected");
    }
}

function onMouseOverSquare(event) {
    event.target.closest("div").classList.add("over-square");
}

function onRevertSquareColor(event) {
    event.target.closest("div").classList.remove("over-square");
}

function build() {
    let html = '<div id="board">';
    for (let rank = 8; rank >= 1; rank--) {
        html += addSquaresAtRank(rank);
    }
    html += "</div>";
    return html;
}

function addSquaresAtRank(rank) {
    let cellHtmlOfRank = '';
    for (let fileIndex = 0; fileIndex < 8; fileIndex++) {
        cellHtmlOfRank += addSquare(rank, fileIndex);
    }
    return cellHtmlOfRank;
}

function addSquare(rank, fileIndex) {
    let color = getSquareColor(rank, fileIndex);
    return `<div class=${color} id=${FILES[fileIndex] + rank}></div>`;
}

function getSquareColor(rank, fileIndex) {
    return isWhite(rank, fileIndex) ? WHITE_SQUARE : BLACK_SQUARE;
}

function isWhite(rank, fileIndex) {
    if ((rank % 2 === 0) && (fileIndex % 2 === 0)) {
        return true;
    }

    return (rank % 2 === 1) && (fileIndex % 2 === 1);
}
