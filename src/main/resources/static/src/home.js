const searchButton = document.querySelector("#search");
const createButton = document.querySelector("#create");
const roomList = document.querySelector("#roomList");

async function searchRooms () {
    let rooms = await fetch("/search");
    rooms = await rooms.json();
    printSearchResultsBy(rooms);
}

function printSearchResultsBy(rooms) {
    rooms.forEach(function (value) {
        const room = document.createElement('li');
        room.className = "room";
        const roomRink = document.createElement('a');
        roomRink.innerText = value.name;
        roomRink.href = "/game/" + value.id;
        room.appendChild(roomRink);
        roomList.appendChild(room);
    })
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
    await fetch("/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: roomName.value,
            password: roomPassword.value,
        }),
    })
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
