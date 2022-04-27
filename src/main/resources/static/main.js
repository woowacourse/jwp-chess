const API_HOST = "http://localhost:8080"

const X_AXES = ["a", "b", "c", "d", "e", "f", "g", "h"];
const Y_AXES = ["8", "7", "6", "5", "4", "3", "2", "1"];

let roomId = localStorage.getItem("roomId");

let from = null;
let to = null;

async function fetchAsGet(path) {
    const response = await fetch(`${API_HOST}/rooms/${roomId}/${path}`, {
        method: "GET"
    });

    return response.json();
}

async function fetchAsPost(path, body) {
    await fetch(`${API_HOST}/rooms/${roomId}/${path}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });
}

const fetchBoard = async () => {
    return fetchAsGet("board");
}

const fetchCurrentTurn = async () => {
    return fetchAsGet("turn");
}

const fetchScore = async () => {
    return fetchAsGet("score");
}

const fetchWinner = async () => {
    return fetchAsGet("winner");
}

const move = async (from, to) => {
    await fetchAsPost("move", {from, to})
    await render();
}

const renderBoard = async () => {
    clear();

    const board = await fetchBoard(roomId);
    let html = "";
    Y_AXES.forEach((yAxis) => {
        html += "<div class='row'>";

        X_AXES.forEach((xAxis) => {
            const coordinate = xAxis + yAxis;
            const pieceName = board[coordinate];
            if (pieceName !== undefined) {
                const imgTag = `<div class="column" onclick="handleClickTile('${coordinate}')"><img alt="chess-piece" src='images/${pieceName.toLowerCase()}.svg'/></div>`;
                html += imgTag;
            } else {
                const imgTag = `<div class="column" onclick="handleClickTile('${coordinate}')"></div>`;
                html += imgTag;
            }
        });

        html += "</div>";
    });

    document.getElementById("board").innerHTML = html;
}

const handleClickTile = async (coordinate) => {
    if (!from) {
        from = coordinate;
    } else if (!to) {
        to = coordinate;

        await move(from, to);
        from = null;
        to = null;
    }
}

const renderCurrentTurn = async () => {
    const currentTurn = await fetchCurrentTurn(roomId);

    let turn = "";
    if (currentTurn.pieceColor === "WHITE") {
        turn = "백";
    }

    if (currentTurn.pieceColor === "BLACK") {
        turn = "흑";
    }

    document.getElementById("turn").innerHTML = `현재차례: ${turn}`;
}

const renderScore = async () => {
    const score = await fetchScore(roomId);

    let html = "";
    html += `백: ${score.whiteScore}<br/>`;
    html += `흑: ${score.whiteScore}`;

    document.getElementById("score").innerHTML = html;
}

const renderWinner = async () => {
    try {
        const winner = await fetchWinner(roomId);

        if (winner.pieceColor) {
            let color;
            if (winner.pieceColor === "WHITE") {
                color = "백";
            } else {
                color = "흑";
            }

            document.getElementById("score").innerHTML = `승자는 ${color}`;
        }
    } catch (e) {
    }
}

const clear = () => {
    document.getElementById("board").innerHTML = "";
    document.getElementById("turn").innerHTML = "";
    document.getElementById("score").innerHTML = "";
    document.getElementById("winner").innerHTML = "";
}

const render = async () => {
    clear();
    await renderBoard();
    await renderCurrentTurn();
    await renderScore();
    await renderWinner();
}

window.onload = async () => {
    if (roomId !== "null") {
        await render();
    }
}

const createRoom = async () => {
    let title = prompt("방 제목 입력");
    let password = prompt("방 비밀번호 입력");

    await fetchAsPost(`rooms`, {title, password});
}

const enterRoom = async () => {
    roomId = prompt("방 아이디 입력");
    localStorage.setItem("roomId", roomId);
    await render();
}
