import {Board} from "./board/Board.js"
import {GUEST, HOST, Role} from "./role/Role.js";
import {getData} from "./utils/FetchUtil.js"
import {getCookie, USER_ID_KEY} from "./utils/CookieUtil.js";

const url = "http://localhost:8080";
let board;

window.onload = async function () {
  await initSocket();
  const response = await requestData();
  if (!response) {
    alert("게임을 불러올 수 없습니다. 홈으로 돌아갑니다.");
    history.back();
    return;
  }
  if (response["finished"]) {
    alert("이미 끝난 게임입니다. 홈으로 돌아갑니다.");
    history.back();
    return;
  }
  const host = response["host"];
  const guest = response["guest"];
  const name = response["name"];
  if (host["id"] && guest["id"]) {
    await initGame(response);
    return;
  }
  fillInformation(host, guest, name);
}

async function initGame(response) {
  const pieces = response["pieceResponseDtos"];
  const host = response["host"];
  const guest = response["guest"];
  const name = response["name"];
  const turn = response["turn"];
  const finished = response["finished"];
  const role = makeRole(host, guest);
  initBoard(pieces, turn, role);
  fillInformation(host, guest, name)
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
  board = new Board(pieces, turn, role);
  addEvent(board);
}

function fillInformation(host, guest, name) {
  const roomName = document.querySelector(".room-name-content");
  roomName.innerHTML = name;

  const blackNameTag = document.querySelector(".name-tag.black");
  blackNameTag.innerHTML = guest["name"];
  const blackRecordTag = document.querySelector(".record-tag.black");
  blackRecordTag.innerHTML = "흑팀";

  const whiteNameTag = document.querySelector(".name-tag.white");
  whiteNameTag.innerHTML = host["name"];
  const whiteRecordTag = document.querySelector(".record-tag.white");
  whiteRecordTag.innerHTML = "백팀";
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

async function initSocket() {
  const socket = new SockJS(`${url}/stomp`);
  const stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    const gameId = findGameIdInUri();
    stompClient.subscribe(`/topic/games/${gameId}/move`,
        response => actByMove(JSON.parse(response.body)));

    stompClient.subscribe(`/topic/games/${gameId}/join`,
        response => actByJoin(JSON.parse(response.body)));
  })
}

function actByMove(response) {
  const source = response["source"];
  const target = response["target"];
  const color = response["color"];
  const finished = response["finished"];
  board.moveOtherSide(source, target, color, finished);
}

function actByJoin(response) {
  initGame(response);
}
