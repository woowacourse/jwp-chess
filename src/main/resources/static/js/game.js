const roomNo = document.querySelector('#room').firstElementChild.className;
const $blackScore = document.querySelector("#blackScore");
const $whiteScore = document.querySelector("#whiteScore");
const $turn = document.querySelector("#turn");
const $homeButton = document.getElementById('home');
const $exitButton = document.getElementById('exit');
const $squares = document.querySelectorAll("td[class=chessboard]");

$homeButton.addEventListener('click', goHome);
$exitButton.addEventListener('click', exitChessRoom);

// draggable setting

for (let i = 0; i < $squares.length; i++) {
    $squares[i].addEventListener("dragstart", dragstart_handler)
    $squares[i].addEventListener("dragover", dragover_handler)
    $squares[i].addEventListener("drop", drop_handler)
}

function dragstart_handler(event) {
    event.dataTransfer.setData("text/plain", event.target.parentNode.id);
    event.dataTransfer.dropEffect = "move";
}

function dragover_handler(event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
}

function drop_handler(event) {
    event.preventDefault()
    const source = event.dataTransfer.getData("text/plain");
    const target = event.target.parentNode.id;

    const moveCommand = "move " + source + " " + target;
    sendMoveRequest(moveCommand)
}


async function sendMoveRequest(trimmedMoveCommand) {
    const data = {
        command: trimmedMoveCommand
    };
    const sourcePosition = trimmedMoveCommand.split(" ")[1]
    const targetPosition = trimmedMoveCommand.split(" ")[2]
    try {
        let response = await fetch(`/game/${roomNo}/move`, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(data)
        });
        if (response.ok) {
            response = await response.json();
            replaceComponents(response, sourcePosition, targetPosition);
            return;
        }
        throw await response.text();
    } catch (err) {
        alert(err);
    }
    ;
}

function replaceComponents(response, sourcePosition, targetPosition) {
    const nowTurn = $turn.className;
    if (!response.ongoing) {
        alert(nowTurn + " 승!");
        requestDelete();
        return;
    }
    $turn.className = response.turn;
    $turn.innerText = response.turn;
    document.querySelector("#" + sourcePosition).firstElementChild.src =
        '/images/' + response.chessBoard[sourcePosition].color + response.chessBoard[sourcePosition].type + '.png';
    document.querySelector("#" + targetPosition).firstElementChild.src =
        '/images/' + response.chessBoard[targetPosition].color + response.chessBoard[targetPosition].type + '.png';
    $blackScore.innerText = response.scoreDto.blackScore;
    $whiteScore.innerText = response.scoreDto.whiteScore;
}


function goHome() {
    window.location.replace("/");
}

function exitChessRoom() {
    const result = confirm('방을 나가면 다시 들어올 수 없습니다. 그래도 나가시겠습니까?');
    if (result) {
        requestDelete();
    }
}

async function requestDelete() {
    let response = await fetch(`/exit/${roomNo}`, {
        method: 'DELETE'
    });
    if (!response.ok) {
        alert('방 삭제 중 오류가 발생했습니다.');
        return;
    }
    window.location.replace("/");
}



