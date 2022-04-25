fetchRoomList();

async function start() {
    const roomName = document.getElementById("roomNameInput").value;
    const password = document.getElementById("passwordInput").value;
    if (/^\s*$/.test(roomName)) {
        const error = document.getElementById("error");
        error.innerText = "방 이름을 입력하세요!";

        document.getElementById("roomNameInput").value = "";
        return;
    }

    if (/^\s*$/.test(password)) {
        const error = document.getElementById("error");
        error.innerText = "비밀번호를 입력하세요!";

        document.getElementById("password").value = "";
        return;
    }

    const res = await fetch("/rooms", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({roomName: `${roomName}`, password: `${password}`})
    });
    if (res.status === 201) {
        const location = res.headers.get("Location");
        const array = location.split("/");
        const roomId = array[array.length - 1];
        await fetch(`/rooms/${roomId}/pieces`, {method: "POST"});
        window.location.href = location;
        return;
    }
    document.getElementById("error").innerText = data.message;
}

async function fetchRoomList() {
    const res = await fetch("/rooms");
    const datas = await res.json();
    const container = document.getElementById("room_list");
    const containerHeight = Object.keys(datas).length * 60;
    container.style.height = `${containerHeight}px`;
    datas.forEach(data => {
        const argument = document.createElement("div");
        argument.className = "room_data";

        const link = document.createElement("a");
        link.className = "roomName";
        link.innerText = data.roomName;
        link.href = `/rooms/${data.roomId}`;
        argument.appendChild(link);

        const status = document.createElement("div");
        status.className = "roomStatus";
        status.innerText = data.gameStatus;
        argument.appendChild(status);

        const deleteButton = document.createElement("button");
        deleteButton.className = "deleteButton";
        deleteButton.innerText = "삭제";
        argument.appendChild(deleteButton);

        container.appendChild(argument);
    });
}
