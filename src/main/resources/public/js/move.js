let source = null;
let target = null;
const url = window.location;
const baseUrl = url.protocol + "//" + url.host;

const squares = document.getElementsByClassName("square");
for (let i = 0; i < squares.length; i++) {
    squares.item(i).addEventListener("click", function () {
        mark(this);
        canMove(this);
    });
}

function roomId() {
    let element = document.getElementById("roomId");
    if (element == null)
        return null;
    return element.innerText;
}

function move(source, target) {
    $.ajax({
        type: "POST",
        url: '/rooms/move',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            "source": source.id,
            "target": target.id,
            "roomId": roomId(),
        }),
        success: update,
        error: showError,
        complete: initialize,
    })
}

function update(response) {
    const board = response.squares;
    const turn = response.turn;
    const scores = response.scores;
    const winner = response.winner;

    for (let i = 0; i < board.length; i++) {
        let pieceId = board[i].position.file + board[i].position.rank;
        let piece = document.getElementById(pieceId);

        if (board[i].piece) {
            let pieceImage = board[i].piece.name + "_" + board[i].piece.team.toLowerCase();
            piece.firstElementChild.src = "/images/" + pieceImage + ".png";
        } else {
            piece.firstElementChild.src = "/images/blank.png";
        }
    }

    const nowTurn = document.getElementById("turn");
    nowTurn.innerText = turn + "팀 차례입니다.";

    let message = "";

    function getInnerText(team, scores) {
        return team + " 점수 | " + scores.score;
    }

    for (let i = 0; i < scores.length; i++) {
        const team = scores[i].team.toLowerCase();
        const score = document.getElementById(team);
        score.innerText = getInnerText(team, scores[i]);
        message += getInnerText(team, scores[i]) + "\n";
    }

    if (winner != null) {
        message += winner + "팀이 이겼습니다.🤭";
        alert(message);
        window.location = baseUrl + "/rooms";
    }
}

function showError(response) {
    alert(response.responseJSON.message);
}

function initialize() {
    initializeBoxShadow(source);
    initializeBoxShadow(target);
    source = null;
    target = null;
}

function initializeBoxShadow(location) {
    location.style.boxShadow = "";
}

function canMove(clickedLocation) {
    if (source === null) {
        source = clickedLocation;
        return;
    }
    if (target === null) {
        target = clickedLocation;
        move(source, target);
    }
}

function mark(clickedLocation) {
    if (clickedLocation.style.boxShadow) {
        clickedLocation.style.boxShadow = "";
    } else {
        clickedLocation.style.boxShadow = "inset 0px 0px 10px 3px #ffff60";
    }
}

function endGame() {
    alert('게임을 종료합니다. bye~');

    $.ajax({
        type: "POST",
        url: '/rooms/end',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            "roomId": roomId(),
        }),
        complete: redirect,
    })
}

function redirect() {
    window.location = `/rooms`;
}