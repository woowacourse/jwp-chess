window.onload = async function () {
    await selectRoomAll();
}

async function selectRoomAll() {
    const res = await fetch('/api/room/all');
    const data = await res.json();
    if (!res.ok) {
        const data = await res.json();
        alert(data.message);
    }
    await clearRenderRooms();
    await renderRooms(data)
}

async function clearRenderRooms() {
    const rooms = document.getElementById("rooms");
    while (rooms.hasChildNodes()) {
        rooms.removeChild(rooms.firstChild);
    }
}

async function renderRooms(data) {
    const rooms = document.getElementById("rooms");

    for (let i = 0; i < data.rooms.length; i++) {
        const roomDiv = document.createElement("div");
        roomDiv.id = data.rooms[i].id;
        roomDiv.classList.add("room");

        const titleSpan = document.createElement("button");
        titleSpan.className = "titleRoom";
        titleSpan.innerText = data.rooms[i].title;
        titleSpan.addEventListener("click", onclick);
        roomDiv.appendChild(titleSpan);

        const deleteButton = document.createElement("button");

        deleteButton.className = "deleteButton";
        deleteButton.innerText = "삭제"
        deleteButton.addEventListener("click", deleteRoom);
        roomDiv.appendChild(deleteButton);

        const admittedButton = document.createElement("button");

        admittedButton.className = "admittedButton";
        admittedButton.innerText = "입장"
        admittedButton.addEventListener("click", selectBoard);
        roomDiv.appendChild(admittedButton);

        rooms.appendChild(roomDiv);
    }
}

async function selectBoard() {
    const roomId = this.parentElement.id;
    window.location.href = `/room/${roomId}`;
}

async function insertRoom() {
    const title = prompt("방 제목을 입력하세요");
    const password = prompt("비밀번호를 입력하세요");

    const res = await fetch('/api/room', {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            title: title,
            password: password
        })
    });

    if (!res.ok) {
        const data = await res.json();
        alert(data.message);
    }
    await selectRoomAll();
}

async function deleteRoom() {
    const roomId = this.parentElement.id;
    const password = prompt("비밀번호를 입력하세요");
    const res = await fetch(`/api/room/${roomId}`, {
        method: "delete",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            password: password
        })
    });
    if (!res.ok) {
        const data = await res.json();
        alert(data.message);
        return;
    }
    await selectRoomAll();
}
