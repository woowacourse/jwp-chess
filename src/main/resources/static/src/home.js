const searchButton = document.querySelector("#search");
const createButton = document.querySelector("#create");
const roomList = document.querySelector("#roomList");

async function searchRooms () {
    let rooms = await fetch("/search");
    rooms = await rooms.json();
    print(rooms);
}

function print(rooms) {
    while(roomList.hasChildNodes()) {
        const room = document.querySelector(".room");
        roomList.removeChild(room);
    }
    rooms.forEach(function (value) {
        const room = document.createElement('li');
        room.id = value.id;
        room.className = "room";
        const roomRink = document.createElement('a');
        roomRink.innerText = value.name;
        roomRink.href = "/game/" + value.id;
        room.appendChild(roomRink);

        const roomDeleteButton = document.createElement('input');
        roomDeleteButton.className = 'roomDeleteButton'
        roomDeleteButton.type = 'button';
        roomDeleteButton.value = '❌';
        roomDeleteButton.addEventListener('click', function () {
            deleteRoom(value.id);
        })
        room.appendChild(roomDeleteButton);
        roomList.appendChild(room);
    })
}

async function deleteRoom(roomId) {
    const room = document.querySelector(".room");
    console.log(roomId);
    const deleteUrl = "delete/" + roomId;
    let rooms = await fetch(deleteUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: roomName.value,
            password: roomPassword.value,
        }),
    })
    rooms = await rooms.json();
    print(rooms);
}

function closeRooms() {
    while(roomList.hasChildNodes()) {
        const room = document.querySelector(".room");
        roomList.removeChild(room);
    }
}

async function createRoom() {
    var roomName = document.querySelector("#roomName");
    var roomPassword = document.querySelector("#roomPassword");
    if (roomName.value === "" || roomPassword.value === "") {
        alert("방이름과 방 비밀번호중 입력되지 않은 값이 있습니다.")
        return;
    }
    let rooms = await fetch("/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: roomName.value,
            password: roomPassword.value,
        }),
    }).then(
        roomName.value = "",
        roomPassword.value = ""
    )
    rooms = await rooms.json();
    searchButton.innerText = "Close";
    print(rooms);
}

searchButton.addEventListener("click", function () {
    if (searchButton.innerText === "Search") {
        searchRooms();
        searchButton.innerText = "Close"
        return;
    }
    closeRooms();
    searchButton.innerText = "Search";
});

createButton.addEventListener("click", function () {
    createRoom();
})
