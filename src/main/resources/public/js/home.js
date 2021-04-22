let rooms;

const $roomFrame = document.getElementById("room-frame");

loadRoom();

function loadRoom() {
    fetch("/chess/room/list").then(response => {
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
        let title = (i + 1) + "번 방: " + rooms[i].roomName;
        document.getElementById("room" + (i + 1)).innerHTML = `
            <text class="room-title" id=${i + 1}>${title}</text>
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

    location.href = "https://localhost:8080/chess/enter?room=" + e.target.id
}

//$roomFrame.addEventListener("click", selectRoom);