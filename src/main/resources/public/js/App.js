const App = function(roomName) {
  this.$chessBoard = document.querySelector("#chess_board");
  this.$endBtn = document.querySelector("#end");
  this.$lobbyBtn = document.querySelector("#lobby");
  this.$currentTurn = document.querySelector("#current_turn");
  this.$blackScore = document.querySelector("#black_score");
  this.$whiteScore = document.querySelector("#white_score");
  this.$winner = document.querySelector("#winner");
  this.$roomName = roomName;

  this.renderEmptyBoard = function() {
    this.$chessBoard.innerHTML = '';

    for (let r = 1; r <= 8; r++) {
      const row = document.createElement("tr");
      for (let c = 1; c <= 8; c++) {
        const column = document.createElement("td");
        column.setAttribute("class", "chess_cell");
        column.setAttribute("id", `${c}${8 - r + 1}`);
        column.innerText = ".";
        row.appendChild(column);
      }
      this.$chessBoard.appendChild(row);
    }
  }.bind(this);

  this.renderRoomName = function() {
    document.querySelector("#room_name").insertAdjacentHTML("beforeend",
        `<h1>${this.$roomName}</h1>`);
  }.bind(this);

  this.renderBoard = function(response) {
    this.renderEmptyBoard();

    response.forEach((piece) => {
      const viewPiece = document.getElementById(piece.location);
      viewPiece.innerText = piece.team + piece.signature;
    })

  }.bind(this);

  this.renderMessage = function(payload) {
    const blackScore = payload.scoreDto.blackScore;
    const whiteScore = payload.scoreDto.whiteScore;
    const currentTeam = payload.currentTeam;
    const winner = payload.scoreDto.winner;

    this.$blackScore.innerHTML = `블랙 점수 : ${blackScore}`;
    this.$whiteScore.innerHTML = `화이트 점수 : ${whiteScore}`;
    this.$currentTurn.innerHTML = `현재 턴 : ${currentTeam}`;

    if (blackScore == 0 || whiteScore == 0) {
      alert(`축하합니다!! 승자 : ${winner}`);
      this.$winner.innerHTML = `승자 : ${winner}`;
    }

  }.bind(this);

  this.getClickedPiece = function() {
    const pieces = document.getElementsByTagName("td");
    for (let i = 0; i < pieces.length; i++) {
      if (pieces[i].classList.contains("clicked")) {
        return pieces[i];
      }
    }
    return null;
  }.bind(this);

  this.onClickPiece = function(event) {
    const clickPiece = event.target.closest("td");
    const clickedPiece = this.getClickedPiece();
    if (clickedPiece) {
      clickedPiece.classList.remove("clickedTile");
      clickedPiece.classList.toggle("clicked");
      if (clickedPiece != clickPiece) {
        this.move(clickedPiece.id, clickPiece.id);
      }
    } else {
      clickPiece.classList.toggle("clicked");
      clickPiece.classList.add("clickedTile");
    }
  }.bind(this);

  this.move = function(source, target) {
    const payload = {
      source: source,
      target: target
    }

    fetch(`http://localhost:8080/api/room/${this.$roomName}/move`, {
      method: 'POST',
      headers: {'content-type': 'application/json'},
      body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(result => {
      if (result.status === "ERROR") {
        alert(result.message);
        return;
      }
      this.renderBoard(result.pieces);
      this.renderMessage(result);
    })
    .catch(err => alert(err));
  }.bind(this);

  this.$chessBoard.addEventListener('click', this.onClickPiece);

  this.$endBtn.addEventListener('click', () => {
    if (confirm("게임을 끝내시겠습니까?")) {
      fetch(`http://localhost:8080/api/room/${this.$roomName}/end`, {
        method: 'GET'
      })
      .then(response => response.json())
      .then(result => {
        if (result.status === "ERROR") {
          alert(result.message);
          return;
        }
        this.renderBoard(result.pieces);
        this.renderMessage(result);
      })
      .catch(err => alert(err));
    }
  });

  this.$lobbyBtn.addEventListener('click', () => {
    if (confirm("로비로 돌아가시겠습니까?")) {
      window.location.href = "http://localhost:8080/";
    }
  });

  this.constructor = function() {
    this.renderEmptyBoard();
    this.renderRoomName();
    fetch(`http://localhost:8080/api/room/${roomName}`)
    .then(response => response.json())
    .then(result => {
      if (result.status === "ERROR" && result.message === "[ERROR] 아직 시작되지 않은 방입니다.") {
        this.renderEmptyBoard();
        return;
      }
      this.renderBoard(result.pieces);
      this.renderMessage(result);
    })
    .catch(err => alert(err));
  }.bind(this);
}

window.onload = () => {
  const pathName = decodeURI(window.location.pathname);
  const app = new App(pathName.split("/")[2]);
  app.constructor();
}