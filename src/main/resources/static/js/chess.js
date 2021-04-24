let chessPage;

document.addEventListener("DOMContentLoaded", function () {
    chessPage = new ChessPage();
    chessPage.initChessPage();
});

function ChessPage() {
    this.roomId = document.querySelector("#roomId").textContent;
    this.getScoreUrl = window.location.origin + "/api/score/" + this.roomId;
    this.putBoardUrl = window.location.origin + "/api/board/" + this.roomId;
}

ChessPage.prototype.initChessPage = function () {
    this.templatePieces(JSON.parse(localStorage.getItem("pieces")));
    this.registerGameExitEvent();
    this.registerPieceMoveEvent();
    this.registerGetScoreEvent();
}

ChessPage.prototype.templatePieces = function (pieces) {
    for (let i = 0; i < pieces.piecesInBoard.length; i++) {
        document.querySelector("." + pieces.piecesInBoard[i].position)
            .insertAdjacentHTML("beforeend",
                chessPage.pieceElement(pieces.piecesInBoard[i]));
    }
}

ChessPage.prototype.pieceElement = function (piece) {
    if (piece.name === ".") {
        return `<img class="chess-piece">`;
    }

    let color;
    if (piece.name.charCodeAt(0) >= 97) {
        color = "w";
    } else {
        color = "b";
    }

    const url = color + piece.name.toUpperCase() + ".png";
    return `<img src="/images/${url}" class="chess-piece">`;
}

ChessPage.prototype.registerPieceMoveEvent = function () {
    document.querySelector(".move-button")
        .addEventListener("click", function () {
            chessPage.putPieces();
        });
}

ChessPage.prototype.putPieces = function () {
    let source = document.querySelector('.chess-game-move-button .source').value;
    document.querySelector('.chess-game-move-button .source').value = "";
    let target = document.querySelector('.chess-game-move-button .target').value;
    document.querySelector('.chess-game-move-button .target').value = "";

    const moveData = {
        roomId: this.roomId,
        source: source,
        target: target
    };

    const obj = {
        body: JSON.stringify(moveData),
        headers: {
            'Content-Type': 'application/json',
        },
        method: 'PUT'
    }

    fetch(this.putBoardUrl, obj)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            }
            response.text().then(function (data) {
                alert(data);
            })
            throw Error();
        })
        .then(function (data) {
            chessPage.deleteAllPieces();
            chessPage.templatePieces(data);
            if (!data.playing) {
                alert(data.winnerColor + "팀이 승리했습니다.");
                location.href = '/';
            }
        })
}

ChessPage.prototype.deleteAllPieces = function () {
    document.querySelectorAll('.chess-piece').forEach(item => {
        item.remove();
    });
}

ChessPage.prototype.registerGetScoreEvent = function () {

    document.querySelector(".chess-game-black-score-button")
        .addEventListener("click", function () {
            chessPage.getScore("BLACK");
        });

    document.querySelector(".chess-game-white-score-button")
        .addEventListener("click", function () {
            chessPage.getScore("WHITE");
        });
}

ChessPage.prototype.getScore = function (colorName) {
    fetch(
        chessPage.getScoreUrl + "/" + colorName,
        {
            method: 'GET'
        }).then(res => res.json())
        .then(function (data) {
            alert(data.score + "점");
        });
}

ChessPage.prototype.registerGameExitEvent = function () {
    document.querySelector(".chess-game-exit-button")
        .addEventListener("click", function () {
            alert("게임을 종료합니다.");
            location.href = '/';
        });
}
