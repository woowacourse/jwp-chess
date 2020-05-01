window.onload = function () {
  const PIECES = {
    BLACK_KING: `<img src="../../image/king_black.png" class="piece king"/>`,
    BLACK_QUEEN: `<img src="../../image/queen_black.png" class="piece queen"/>`,
    BLACK_ROOK: `<img src="../../image/rook_black.png" class="piece rook"/>`,
    BLACK_BISHOP: `<img src="../../image/bishop_black.png" class="piece bishop"/>`,
    BLACK_KNIGHT: `<img src="../../image/knight_black.png" class="piece knight"/>`,
    BLACK_BLACK_PAWN: `<img src="../../image/pawn_black.png" class="piece pawn"/>`,
    WHITE_KING: `<img src="../../image/king_white.png" class="piece king"/>`,
    WHITE_QUEEN: `<img src="../../image/queen_white.png" class="piece queen"/>`,
    WHITE_ROOK: `<img src="../../image/rook_white.png" class="piece rook"/>`,
    WHITE_BISHOP: `<img src="../../image/bishop_white.png" class="piece bishop"/>`,
    WHITE_KNIGHT: `<img src="../../image/knight_white.png" class="piece knight"/>`,
    WHITE_WHITE_PAWN: `<img src="../../image/pawn_white.png" class="piece pawn"/>`,
  };
  let startPosition = null;
  let pathName;

  async function getChessGame() {
    let path = window.location.pathname;
    pathName = path.substring(path.lastIndexOf("/") + 1);
    const response = await fetch(`http://localhost:8080/board/${pathName}`);
    return await response.json();
  }

  function setBoard(board) {
    document.querySelectorAll(".position").forEach(element => {
      while (element.lastElementChild) {
        element.removeChild(element.lastElementChild);
      }
    });
    Object.keys(board)
      .filter(position => board[position]["pieceType"] !== "BLANK")
      .forEach(position => {
        const color = board[position]["color"];
        const pieceType = board[position]["pieceType"];
        document.getElementById(position)
          .insertAdjacentHTML("beforeend", PIECES[`${color}_${pieceType}`]);
      });
  }

  function setTurn(turn) {
    document.getElementById("turn_box")
      .innerHTML = '<p id="turn" style="font-size: 36px; padding-top: 20px;">' + turn["color"] + "의 턴입니다." + '</p>';
  }

  function setScore(score) {
    document.getElementById("score")
      .innerHTML = '<p id="turn" style="font-size: 18px; padding-top: 15px;">' +
      "WHITE 팀의 점수는 " + score["WHITE"] + "점 입니다.\n" +
      "BLACK 팀의 점수는 " + score["BLACK"] + "점 입니다." +
      '</p>';
  }

  (async function () {
    const chessGame = await getChessGame();
    const board = await chessGame["boardDto"]["board"];
    console.log(board);
    const turn = chessGame["turn"];
    const score = chessGame["score"]["scores"];

    if (chessGame["normalStatus"] === false) {
      alert("잘못된 명령입니다.");
      return;
    }
    setBoard(board);
    setTurn(turn);
    setScore(score);
  })();

  function chooseFirstPosition(position) {
    fetch(`http://localhost:8080/board/${pathName}/source?source=${position}`,
      {method: "GET"})
      .then(res => res.json())
      .then(data => {
        startPosition = data.position;
        console.log(data.normalStatus);
        if (data.normalStatus === false) {
          alert(data.exception);
          startPosition = null;
          return;
        }
        const positions = data.movable;
        positions.forEach(position => {
          document.getElementById(position)
            .style
            .backgroundColor = "rgba(255, 100, 0, 0.2)";
        });

        document.getElementById(position)
          .style
          .backgroundColor = "rgba(255, 200, 0, 0.2)";
      });
  }

  function chooseSecondPosition(position) {
    fetch(`http://localhost:8080/board/${pathName}/destination?destination=${position}&startPosition=${startPosition}`,
      {method: "GET"})
      .then(function (response) {
        return response.json();
      })
      .then(data => {
        if (data.normalStatus === false) {
          alert(data.exception);
          startPosition = null;
          window.location.href = `/loading/${pathName}`;
          return;
        }
        const source = startPosition;
        const destination = position;
        startPosition = null;

        fetch(`http://localhost:8080/board/${pathName}`, {
          method: "POST",
          body: JSON.stringify({
            start: source,
            end: destination
          }),
          headers: {"Content-Type": "application/json"}
        })
          .then(function (winnerResponse) {
            return winnerResponse.json();
          })
          .then(winnerData => {
            console.log(winnerData);
            if (winnerData["winner"] !== "NONE") {
              let winnerTeam = winnerData["winner"];
              return location.href = `/result/${winnerTeam}`;
            }
            location.href= `/loading/${pathName}`;
          })
      });
  }

  document.querySelectorAll(".position").forEach(
    element => {
      element.addEventListener("click", (e) => {
        let position = e.currentTarget.id;
        e.preventDefault();
        if (startPosition == null) {
          chooseFirstPosition(position);
        } else {
          chooseSecondPosition(position);
        }
      });
    }
  );
};