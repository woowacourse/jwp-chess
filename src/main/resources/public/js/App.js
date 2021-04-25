const App = function(roomName) {
  this.$chessBoard = document.querySelector("#chess_board");
  this.$startBtn = document.querySelector("#start");
  this.$endBtn = document.querySelector("#end");
  this.$currentTurn = document.querySelector("#current_turn");
  this.$blackScore = document.querySelector("#black_score");
  this.$whiteScore = document.querySelector("#white_score");
  this.$winner = document.querySelector("#winner");
  this.roomName = roomName;

  this.renderEmptyBoard = () => {
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
  };

  this.renderBoard = (response) => {
    this.renderEmptyBoard();

    response.forEach((piece) => {
      const viewPiece = document.getElementById(piece.location);
      viewPiece.innerText = piece.team + piece.signature;
    })
  };

  this.renderMessage = (payload) => {
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
  };

  this.getClickedPiece = () => {
    const pieces = document.getElementsByTagName("td");
    for (let i = 0; i < pieces.length; i++) {
      if (pieces[i].classList.contains("clicked")) {
        return pieces[i];
      }
    }
    return null;
  };

  this.onClickPiece = (event) => {
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
  };

  this.move = (source, target) => {
    const payload = {
      source: source,
      target: target
    }

    fetch(`http://localhost:8080/rooms/${this.roomName}/move`, {
      method: 'PUT',
      headers: {'content-type': 'application/json'},
      body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(responseJson => {
      if (responseJson.status != "OK") {
        alert(responseJson.detailMessage);
        return;
      }
      this.renderBoard(responseJson.payload.pieces);
      this.renderMessage(responseJson.payload);
    })
    .catch(err => alert(err));
  };

  this.$chessBoard.addEventListener('click', this.onClickPiece);
  this.$startBtn.addEventListener('click', () => {
    fetch(`http://localhost:8080/rooms/${this.roomName}/start`, {
      method: 'PUT',
      headers: {'content-type': 'application/json'}
    })
      .then(response => response.json())
      .then(responseJson => {
        if (responseJson.status != "OK") {
          alert(responseJson.detailMessage);
          return;
        }
        this.renderBoard(responseJson.payload.pieces);
        this.renderMessage(responseJson.payload);
        alert("게임이 시작되었습니다.");
      })
      .catch(err => alert(err));
  })

  this.$endBtn.addEventListener('click', () => {
    fetch(`http://localhost:8080/rooms/${this.roomName}/end`, {
      method: 'PUT',
      headers: {'content-type': 'application/json'}
    })
      .then(response => response.json())
      .then(responseJson => {
        if (responseJson.status != "OK") {
          alert(responseJson.detailMessage);
          return;
        }
        this.renderBoard(responseJson.payload.pieces);
        this.renderMessage(responseJson.payload);
        alert("게임이 종료되었습니다.");
      })
      .catch(err => alert(err));
  })

  this.constructor = () => {
    this.renderEmptyBoard();

    fetch(`http://localhost:8080/rooms/${this.roomName}`)
      .then(response => response.json())
      .then(responseJson => {
        if (responseJson.status != "OK") {
          alert(responseJson.detailMessage);
          return;
        }
        this.renderBoard(responseJson.payload.pieces);
        this.renderMessage(responseJson.payload);
      })
      .catch(error => alert('에러 : ' + error));
  };

  this.constructor();
}

window.onload = () => {
  const roomName = window.location.pathname.split("/")[2];
  const app = new App(roomName);
  app.constructor();
}
