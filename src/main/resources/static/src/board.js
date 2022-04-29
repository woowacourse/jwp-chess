const score = document.getElementById("score");

function selectBoard(roomId) {
    window.location.href = `/room/${roomId}`;
}

async function insertBoard() {
    const roomId = document.getElementById("roomId").value;
    const res = await fetch(`/api/room/${roomId}/board`, {
        method: "post"
    });

    selectBoard(roomId);
}

async function updateBoard() {
    const roomId = document.getElementById("roomId").value;
    const res = await fetch(`/api/room/${roomId}/square`, {
        method: "put",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            from: document.getElementById("from").value,
            to: document.getElementById("to").value
        }),
    });

    if (!res.ok) {
        const data = await res.json();
        alert(data.message);
        return;
    }

    await selectGameIsFinish(roomId);
    selectBoard(roomId);
}

async function updateStateEnd() {
    const roomId = document.getElementById("roomId").value;
    const res = await fetch(`/api/room/${roomId}/stateEnd`, {
        method: "put"
    });

    if (!res.ok) {
        const data = await res.json();
        alert(data.message);
        return;
    }

    selectBoard(roomId);
}

async function selectGameIsFinish(roomId) {
    const res = await fetch(`/api/room/${roomId}/stateEnd`);

    const data = await res.json();
    if (data.winner != "NO_WINNER") {
        alert(data.winner + "이 승리했습니다.");
    }
}

async function selectStatus() {
    const roomId = document.getElementById("roomId").value;
    const res = await fetch(`/api/room/${roomId}/status`, {
        method: "get",
    });

    if (!res.ok) {
        const data = await res.json();
        alert(data.message);
        return;
    }
    const data = await res.json();
    score.innerText = `백: ${data.whiteScore}점 흑: ${data.blackScore}점`;
}
