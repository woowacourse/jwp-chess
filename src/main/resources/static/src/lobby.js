const defaultPath = "/api/rooms";
let roomId = null;

window.onload = async function () {
    const res = await fetch(defaultPath, {method: "GET"});
    const data = await res.json();
    loadRooms(data);
}

function loadRooms(data) {
    const ul = document.getElementsByClassName("room-list-group")[0];
    for (let i = 0; i < data.length; i++) {
        const roomListLi = generateRoomListLi(data[i].id);

        const roomNameA = generateRoomNameA(data[i].name, data[i].id);
        roomListLi.appendChild(roomNameA);

        const statusSpan = generateStatusSpan(data[i].status);
        roomListLi.appendChild(statusSpan);

        const deleteBtn = generateDeleteBtn();
        roomListLi.appendChild(deleteBtn);

        ul.appendChild(roomListLi);
    }
}

function generateRoomListLi(id) {
    const li = document.createElement("li");
    li.className = "room-list";
    li.id = id;
    return li;
}

function generateRoomNameA(name, id) {
    const a = document.createElement("a");
    a.className = "name";
    a.innerText = name;
    a.href = `/room/${id}`;
    return a;
}

function generateStatusSpan(status) {
    const span = document.createElement("span");
    span.className = "status";
    span.innerText = status;
    return span;
}

function generateDeleteBtn() {
    const deleteBtn = document.createElement("button");
    deleteBtn.className = "delete-btn";
    deleteBtn.innerText = "삭제"
    deleteBtn.addEventListener('click', openDeleteModal);
    return deleteBtn;
}

function openCreateModal() {
    const modal = document.getElementById("create-modal");
    modal.style.display = 'block';
}

function closeCreateModal() {
    document.getElementById("create-input-name").value = "";
    document.getElementById("create-input-password").value = "";
    document.getElementById("create-modal-error").innerText = "";

    const modal = document.getElementById("create-modal");
    modal.style.display = 'none';
}

function openDeleteModal(event) {
    roomId = event.target.parentElement.id;

    const name = document.getElementById("delete-modal-name");
    name.innerText = event.target.parentElement.firstChild.textContent;

    const modal = document.getElementById("delete-modal");
    modal.style.display = 'block';
}

function closeDeleteModal() {
    document.getElementById("delete-input-password").value = "";
    document.getElementById("delete-modal-error").innerText = "";

    const modal = document.getElementById("delete-modal");
    modal.style.display = 'none';
}

async function createRoom() {
    const name = document.getElementById("create-input-name").value;
    const password = document.getElementById("create-input-password").value;

    if (/^\s*$/.test(name)) {
        const error = document.getElementById("create-modal-error");
        error.innerText = "방 이름을 입력하세요.";
        document.getElementById("create-input-name").value = "";
        document.getElementById("create-input-password").value = "";
        return;
    }

    if (/^\s*$/.test(password)) {
        const error = document.getElementById("create-modal-error");
        error.innerText = "비밀번호를 입력하세요.";
        document.getElementById("create-input-name").value = "";
        document.getElementById("create-input-password").value = "";
        return;
    }

    const res = await fetch("/api/rooms", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify({
            name: `${name}`,
            password: `${password}`
        })
    });
    const data = await res.json();
    if (!res.ok) {
        const data = await res.json();
        const error = document.getElementById("create-modal-error");
        error.innerText = data.message;
        document.getElementById("input-room-name").value = "";
        document.getElementById("create-input-password").value = "";
        return;
    }

    window.location.href = `/room/${data.id}`;
}

async function deleteRoom() {
    const password = document.getElementById("delete-input-password").value;

    if (/^\s*$/.test(password)) {
        const error = document.getElementById("delete-modal-error");
        error.innerText = "비밀번호를 입력하세요.";
        document.getElementById("delete-input-password").value = "";
        return;
    }

    const res = await fetch(`/api/rooms/${roomId}`, {
        headers: {
            'Content-Type': 'application/json'
        },
        method: "DELETE",
        body: JSON.stringify({
            password: `${password}`
        })
    });
    if (!res.ok) {
        const data = await res.json();
        const error = document.getElementById("delete-modal-error");
        error.innerText = data.message;

        document.getElementById("delete-input-password").value = "";
        return;
    }
    window.location.href = "/";
}
