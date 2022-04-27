const columns = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
const rows = ['8', '7', '6', '5', '4', '3', '2', '1'];

const gameId = window.location.pathname.split("/")[2];

const section = document.getElementById("chess-section");
const startButton = document.getElementById("start-button");
const resetButton = document.getElementById("reset-button");
const turnInfo = document.getElementById("turn-info");
const statusButton = document.getElementById("status-button");
const score = document.getElementById("score");
const deleteButton = document.getElementById("delete-button");
const modal = document.getElementById("modal");
const closeButton = document.getElementById("close-button");
const deleteGameForm = document.getElementById("delete-game-form");

const turn = {
  WHITE_RUNNING: "백",
  BLACK_RUNNING: "흑",
  WHITE_WIN: "백",
  BLACK_WIN: "흑",
};

const lightCellColor = "#ffffff";
const darkCellColor = "#8977ad";

let firstSelected;
let secondSelected;

window.onload = async function () {
  for (let i = 0; i < 8; i++) {
    const row = document.createElement("div");
    row.classList.add("row");
    makeRow(row, i);
    section.appendChild(row);
  }
  startButton.addEventListener("click", start);
  resetButton.addEventListener("click", reset);
  statusButton.addEventListener("click", getStatus);
  deleteButton.addEventListener("click", showDeleteForm);
  deleteGameForm.addEventListener("submit", deleteGame);
  closeButton.addEventListener("click", () => {
    modal.classList.add("hidden");
  });

  const res = await fetch(`/games/${gameId}`);
  if (!res.ok) {
    alert("존재하지 않는 게임입니다. 초기 화면으로 돌아갑니다.");
    location.href = "/";
    return;
  }
  const data = await res.json();
  load(data);
}

function load(data) {
  if (data.gameState === "READY") {
    turnInfo.innerText = "게임을 시작해주세요.";
    return;
  }
  rendBoard(data.board.pieces);
  printTurn(data);
  startButton.classList.add("hidden");
  resetButton.classList.remove("hidden");
}

function rendBoard(pieces) {
  clearBoard();
  pieces.forEach(piece => createPieceImage(piece.position, piece.pieceType, piece.color));
}

function clearBoard() {
  const cells = document.querySelectorAll("div.cell");
  cells.forEach(cell => removePiece(cell));
}

function makeRow(rowDiv, rowIndex) {
  for (let colIndex = 0; colIndex < 8; colIndex++) {
    const cell = document.createElement("div");
    cell.classList.add("cell");
    cell.id = createSquareId(colIndex, rowIndex);
    cell.style.background = decideCellColor(colIndex, rowIndex);
    cell.addEventListener("click", onclick);
    rowDiv.appendChild(cell);
  }
}

function createSquareId(column, row) {
  return columns[column] + rows[row];
}

function decideCellColor(column, row) {
  if ((column + row) % 2 === 0) {
    return lightCellColor;
  }
  return darkCellColor;
}

function printTurn(data) {
  score.innerText = "";
  if (data.gameState === "WHITE_WIN" || data.gameState === "BLACK_WIN") {
    turnInfo.innerText = `${turn[data.gameState]}의 승리입니다.`;
    alert(`${turn[data.gameState]}의 승리입니다.`);
    return;
  }
  turnInfo.innerText = `${turn[data.gameState]}의 차례입니다.`;
}

async function start() {
  const res = await fetch(`/games/${gameId}`, {
    method: "PATCH",
  });
  const data = await res.json();
  if (!res.ok) {
    alert(data.message);
    return;
  }
  rendBoard(data.board.pieces);
  printTurn(data);
  startButton.classList.add("hidden");
  resetButton.classList.remove("hidden");
}

async function reset() {
  const res = await fetch(`/games/${gameId}`, {
    method: "PUT",
  });
  const data = await res.json();
  if (!res.ok) {
    alert(data.message);
    return;
  }
  clearBoard();
  turnInfo.innerText = "게임을 시작해주세요.";
  score.innerText = "";
  startButton.classList.remove("hidden");
  resetButton.classList.add("hidden");
}

function removePiece(cell) {
  if (cell.hasChildNodes()) {
    cell.removeChild(cell.firstChild);
  }
}

function createPieceImage(position, pieceType, color) {
  const cell = document.getElementById(position);
  const piece = document.createElement("img");
  piece.classList.add("piece-image");
  piece.src = `/images/${pieceType}${color}.png`
  cell.appendChild(piece);
}

async function move() {
  const res = await requestMove();
  const data = await res.json();
  clearSelection();
  if (!res.ok) {
    alert(data.message);
    return;
  }
  rendBoard(data.board.pieces);
  printTurn(data);
}

async function onclick(event) {
  const cell = event.currentTarget;
  decideSelection(cell);
  if (firstSelected && secondSelected) {
    await move();
  }
}

function decideSelection(cell) {
  if (!firstSelected && !cell.hasChildNodes()) {
    return;
  }
  if (firstSelected === cell) {
    const piece = firstSelected.childNodes[0];
    piece.classList.remove("selected");
    firstSelected = null;
    return;
  }
  if (!firstSelected) {
    firstSelected = cell;
    highlightSelectedCell(cell);
    return;
  }
  secondSelected = cell;
}

function highlightSelectedCell(cell) {
  const piece = cell.childNodes[0];
  piece.classList.add("selected");
}

async function requestMove() {
  return await fetch(`/games/${gameId}/pieces`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      start: firstSelected.getAttribute("id"),
      target: secondSelected.getAttribute("id"),
    }),
  });
}

function clearSelection() {
  firstSelected.childNodes[0].classList.remove("selected");
  firstSelected = null;
  secondSelected = null;
}

async function getStatus() {
  const res = await fetch(`/games/${gameId}/status`);
  const data = await res.json();
  if (!res.ok) {
    alert(data.message);
    return;
  }
  score.innerText = `백: ${data.whiteScore}점
  흑: ${data.blackScore}점`;
}

function showDeleteForm() {
  modal.classList.remove("hidden");
}

async function deleteGame() {
  modal.classList.add("hidden");
  const res = await fetch(`/games/${gameId}`, {
    method: "DELETE",
    headers: {
      "Authorization": this.password.value,
    },
  });
  if (!res.ok) {
    const data = await res.json();
    alert(data.message);
    this.password.value = "";
    return;
  }
  alert("게임이 성공적으로 삭제되었습니다. 로비로 돌아갑니다.");
  location.replace("/");
}
