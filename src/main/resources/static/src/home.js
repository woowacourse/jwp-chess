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
        const roomRink = document.createElement('a');
        roomRink.innerText = value.name;
        roomRink.href = "/game/" + value.id;
        room.appendChild(roomRink);
        roomList.appendChild(room);
    })
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
    })
}

searchButton.addEventListener("click", function () {
    searchRooms();
});

createButton.addEventListener("click", function () {
    createRoom();
})
