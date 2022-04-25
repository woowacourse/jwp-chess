const FILES = ["A", "B", "C", "D", "E", "F", "G", "H"];
const RANKS = ["1", "2", "3", "4", "5", "6", "7", "8"];
const REVERSE_RANKS = RANKS.reverse();
const pieceRegExp = /images\/(\w*?)\.png/;

$(document).ready(function () {
  initChessBoard();
  triggerEvents();

  if (isNewGame()) {
    initChessPieces();
    currentTeam();
  } else {
    loadPiecesAndTeam();
  }
});

function isNewGame() {
  return JSON.parse($("#isNewGame").val());
}

function initChessBoard() {
  for (let r = 0; r < REVERSE_RANKS.length; r++) {
    addChessBoardRow();
    for (let f = 0; f < FILES.length; f++) {
      addChessBoardSquare(r, f);
    }
  }
}

function initChessPieces() {
  $.ajax({
    url: "/board",
    method: "GET",
    dataType: "json",
  })
    .done(function (json) {
      placeChessPieces(json.board);
    })
    .fail(function (xhr, status, errorThrown) {
      alert("initChessPieces - error !");
    });
}

function loadPiecesAndTeam() {
  $.ajax({
    url: "/load-last-game",
    method: "GET",
    dataType: "json",
  })
    .done(function (data) {
      placeChessPieces(data.boardResponse.board);
      setCurrentTeam(data.lastTeam);
    })
    .fail(function (xhr, status, errorThrown) {
      alert("loadPiecesAndTeam - error !");
      console.log(xhr);
    });
}

function triggerEvents() {
  $("button#move").click(function () {
    movePiece();
  });

  $("button#save-game").click(function () {
    saveGame();
  });
}

function addChessBoardRow() {
  $("#chess-board").append("<tr>");
}

function addChessBoardSquare(r, f) {
  const rank = REVERSE_RANKS[r];
  const file = FILES[f];
  const squareId = file + rank;
  const color = getColor(r, f);
  $("#chess-board tr")
    .last()
    .append(`<td id=${squareId} style="background-color: ${color}">`);
}

function getColor(r, f) {
  if (isEven(r, f)) {
    return "#AD8B73";
  }
  return "#CEAB93";
}

function isEven(r, f) {
  return (r + f) % 2 === 0;
}

function placeChessPiece(position, pieceImage) {
  $("#chess-board td#" + position).append(
    `<img src="images/${pieceImage}.png" />`
  );
}

function placeChessPieces(piecePositions) {
  for (let element of piecePositions) {
    let team = element["team"];
    let piece = element["piece"];
    let position = element["position"].toUpperCase();
    let pieceImage = team + piece; // ex) blackPawn

    if (team !== "empty") {
      placeChessPiece(position, pieceImage);
    }
  }
}

function movePiece() {
  const from = $('input[name="from"]').val();
  const to = $('input[name="to"]').val();
  $.ajax({
    url: `/move?from=${from}&to=${to}`,
    method: "POST",
    dataType: "json",
  })
    .done(function (data) {
      clearChessBoard();
      initChessBoard();
      currentTeam();
      placeChessPieces(data.board);
    })
    .fail(function (xhr, status, errorThrown) {
      alert("movePiece - error !");
    });
}

function clearChessBoard() {
  $("#chess-board").children().remove();
}

function setCurrentTeam(teamName) {
  $("#current-team").text(teamName);
}

function currentTeam() {
  $.ajax({
    url: "/current-team",
    method: "GET",
    dataType: "text",
  })
    .done(function (data) {
      setCurrentTeam(data);
    })
    .fail(function (xhr, status, errorThrown) {
      alert("currentTeam - error !");
    });
}

function isExistPiece(sqaure) {
  return $("#" + sqaure).find("img").length == true;
}

function getPieces() {
  let pieces = {};

  $("#chess-board tr td").each(function (index, element) {
    const square = element;
    if (isExistPiece(square.id)) {
      const imageName = $(square).find("img").attr("src");
      const piece = imageName.replace(pieceRegExp, `$1`);
      pieces[square.id] = piece;
    }
  });

  return pieces;
}

function convertAndGetCurrentTeam() {}

function saveGame() {
  const gameData = {
    currentTeam: $("#current-team").text(),
    pieces: getPieces(),
  };

  $.ajax({
    url: "/save-game",
    method: "POST",
    data: JSON.stringify(gameData),
    contentType: "application/json",
  })
    .done(function (data) {
      alert("저장 완료 !");
    })
    .fail(function (xhr, status, errorThrown) {
      alert("저장 오류");
      console.log(xhr);
    });

  location.replace("/");
}
