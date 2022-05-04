fetchRooms(1);

async function createRoom() {
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
    const data = await res.json();
    document.getElementById("error").innerText = data.message;
}

async function previousPage() {
    let currentPage = document.getElementById("current_page").innerText;
    fetchRooms(--currentPage);
    document.getElementById("current_page").innerText = currentPage;
}

async function nextPage() {
    let currentPage = document.getElementById("current_page").innerText;
    fetchRooms(++currentPage);
    document.getElementById("current_page").innerText = currentPage;
}

async function fetchRooms(page) {
    const res = await fetch(`/rooms?size=10&page=${page}`);
    const datas = await res.json();
    const container = document.getElementById("room_list");
    const containerHeight = datas.size * 60;
    container.style.height = `${containerHeight}px`;
    while (container.hasChildNodes()) {
        container.removeChild(container.firstChild);
    }
    datas.rooms.forEach(data => {
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
        deleteButton.id = `${data.roomId}`
        deleteButton.innerText = "삭제";
        deleteButton.addEventListener("click", requestDelete);
        argument.appendChild(deleteButton);

        container.appendChild(argument);
    });
}

async function requestDelete({target: {id}}) {
    const password = prompt("비밀번호를 입력해 주세요.");

    const res = await fetch("/rooms", {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({roomId: `${id}`, password: `${password}`})
    });

    const data = await res.json();

    if (!res.ok) {
        const error = document.getElementById("error");
        error.innerText = data.message;
    }
}
