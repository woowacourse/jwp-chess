let chessPage;

document.addEventListener("DOMContentLoaded", function () {
    chessPage = new ChessPage();
    chessPage.initChessPage();
});

function ChessPage() {
    this.roomId = document.querySelector("#roomId").textContent;
    this.getScoreUrl = window.location.origin + "/api/score/" + this.roomId;
    this.putBoardUrl = window.location.origin + "/api/board/" + this.roomId;
    this.postPiecesUrl = window.location.origin + "/api/pieces/" + this.roomId;
}

ChessPage.prototype.initChessPage = function () {
    this.pieces = this.postPieces();
    this.registerGameExitEvent();
    this.registerPieceMoveEvent();
    this.registerGetScoreEvent();
}

ChessPage.prototype.postPieces = async function () {
    return await fetch(this.postPiecesUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => res.json())
        .then(data => this.templatePieces(data.piecesInBoard));
}

ChessPage.prototype.templatePieces = function (pieces) {
    for (let i = 0; i < pieces.length; i++) {
        document.querySelector("#" + pieces[i].position)
            .insertAdjacentHTML("beforeend",
                chessPage.pieceElement(pieces[i]));
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

ChessPage.prototype.onClickCell = function (id) {
    let clickedId = id;
    let sourcePoint;
    let targetPoint;

    const list = document.querySelectorAll(".board-cell");
    for (let i = 0; i < list.length; i++) {
        if (list[i].classList.contains("clicked")) {
            sourcePoint = list[i].id;
            targetPoint = clickedId;
            document.getElementById(targetPoint).classList.toggle("clicked");
            this.movePieces(sourcePoint, targetPoint);
            document.getElementById(sourcePoint).classList.toggle("clicked");
            document.getElementById(targetPoint).classList.toggle("clicked");
            return;
        }
    }
    document.getElementById(clickedId).classList.toggle("clicked");
}

ChessPage.prototype.movePieces = function (sourcePoint, targetPoint) {
    const moveData = {
        roomId: this.roomId,
        source: sourcePoint,
        target: targetPoint
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
            chessPage.templatePieces(data.piecesInBoard);
            if (!data.playing) {
                alert(data.winnerColor + "팀이 승리했습니다.");
                location.href = '/';
            }
        })
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

    this.movePieces(source, target);
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
