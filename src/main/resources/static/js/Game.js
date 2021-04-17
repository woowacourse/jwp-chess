import {Board} from "./board/Board.js"
import {getData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = async function () {
  const response = await requestData();
  const pieces = response["pieceDtos"];
  const host = response["host"];
  const guest = response["guest"];
  const game = response["gameResponseDto"];

  initBoard(pieces, game["turn"]);
  fillInformation(host, guest)
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

function initBoard(pieces, turn) {
  const board = new Board(pieces, turn);
  addEvent(board);
}

function fillInformation(host, guest) {
  const blackNameTag = document.querySelector(".name-tag.black");
  blackNameTag.innerHTML = guest["name"];
  const blackRecordTag = document.querySelector(".record-tag.black");
  blackRecordTag.innerHTML = "검정색 플레이어";

  const whiteNameTag = document.querySelector(".name-tag.white");
  whiteNameTag.innerHTML = host["name"];
  const whiteRecordTag = document.querySelector(".record-tag.white");
  whiteRecordTag.innerHTML = "흰색 플레이어";
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
}
