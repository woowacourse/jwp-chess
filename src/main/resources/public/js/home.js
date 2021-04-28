let rooms;

const $roomFrame = document.getElementById("room-frame");

loadRoom();

function loadRoom() {
    fetch("/chess/api/room/list").then(response => {
        if (response.ok) {
            return response.json();
        }
    }).then(data => {
        rooms = data;
        createRoom();
    });
}

function createRoom() {
    for (let i = 0; i < rooms.length ; i++) {
        let title = rooms[i].roomId + "번 방: " + rooms[i].roomName;
        document.getElementById("room" + (i + 1)).innerHTML = `
            <text class="room-title" id=${rooms[i].roomId}>${title}</text>
        `
        if (i === 7) {
            break;
        }
    }
}

function selectRoom(e) {
    if (e.target.className !== "room-title") {
        return;
    }

    location.href = "http://localhost:8080/chess/enter?room=" + e.target.id
}

document.addEventListener("click", selectRoom);