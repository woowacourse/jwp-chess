const roomTable = document.querySelector(".room-table");
const inputText = document.querySelector(".input-name");
const deleteButton = document.querySelector(".delete-button");
inputText.addEventListener('keyup', addRoom);
roomTable.addEventListener("click", clickRoomName);
deleteButton.addEventListener("click", deleteRoom);
roomTable.addEventListener("dblclick", joinRoom);

window.onload = function () {
    renderRooms();
}

async function renderRooms() {
    initList();
    roomTable.insertAdjacentHTML("beforeend", `<tr><th>방 이름</th></tr>`);
    const response = await fetch('/rooms', {
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        return res.json();
    })

    for (let i = 0; i < response.length; i++) {
        renderRoom(response[i]);
    }
}

function initList() {
    while (roomTable.hasChildNodes()) {
        roomTable.removeChild(roomTable.firstChild);
    }
}

function renderRoom(roomName) {
    roomTable.insertAdjacentHTML("beforeend", `<tr><td class="room-name">${roomName}</td></tr>`);
}

function joinRoom(event) {
    if (event.target.closest("td")) {
        const roomName = encodeURI(getClickedRoom().textContent);
        window.location.href = `/rooms/${roomName}`;
    }
}

async function deleteRoom() {
    const clickedRoom = getClickedRoom();
    if (clickedRoom) {
        if (!confirm(clickedRoom.textContent + " 방을 삭제하시겠습니까?")) {
            return;
        }
        let roomName = decodeURI(clickedRoom.textContent);

        await fetch('/rooms/' + roomName, {
            method: 'delete',
            headers: {
                'Content-Type': 'application/json'
            }
        });
    }
    renderRooms();
}

function clickRoomName(event) {
    if (event.target.closest("td")) {
        const clickRoom = event.target.closest("td");
        const clickedRoom = getClickedRoom();

        if (clickedRoom) {
            clickedRoom.classList.remove("clickedRoomName");
            clickedRoom.classList.toggle("clicked");
        }
        clickRoom.classList.toggle("clicked");
        clickRoom.classList.add("clickedRoomName");
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
        if (0 > roomName.length || 10 < roomName.length) {
            alert("방 이름은 한 글자 이상 열 글자 이하여야 합니다.")
            return;
        }

        const response = await fetch('/rooms/' + roomName + '/check', {
            method: 'get',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            return res.json();
        });
        if (response.code === "SUCCEED") {
            createRoom(roomName);
        }
        alert(response.message);
        event.target.value = '';
        renderRooms();
    }
}

async function createRoom(roomName) {
    let data = {
        roomName: roomName
    }
    await fetch('/rooms', {
        method: 'post',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}
