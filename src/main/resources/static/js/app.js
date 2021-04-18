const end = document.getElementById("end");
const exit = document.getElementById("exit");
const chessBoard = document.querySelector(".chess-board");
const tiles = document.getElementsByClassName("tile");
const whiteCount = document.querySelector(`#whiteScore strong`);
const blackCount = document.querySelector(`#blackScore strong`);
const winner = document.querySelector(`#winner`);
const basePath = 'http://localhost:8080';

end.addEventListener("click", (event) => {
    const item = event.target;
    if (item.classList.contains("game_over")) {
        alert("이미 게임끝냤슈!");
        return
    }
    if (window.confirm("정말 끝내려구?")) {
        if (!item.classList.contains("game_over")) {
            item.classList.add("game_over");
            item.classList.remove("run");
        }

        const data = {
            chessName: localStorage.getItem("name"),
            gameOver: true
        };

        const option = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };

        fetch(basePath + "/api/games", option)
        .then(async () => {
            await loadGame();
        });
        alert("이 게임 끝났습니다.");
    }
});

exit.addEventListener("click", () => {
    if (window.confirm("정말 나갈래요?")) {
        window.location = basePath;
    }
})

const loadGame = async () => {
    const response = await fetch(
        basePath + "/api/games/" + localStorage.getItem("name"))
    .then(res => res.json());

    if (response.statusCode === 400) {
        alert(response.message);
        return;
    }
    reRangeBoard(response.body);
    console.log(response.body);

    if (response.body.gameOver) {
        let winnerNode = winner.querySelector("strong");
        if (response.body.winner === "NOTHING") {
            winnerNode.innerText = "무승부";
            alert("무승부!");
        } else {
            winnerNode.innerText = response.body.winner;
            alert("승리자는" + response.body.winner);
        }
        winner.style.visibility = "visible";
    }
};

loadGame();

function reRangeBoard(responsePieces) {
    for (let idx = 0; idx < tiles.length; idx++) {
        let childNodes = tiles[idx].childNodes;
        for (let childIdx = 0; childIdx < childNodes.length; childIdx++) {
            let img = document.createElement("img");
            tiles[idx].replaceChild(img, childNodes[childIdx]);
        }
    }

    const pieces = responsePieces.pieces;
    for (let pieceIdx = 0; pieceIdx < pieces.length; pieceIdx++) {
        for (let idx = 0; idx < tiles.length; idx++) {
            if (tiles[idx].id === pieces[pieceIdx].position) {
                let img = document.createElement("img");
                img.src = "css/image/" + imageName(pieces[pieceIdx].pieceName)
                    + ".png";
                tiles[idx].removeChild(tiles[idx].childNodes[0]);
                tiles[idx].appendChild(img);
                break;
            }
        }
    }
    if (responsePieces.gameOver && !end.classList.contains("game_over")) {
        end.classList.add("game_over");
        end.classList.remove("run");
    }
    whiteCount.innerText = responsePieces.scoreDto.whiteScore;
    blackCount.innerText = responsePieces.scoreDto.blackScore;
}

function imageName(pieceName) {
    const pattern = /[a-z]/;
    if (pattern.test(pieceName)) {
        return "W" + pieceName.toUpperCase();
    }
    return "B" + pieceName;
}

chessBoard.addEventListener("click", async (source) => {
    if (end.classList.contains("game_over")) {
        alert("게임 끝났슈!");
        return;
    }

    const nowClickedPiece = source.target.closest("div");
    const pastClickedPiece = decideClickedPiece();
    if (pastClickedPiece === "") {
        if (!nowClickedPiece.children[0].src) {
            alert("빈 공간은 선택할 수 없습니다!");
            return;
        }
        nowClickedPiece.classList.toggle("clicked");
        return;
    }

    clearClicked();
    await movePiece(pastClickedPiece.id, nowClickedPiece.id);
})

function decideClickedPiece() {
    const divs = document.getElementsByTagName("div");
    for (let i = 0; i < divs.length; i++) {
        if (divs[i].classList.contains("clicked")) {
            return divs[i];
        }
    }
    return "";
}

function clearClicked() {
    const divs = document.getElementsByTagName("div");
    for (let i = 0; i < divs.length; i++) {
        if (divs[i].classList.contains("clicked")) {
            divs[i].classList.remove("clicked");
        }
    }
}

async function movePiece(sourcePosition, targetPosition) {
    const data = {
        chessName: localStorage.getItem("name"),
        source: sourcePosition,
        target: targetPosition
    };

    const option = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    };

    const response = await fetch(basePath + "/api/games/pieces", option)
    .then(res => res.json());
    console.log(response.body);

    if (response.statusCode === 400) {
        alert(response.message);
        return;
    }
    await loadGame();
}

