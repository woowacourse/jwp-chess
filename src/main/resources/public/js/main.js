document.getElementById("new-game").addEventListener("click", onNewGame);
document.getElementById("continue").addEventListener("click", onContinue);

const POST = {
    "method": 'POST',
    "headers": {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
}

function getCookie(name) {
    return document.cookie.split("; ").find(row => row.startsWith(name)).split("=")[1];
}

const moveToChessView = function (response) {
    window.location.href = response.headers.get('location');
}

async function onNewGame() {
    const response = await fetch('/api/chess', POST);
    moveToChessView(response);
}

async function onContinue() {
    if (!document.cookie.includes("chessId")) {
        alert("진행 중인 게임이 없습니다.");
        return;
    }

    const chessId = getCookie("chessId");
    const response = await fetch("/api/chess/" + chessId);
    console.log(response);
    const data = await response.json();
    if (!data.status.includes("RUNNING")) {
        alert("진행 중인 게임이 없습니다.");
        return;
    }

    moveToChessView(response);
}
