const score = document.getElementById("score");

function selectGame() {
    window.location.href = "/"
}

async function insertGame() {
    const res = await fetch('/game', {
       method: "post"
    });
    const data = await res.json();
    if (!res.ok) {
        alert(data.message);
        return;
    }
    selectGame()
}

async function updateBoard() {
    const res = await requestMove();
    const data = await res.json();
    if (!res.ok) {
        alert(data.message);
        return;
    }
    selectGame();
}

async function requestMove() {
    return await fetch("/game/board", {
        method: "put",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            from: document.getElementById("from").value,
            to: document.getElementById("to").value
        }),
    });
}

async function deleteGame() {
    const res = await fetch('/game', {
        method: "delete"
    });
    const data = await res.json();
    if (!res.ok) {
        alert(data.message);
        return;
    }
    selectGame()
}

async function selectStatus() {
    const res = await fetch('/game/status', {
        method: 'get'
    });

    const data = await res.json();
    if (!res.ok) {
        alert(data.message);
        return;
    }
    score.innerText = `백: ${data.whiteScore}점 흑: ${data.blackScore}점`;
}
