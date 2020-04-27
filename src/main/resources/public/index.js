window.onload = function () {
    getRooms();
}

function getRooms() {
    fetch("/api/rooms")
        .then(async (res) => {
            if (res.status !== 200) {
                console.log(res);
            }
            bindRooms(await res.json());
        })
}

function bindRooms(data) {
    for (let room in data) {
        const div = document.createElement("div");
        div.setAttribute("class", "room");
        div.innerText = data[room].name;

        document.getElementsByClassName("room-container")[0].appendChild(div);
    }
}