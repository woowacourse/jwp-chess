let sourcePosition = "";
let running = false;


function move(position) {
  canMove(roomId);
  if (sourcePosition === "") {
    sourcePosition = position;
    return;
  }

  const object = {
    "source": sourcePosition,
    "destination": position,
  }

  movePiece(object, position);
}

function movePiece(object, position) {
  if (running === false) {
    alert("게임이 이미 종료되었습니다.");
    return;
  }
  $.ajax({
    url: "/board/"+roomId+"",
    type: "PUT",
    accept: 'application/json; charset=utf-8',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(object),
    success(data) {
      printGameState(data);
      changePiece(position);
    },
    error(error) {
      resetSourPosition();
      alert(error.responseText);
    }
  })
}

function changePiece(position) {
  const source = sourcePosition;
  document.getElementById(position).innerHTML = document.getElementById(source).innerHTML;
  document.getElementById(source).innerHTML = "";
  resetSourPosition();
}

function resetSourPosition() {
  sourcePosition = ""
}

function printGameState(result) {
  if (result.running === false) {
    running = true;
    alert(result.gameState + "가 승리했습니다.");
    document.getElementById("turn").innerText = "게임이 종료되었습니다.";
    getScore();
    return;
  }
  document.getElementById("turn").innerText = result.gameState + " Turn";
}

function canMove() {
  $.ajax({
    url: "/rooms/"+roomId+"/status",
    type: "GET",
    accept: 'application/json; charset=utf-8',
    contentType: 'application/json; charset=utf-8',
    success(data) {
      running = data.running;
    }
  })
}
