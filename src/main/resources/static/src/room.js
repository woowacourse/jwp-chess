const roomId = location.pathname.split("/")[2];
let from = "";
let to = "";
let status = "";

window.onload = function () {
    addClickEventToChessBoard();
    loadData();
}

function addClickEventToChessBoard() {
    chessBoard.addEventListener("click", ({target: {id}}) => {
        if (from === "") {
            from = id;
        } else if (to === "") {
            to = id;
            move();
            from = "";
            to = "";
        }
    });
}

async function loadData() {
    const status = await loadRoom();
    if (status !== "ready") {
        loadScores();
    }
    loadPieces();
}

async function loadRoom() {
    const res = await fetch(`/api/rooms/${roomId}`);
    const data = await res.json();
    status = data.status;
    setNameAndStatus(data.name, data.status);
    setTurn(data.turn);
    return data.status;
}

function setNameAndStatus(name, status) {
    const nameDiv = document.getElementsByClassName("name")[0];
    nameDiv.innerText = name + " (" + status + ")";
}

function setTurn(turn) {
    document.getElementById("white-turn-image").style.boxShadow = "none";
    document.getElementById("black-turn-image").style.boxShadow = "none";
    if (status === "end" || status === "king_die") {
        return;
    }
    const currentTurn = document.getElementById(turn.toLowerCase() + "-turn-image");
    currentTurn.style.boxShadow = "0 0 10px 2px mediumpurple";
}

async function move() {
    document.getElementById("error").innerText = "";

    const res = await fetch(`/api/rooms/${roomId}/pieces`, {
        headers: {
            'Content-Type': 'application/json'
        },
        method: "PATCH",
        body: JSON.stringify({from: `${from}`, to: `${to}`})
    });
    if (!res.ok) {
        const data = await res.json();
        document.getElementById("error").innerText = data.message;
    }
    loadData();
}

async function endGame() {
    let res = await fetch(`/api/rooms/${roomId}/end`, {method: "PATCH"});

    if (!res.ok) {
        const data = await res.json();
        document.getElementById("error").innerText = data.message;
        return;
    }

    res = await fetch(`/api/rooms/${roomId}/scores`);
    const data = await res.json();
    if (!res.ok) {
        document.getElementById("error").innerText = data.message;
        return;
    }

    loadData();
}

function exitRoom() {
    window.location.href = "/";
}

async function loadPieces() {
    clearBoard();

    const res = await fetch(`/api/rooms/${roomId}/pieces`);
    const datas = await res.json();

    datas.forEach(data => {
        const element = document.getElementById(data.position);
        const img = document.createElement("img");
        img.src = `/images/${data.type}_${data.color.toLocaleLowerCase()}.png`;
        img.id = `${data.position}`;
        img.alt = "piece";
        img.className = "piece_img";
        element.appendChild(img);
    });
}

async function loadScores() {
    const res = await fetch(`/api/rooms/${roomId}/scores`);
    const data = await res.json();
    if (!res.ok) {
        document.getElementById("error").innerText = data.message;
        return;
    }

    document.getElementById('whiteScore').innerText = `${data.whiteScore}점`;
    document.getElementById('blackScore').innerText = `${data.blackScore}점`;

    document.getElementsByClassName("draw_text")[0].style.visibility = "hidden";
    document.getElementById("white_win").style.visibility = "hidden";
    document.getElementById("black_win").style.visibility = "hidden";

    let winColor = "";
    if (data.whiteScore > data.blackScore) {
        winColor = "white";
    } else if (data.whiteScore < data.blackScore) {
        winColor = "black";
    } else {
        winColor = "draw";
    }
    let element = document.getElementById(winColor);
    if (winColor !== "draw") {
        element = document.getElementById(winColor + "_win");
    }
    element.style.visibility = "visible";
}

function clearBoard() {
    const chessBoard = document.getElementById("chessBoard");
    for (let i = 0; i < chessBoard.children.length; i++) {
        chessBoard.children[i].innerHTML = `<div id=${chessBoard.children[i].id}></div>`
    }
}
