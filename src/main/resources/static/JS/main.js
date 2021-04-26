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
    await fetch('/api/rooms', {
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        return res.json();
    }).then(json => {
        for (let i = 0; i < json.length; i++) {
            renderRoom(json[i]);
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

        let response = await fetch('/api/rooms/' + roomName, {
            method: 'delete',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        let status = response.status;
        if (status === 204) {
            alert("삭제가 완료되었습니다.");
        }
    }
    await renderRooms();
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
        let data = {
            roomName: roomName
        }
        let response = await fetch('/api/rooms', {
            method: 'post',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        });

        let status = response.status;
        event.target.value = '';
        response = await response.text();
        if (status === 201 || status === 500) {
            alert(response);
        }
        await renderRooms();
    }
}
