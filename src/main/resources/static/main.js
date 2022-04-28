const API_HOST = "http://localhost:8080"

const X_AXES = ["a", "b", "c", "d", "e", "f", "g", "h"];
const Y_AXES = ["8", "7", "6", "5", "4", "3", "2", "1"];

let from = null;
let to = null;

async function fetchAsGet(path) {
    const response = await fetch(`${API_HOST}/${path}`, {
        method: "GET"
    });

    return response.json();
}

const fetchBoard = async (id) => {
    return fetchAsGet("board/" + id);
}

const fetchCurrentTurn = async (id) => {
    return fetchAsGet("turn/" + id);
}

const fetchScore = async (id) => {
    return fetchAsGet("score/" + id);
}

const fetchWinner = async (id) => {
    return fetchAsGet("winner/" + id);
}

const move = async (from, to, id) => {
    await fetch("move/" + id, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({from, to})
    });
    
    await render(id);
}

const renderBoard = async (id) => {
    clear();

    const board = await fetchBoard(id);
    let html = "";
    Y_AXES.forEach((yAxis, i) => {
        html += "<div class='row'>";

        X_AXES.forEach((xAxis, j) => {
            const coordinate = xAxis + yAxis;
            const pieceName = board[coordinate];
            if (pieceName !== undefined) {
                const imgTag = `<div class="column" onclick="handleClickTile('${coordinate}')"><img src='images/${pieceName.toLowerCase()}.svg'/></div>`;
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

        await move(from, to, parseQueryString().id);
        from = null;
        to = null;
    }
}

const renderCurrentTurn = async (id) => {
    const currentTurn = await fetchCurrentTurn(id);

    let turn = "";
    if (currentTurn.pieceColor === "WHITE") {
        turn = "백";
    }

    if (currentTurn.pieceColor === "BLACK") {
        turn = "흑";
    }

    document.getElementById("turn").innerHTML = `현재차례: ${turn}`;
}

const renderScore = async (id) => {
    const score = await fetchScore(id);

    let html = "";
    html += `백: ${score.white}<br/>`;
    html += `흑: ${score.black}`;

    document.getElementById("score").innerHTML = html;
}

const renderWinner = async (id) => {
    try {
        const winner = await fetchWinner(id);

        if (winner.pieceColor) {
            let color = "";
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

const render = async (id) => {
    clear();
    await renderBoard(id);
    await renderCurrentTurn(id);
    await renderScore(id);
    await renderWinner(id);
}

window.onload = async () => {
    const id = parseQueryString().id;
    await render(id);
}

const parseQueryString = () => {
    const params = {};
    console.log(window.location.search);
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        (str, key, value) => {
            params[key] = value;
        }
    );
    return params;
}
