const roomTable = document.querySelector(".room-table");
const inputText = document.querySelector(".input-name");
const deleteButton = document.querySelector(".delete-button");
inputText.addEventListener('keyup', addRoom);
roomTable.addEventListener("click", clickRoomName);
deleteButton.addEventListener("click", deleteRoom);
roomTable.addEventListener("dblclick", joinRoom);

const MIN_ROOM_NAME_LENGTH = 1;
const MAX_ROOM_NAME_LENGTH = 10;

const SUCCEED_HTTP_CODE = 200;
const FAIL_HTTP_CODE = 400;

window.onload = function () {
    renderRooms();
}

function joinRoom(event) {
    if (event.target.closest("td")) {
        const roomName = encodeURI(getClickedRoom().textContent);
        window.location.href = `/enter/${roomName}`;
    }
}

async function deleteRoom() {
    const clickedRoom = getClickedRoom();
    if (clickedRoom) {
        await ansDeleteRoom(clickedRoom);
    }
}

async function ansDeleteRoom(clickedRoom) {
    if (!confirm(clickedRoom.textContent + " 방을 삭제하시겠습니까?")) {
        return;
    }

    let data = {
        roomName: clickedRoom.textContent
    }
    await fetch('/deleteRoom/', {
        method: 'delete',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    renderRooms();
}

function clickRoomName(event) {
    if (event.target.closest("td")) {
        const clickRoom = event.target.closest("td");
        const clickedRoom = getClickedRoom();

        toggleClicked(clickedRoom);
        clickRoom.classList.toggle("clicked");
        clickRoom.classList.add("clickedRoomName");
    }
}

function toggleClicked(clickedRoom) {
    if (clickedRoom) {
        clickedRoom.classList.remove("clickedRoomName");
        clickedRoom.classList.toggle("clicked");
    }
}

function getClickedRoom() {
    const room = document.getElementsByTagName("td");

    for (let i = 0; i < room.length; i++) {
        if (room[i].classList.contains("clicked")) {
            return room[i];
        }
    }
    return null;
}

async function addRoom(event) {
    const roomName = event.target.value;
    if (event.key === "Enter" && roomName !== "") {
        await executeAddRoom(roomName, event);
    }
}

async function requestAddRoom(roomName) {
    let data = {
        roomName: roomName
    }
    let response = await fetch('/checkRoomName', {
        method: 'post',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    return response;
}

async function executeAddRoom(roomName, event) {
    if (validateRoomNameLength(roomName)) {
        alert("방 이름은 한 글자 이상 열 글자 이하여야 합니다.")
        return;
    }
    let response = await requestAddRoom(roomName);
    await executeByStatus(response, roomName);
    event.target.value = '';
    renderRooms();
}

async function executeByStatus(response, roomName) {
    const status = response.status;

    if (status === SUCCEED_HTTP_CODE) {
        response = await response.json();
        createRoom(roomName);
        alert(response.message);
    }

    if (status === FAIL_HTTP_CODE) {
        response = await response.text();
        alert(response);
    }
}

function validateRoomNameLength(roomName) {
    return MIN_ROOM_NAME_LENGTH >= roomName.length && MAX_ROOM_NAME_LENGTH < roomName.length;
}

async function renderRooms() {
    initList();
    roomTable.insertAdjacentHTML("beforeend", `<tr><th>방 이름</th></tr>`);
    const response = await fetch('/rooms')
        .then(res => {
            return res.json();
        });

    for (let i = 0; i < response.length; i++) {
        renderRoom(response[i]);
    }
}

async function createRoom(roomName) {
    let data = {
        roomName: roomName
    }
    await fetch('/createRoom', {
        method: 'post',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

function initList() {
    while (roomTable.hasChildNodes()) {
        roomTable.removeChild(roomTable.firstChild);
    }
}

function renderRoom(roomName) {
    roomTable.insertAdjacentHTML("beforeend", `<tr><td class="room-name">${roomName}</td></tr>`);
}
