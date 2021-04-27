window.onload = startGame();

function startGame() {
    $.ajax({
        type: "POST",
        url: "/game",
        data: {
            roomName: document.getElementById("roomName").innerText
        },
        dataType: "json",
        success: setBoard,
        error: errorMessage
    });
}

function setBoard(res) {
    console.log("setBoard");
    let squares = res.squares;
    for (let i = 0; i < squares.length; i++) {
        if (squares[i].piece !== "empty_.") {
            document.getElementById(squares[i].position).firstElementChild.src = "../img/" + squares[i].piece + ".png";
        } else {
            document.getElementById(squares[i].position).firstElementChild.src = "../img/Blank.png";
        }
    }
    turnSetting(res.turn);
    status();
}

document.addEventListener("click", squareClick);
document.getElementById("end").addEventListener("click", endGame);
document.getElementById("restart").addEventListener("click", restartGame);

function restartGame() {
    $.ajax({
        type: "GET",
        url: `/new-game/${document.getElementById("roomName").innerText}`,
        data: {
            roomName: document.getElementById("roomName").innerText
        },
        dataType: "json",
        success: setBoard,
        error: errorMessage
    });
}

function squareClick(event) {
    if (event.target.parentNode.parentNode.parentNode.parentNode.classList.contains("board")
        && !event.target.parentNode.classList.contains("selected")) {
        event.target.parentNode.classList.add("selected");

        let numberOfSelectedSquares = document.getElementsByClassName("selected").length;
        let positionName = "";

        if (numberOfSelectedSquares <= 1) {
            positionName = "source";
        } else if (numberOfSelectedSquares > 1) {
            positionName = "target";
        }

        event.target.parentNode.classList.add(positionName);

        if (positionName === "target") {
            move();
        }
    } else if (event.target.parentNode.classList.contains("selected")) {
        event.target.parentNode.classList.remove("selected");
        event.target.parentNode.classList.remove("source");
    }
}

function endGame() {
    $.ajax({
        type: "DELETE",
        url: "/game",
        data: {
            roomName: document.getElementById("roomName").innerText
        },
        dataType: "json",
        complete: goHome
    });
}

function goHome() {
    window.location.href = "/";
}

function move() {
    $.ajax({
        type: "PUT",
        contentType: 'application/json',
        url: "/game",
        data: JSON.stringify({
            source: document.getElementsByClassName("source")[0].id,
            target: document.getElementsByClassName("target")[0].id,
            roomName: document.getElementById("roomName").innerText
        }),
        dataType: 'json',
        success: switchPiece,
        error: errorMessage,
        complete: clearSelect()
    });
}

function switchPiece(res) {
    console.log(res);
    let sourceElement = document.getElementById(res.source).firstElementChild;
    let targetElement = document.getElementById(res.target).firstElementChild;
    let blankImgUrl = "./img/Blank.png";

    targetElement.src = sourceElement.src;
    sourceElement.src = blankImgUrl;

    if (res.color === "흑" || res.color === "백") {
        alert(res.color + " 이 이겼습니다.");
        goHome();
    } else {
        turnSetting(res.color);
        status();
    }
}

function turnSetting(turn) {
    let turnBoard = document.getElementById("turn");
    let turnColor;
    if (turn === "W") {
        turnColor = "White"
    } else {
        turnColor = "Black"
    }

    turnBoard.innerHTML = turnColor;
}

function status() {
    $.ajax({
        type: "GET",
        url: `/status/${document.getElementById("roomName").innerText}`,
        success: printStatus,
        error: errorMessage,
    });
}

function errorMessage(res) {
    alert(res.responseText);
}

function clearSelect() {
    let source = document.getElementsByClassName("source")[0];
    let target = document.getElementsByClassName("target")[0]
    let selected = document.getElementsByClassName("selected");

    source.classList.remove("source");
    target.classList.remove("target");

    selected[1].classList.remove("selected");
    selected[0].classList.remove("selected");
}

function printStatus(res) {
    let whiteScore = res.whitePoint;
    let blackScore = res.blackPoint;

    let whiteScoreBoard = document.getElementById("white");
    let blackScoreBoard = document.getElementById("black");

    whiteScoreBoard.innerHTML = `White: ${whiteScore}`;
    blackScoreBoard.innerHTML = `Black: ${blackScore}`;
}
