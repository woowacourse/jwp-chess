const searchButton = document.querySelector("#search");
const roomList = document.querySelector("#roomList");

async function searchRooms () {
    let rooms = await fetch("/search");
    rooms = await rooms.json();
    printSearchResultsBy(rooms);
}

function printSearchResultsBy(rooms) {
    rooms.forEach(function (value) {
        const room = document.createElement('li');
        room.innerText = value.name;
        roomList.appendChild(room);
    })
}

searchButton.addEventListener("click", function () {
    searchRooms();
});
