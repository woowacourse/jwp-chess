const button = document.querySelector("button");
let roomList = document.querySelector(".roomlist");

button.addEventListener("click", makeRoom);

mainStart();

async function mainStart() {
    await showRoomList();
}

async function showRoomList() {
    const rooms = await fetch("/rooms"
    ).then(res => res.json());
    for (let i = 0; i < rooms.roomNames.length; i++) {
        const divRoom = document.createElement("div");
        let eachRoom = document.createElement("a");
        eachRoom.setAttribute("class", "room");
        eachRoom.setAttribute("href", "/rooms/" + rooms.roomNumbers[i]);
        eachRoom.textContent = rooms.roomNames[i];
        divRoom.appendChild(eachRoom);
        roomList.appendChild(divRoom);
    }
}

async function makeRoom() {
    const roomName = document.querySelector("#roomName").value;
    const data = {
        roomName: roomName
    }
    await fetch("/board", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    location.reload();
}

