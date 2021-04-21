const button = document.querySelector("button");
let roomList = document.querySelector(".roomlist");

//button.addEventListener("click", makeRoom);
//roomList.addEventListener("click", moveRoom);

mainStart();

async function mainStart() {
    await showRoomList();
}

async function showRoomList() {
    const rooms = await fetch("/room"
    ).then(res => res.json());
    for (let i = 0; i < rooms.roomNames.length; i++) {
        let eachRoom = document.createElement("a");
        eachRoom.setAttribute("class", "room");
        eachRoom.setAttribute("href", "/room/" + rooms.roomNumbers[i]);
        eachRoom.textContent = rooms.roomNames[i];
        roomList.appendChild(eachRoom);
    }
}



