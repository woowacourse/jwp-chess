let from = "";
let turn = "";
let isStart = false;
let roomName = "";
let id;

showIndexPageElement();

function showIndexPageElement() {
    hideElement("create-form");
    hideElement("rooms");
    hideElement("whole-board");
}

async function showCreateForm() {
    showElementBlock("create-form");
    showElementInline("name-form");
    showElementInline("create-room");
    hideElement("showCreateForm");
    hideElement("rooms");
}

function showElementBlock(elementId) {
    document.getElementById(elementId).style.display = "block";
}

function showElementInline(elementId) {
    document.getElementById(elementId).style.display = "inline";
}

function hideElement(elementId) {
    document.getElementById(elementId).style.display = "none";
}

async function create() {
    let isCreated = false;

    hideElement("rooms")
    if (roomName === "") {
        roomName = document.getElementById("roomName").value;
    }
    await fetch("/rooms", {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        },
        body: "name=" + roomName + "&password=" + document.getElementById("roomPassword").value
    })
        .then(res => res.json())
        .then(data => {
            id = data;
            if (data.message) {
                alert("[ERROR]: " + data.message);
                isCreated = false;
                return
            }
            isCreated = true;
        })
        .then(() => document.getElementById("roomPassword").value = "")
        .then(() => document.getElementById("roomName").value = "")
        .then(() => roomName = "")

    if (isCreated !== true) {
        return;
    }
    showElementBlock("whole-board");
    hideElement("create-form");
    hideElement("index");
}

async function start() {
    let pieces;

    await fetch("/rooms/" + id, {
        method: "POST"
    })
        .then(res => res.json())
        .then(data => pieces = data)

    showElementBlock("whole-board");
    turn = pieces.turn;
    isStart = true;
    await printPieces(pieces.board);
    await printStatus();
}

async function newGame() {
    end()
    let status = document.getElementById("chess-status");
    let turnState = document.getElementById("turn-status");
    status.innerText = "";
    turnState.innerText = "";
    roomName = "";

    hideElement("whole-board");
    showElementBlock("index");
    showElementInline("showCreateForm");
    hideElement("create-form");
}

async function load() {
    let pieces;
    let response = await fetch("/rooms/" + id);

    if (response.status === 400) {
        const errorMessage = await response.json();
        alert("[ERROR] " + errorMessage.message);
        return;
    }

    pieces = await response.json();

    turn = pieces.turn;
    if (turn === "empty") {
        let status = document.getElementById("chess-status");
        let turnStatus = document.getElementById("turn-status");
        status.innerText = "진행중인 게임이 없습니다.\n새 게임을 시작해주세요.";
        turnStatus.innerText = "";
        return;
    }
    printPieces(pieces.board);
    isStart = true;
    await printStatus();
}

function end() {
    let whiteSquares = document.getElementsByClassName("white-square");
    let blackSquares = document.getElementsByClassName("black-square");
    for (let square of whiteSquares) {
        removeChildren(square);
    }

    for (let square of blackSquares) {
        removeChildren(square);
    }
    isStart = false;

    let status = document.getElementById("chess-status");
    let turnState = document.getElementById("turn-status");
    status.innerText = "게임이 종료되었습니다. 새 게임을 눌러주세요.";
    turnState.innerText = "";
}


async function printStatus() {
    let stat;
    await fetch("/rooms/" + id + "/status")
        .then(res => res.json())
        .then(data => stat = data)
    let status = document.getElementById("chess-status");
    let turnState = document.getElementById("turn-status");

    let text = "백팀 :" + stat.whiteScore + "\n흑팀 :" + stat.blackScore;
    if (stat.whiteScore > stat.blackScore) {
        text += "\n백팀 우세!";
    } else if (stat.blackScore > stat.whiteScore) {
        text += "\n흑팀 우세!";
    }
    turnState.innerText = turn.toUpperCase() + "팀 차례입니다.";
    status.innerText = text;
}

function printPieces(pieces) {
    for (const key in pieces) {
        const piece = pieces[key];
        const square = document.getElementById(key);
        const img = document.createElement("img");
        removeChildren(square);
        attachPieceInSquare(piece, img, square);
    }

}

function attachPieceInSquare(piece, img, square) {
    if (piece !== "empty") {
        img.setAttribute("src", "/images/" + piece + ".png");
        img.setAttribute("class", "piece");
        square.appendChild(img);
    }
}

function removeChildren(node) {
    while (node.hasChildNodes()) {
        node.removeChild(node.firstChild);
    }
}

async function selectPiece(pieceDiv) {
    let pieceClasses = pieceDiv.classList;
    if (isStart === false) {
        return;
    }

    if (from === "") {
        from = pieceDiv.id;
        pieceClasses.add('selected');
        return;
    }

    if (from !== "") {
        let sourceClassList = document.getElementById(from).classList;
        sourceClassList.remove('selected');
        await move(from, pieceDiv.id)
    }
}

async function move(fromPosition, toPosition) {
    from = "";
    let response = await fetch("/rooms/" + id + "/move", {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            from: fromPosition,
            to: toPosition
        })
    })

    if (!response.ok) {
        const errorMessage = await response.json();
        await tempAlert("[ERROR] " + errorMessage.message, 500);
        return;
    }
    let pieces = await response.json();
    await changePieces(pieces.board, fromPosition, toPosition);
    const before = turn;
    turn = pieces.turn;
    await printStatus();
    if (turn === "empty") {
        isStart = false;
        alert(before.toUpperCase() + " 팀 승리!");
        let status = document.getElementById("chess-status");
        let turnStatus = document.getElementById("turn-status");
        status.innerText = before.toUpperCase() + " 팀이 승리했습니다.\n" +
            "새 게임 혹은 그만하기를 눌러주세요.";
        turnStatus.innerText = "";
    }
}

async function changePieces(pieces, fromPosition, toPosition) {
    const fromPiece = pieces[fromPosition];
    const toPiece = pieces[toPosition];
    const fromSquare = document.getElementById(fromPosition);
    const toSquare = document.getElementById(toPosition);
    const img = document.createElement("img");
    removeChildren(fromSquare);
    removeChildren(toSquare);
    attachPieceInSquare(toPiece, img, toSquare);
    attachPieceInSquare(fromPiece, img, fromSquare);
}

async function tempAlert(message, timeout)
{
    const width = 400;
    const height = 30;
    const x = window.innerWidth / 2 - (width / 2);
    const y = window.innerHeight / 2 - (height / 2);
    const alert = window.open('','',
        'width=' + width + ',height=' + height +
        ',left='+ x + ',top=' + y);
    alert.document.write(message)
    alert.focus()
    setTimeout(() => alert.close(), timeout)
}

async function rooms() {
    showElementBlock("rooms");
    showElementBlock("create-form");
    showElementInline("showCreateForm");
    hideElement("create-room");
    hideElement("name-form");
    let rooms;
    removeChildren(document.querySelector("tbody"));
    await fetch("/rooms", {
        method: "GET",
    })
        .then(res => res.json())
        .then(data => rooms = data)

    for (let index in rooms) {
        let tr = document.createElement("tr");
        for (let column in rooms[index]) {
            let td = document.createElement("td");
            td.innerHTML = rooms[index][column]
            tr.appendChild(td);
        }
        let roomId = rooms[index]["id"];
        let button = document.createElement("button");
        button.innerHTML = `<input type=\"button\" id=\"index/${roomId}\" class=\"chess-btn\" value=\"방 입장\" onclick=\"enterRoom(this)\">`
        button.innerHTML += `<input type=\"button\" id=\"delete/${roomId}\" class=\"chess-btn\" value=\"방 삭제\" onclick=\"deleteRoom(this)\">`
        tr.appendChild(button);
        document.querySelector("tbody").appendChild(tr);
    }
}

async function enterRoom(self) {
    id = self.id.split("/")[1];

    hideElement("rooms");
    await load();
    showElementBlock("whole-board")
}

async function deleteRoom(self) {
    id = self.id.split("/")[1];
    let response = await fetch("/rooms/" + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        body: "password=" + document.getElementById("roomPassword").value
    })

    if (!response.ok) {
        const errorMessage = await response.json();
        await tempAlert(errorMessage.message, 1000);
    }
}

