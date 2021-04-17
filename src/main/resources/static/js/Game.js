import {Board} from "./board/Board.js"
import {getData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = async function () {
    const response = await requestData();
    const pieces = response["pieceDtos"];
    const host = response["host"];
    const guest = response["guest"];
    const game = response["gameResponseDto"];
    const blackScore = response["blackScore"];
    const whiteScore = response["whiteScore"];

    initBoard(pieces);
    fillInformation(host, guest, game, blackScore, whiteScore)
}

async function requestData() {
    const gameId = findGameIdInUri();
    return await getData(`${url}/chess/${gameId}`)
}

function findGameIdInUri() {
    const path = window.location.pathname
    const gameId = path.substr(path.lastIndexOf("/") + 1);
    return gameId;
}

function initBoard(pieces) {
    const board = new Board(pieces);
    addEvent(board);
}

function fillInformation(host, guest, game, blackScore, whiteScore) {
    const blackNameTag = document.querySelector(".name-tag.black");
    blackNameTag.innerHTML = guest["name"];
    const blackRecordTag = document.querySelector(".record-tag.black");
    blackRecordTag.innerHTML = "검정색 플레이어";
    const blackScoreTag = document.querySelector(".status-tag.black");
    blackScoreTag.innerHTML = blackScore + " 점";

    const whiteNameTag = document.querySelector(".name-tag.white");
    whiteNameTag.innerHTML = host["name"];
    const whiteRecordTag = document.querySelector(".record-tag.white");
    whiteRecordTag.innerHTML = "흰색 플레이어";
    const whiteScoreTag = document.querySelector(".status-tag.white");
    whiteScoreTag.innerHTML = whiteScore + " 점";

    fillTurnInfo(game)
}

function fillTurnInfo(game) {
    document.getElementById("turn").innerHTML = game["turn"] + " 턴 입니다"

}

function addEvent(board) {
    const body = document.querySelector("body")
    body.addEventListener("dragover", allowDrop);
    body.addEventListener("drop", e => dropPiece(e, board));
}

function allowDrop(e) {
    e.preventDefault()
}

function dropPiece(e, board) {
    const sourcePosition = e.dataTransfer.getData("sourcePosition");
    const piece = board.findPieceBySourcePosition(sourcePosition);
    piece.unhighlight();
    fillTurnInfo()
}
