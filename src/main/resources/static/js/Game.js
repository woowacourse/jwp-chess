import {Board} from "./board/Board.js"
import {GUEST, HOST, Role} from "./role/Role.js";
import {getData} from "./utils/FetchUtil.js"
import {getCookie, USER_ID_KEY} from "./utils/CookieUtil.js";

const url = "http://localhost:8080";

window.onload = async function () {
  const response = await requestData();
  if (!response) {
    alert("게임을 불러올 수 없습니다. 홈으로 돌아갑니다.");
    history.back();
    return;
  }
  const pieces = response["pieceResponseDtos"];
  const host = response["host"];
  const guest = response["guest"];
  const name = response["name"];
  const turn = response["turn"];
  const finished = response["finished"];
  const role = makeRole(host, guest);
  console.log(finished);
  if (finished) {
    alert("이미 끝난 게임입니다. 홈으로 돌아갑니다.");
    history.back();
    return;
  }

  initBoard(pieces, turn, role);
  fillInformation(host, guest)
}

async function requestData() {
  const gameId = findGameIdInUri();
  return await getData(`${url}/api/games/${gameId}`)
}

function findGameIdInUri() {
  const path = window.location.pathname
  const gameId = path.substr(path.lastIndexOf("/") + 1);
  return gameId;
}

function makeRole(host, guest) {
  const userId = getCookie(USER_ID_KEY);
  if (userId == host["id"]) {
    return new Role(HOST);
  }
  if (userId == guest["id"]) {
    return new Role(GUEST);
  }
}

function initBoard(pieces, turn, role) {
  const board = new Board(pieces, turn, role);
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
