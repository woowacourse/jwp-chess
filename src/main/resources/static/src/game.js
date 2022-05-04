const pathNames = window.location.pathname.split("/");
const id = pathNames[2];
let positions = "";

window.onload = async () => {
    let data = await fetch("/chess-game/" + id + "/board", {
        method: "GET"
    })
    .then(r=>r.json())
    .then(data => {
        return data;
    });

    JsonSender.setChessBoard(data);
}

const move = async function (position) {
    positions += position;
    if (positions.length == 4) {
        const source = positions.substring(0, 2);
        const target = positions.substring(2, 4);

        JsonSender.sendSourceTarget(source, target);
        positions = "";
    }
}

const JsonSender = {
    sendSourceTarget: function(source, target) {
        fetch('/chess-game', {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id: id,
                source: source,
                target: target
            })
        })
        .then(handleErrors)
        .then(r=>r.json())
        .then(data => {
            if (data.isFinished === true) {
                alert(data.turn + "이 승리하였습니다!!!");
                window.location.replace("/chess-game/" + id + "/end");
                return;
            }

            JsonSender.removeChessBoard();
            JsonSender.setChessBoard(data);

            return data;
        })
        .catch(function (error) {
            alert(error.message);
        });
    },

    setChessBoard: function (chessGame) {
        const chessBoard = chessGame.board;

        const turnInfo = document.querySelector(".turnInfo");
        const turn = turnInfo.querySelector("tbody tr td");
        turn.innerHTML = '<img src="../images/' + chessGame.turn + '_turn.png" class="img"/>';

        for (let file = 0; file < 8; file++) {
            for (let rank = 1; rank <= 8; rank++) {
                const position = toFileName(file) + rank;
                const piece = chessBoard[position];

                const eachDiv = document.getElementById(position);
                if (piece) {
                    eachDiv.innerHTML = '<img src="../images/' + piece.name + '_' + piece.color + '.png" class="img"/>';
                    continue;
                }
                eachDiv.innerHTML = '<img src="../images/empty.png" class="img" />';
            }
        }
    },

    removeChessBoard: function () {
        const turnInfo = document.querySelector(".turnInfo");
        const turn = turnInfo.querySelector("tbody tr td");
        turn.innerHTML = "";

        for (let file = 0; file < 8; file++) {
            for (let rank = 1; rank <= 8; rank++) {
                const position = toFileName(file) + rank;
                const eachDiv = document.getElementById(position);
                eachDiv.innerHTML = "";
            }
        }
    }
}

function toFileName(file) {
    const fileNames = new Map([
        [0, "A"], [1, "B"], [2, "C"], [3, "D"], [4, "E"], [5, "F"], [6, "G"], [7, "H"]
    ]);
    return fileNames.get(file);
}

async function handleErrors(response) {
    if (!response.ok) {
        const errorMessage = await response.json().then((data) => {
            return data.errorMessage;
        })
        throw Error(errorMessage);
    }
    return response;
}

function lobby() {
    window.location.replace("/");
}

async function start() {
    await fetch("/chess-game/" + id + "/initialization", {
        method: "GET"
    });

    window.location.reload();
}
