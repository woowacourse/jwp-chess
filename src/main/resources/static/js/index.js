const roomsCombo = document.getElementById("rooms");
const roomName = document.getElementById("room-name");
const intoRoomButton = document.getElementById("into-room");
const deleteRoomButton = document.getElementById("delete-room");
const creatRoomButton = document.getElementById("create-room");

fetch('/api/rooms').then(res => res.json()).then(data => {
    roomSetting(data);
});

intoRoomButton.onclick = () => {
    if (roomsCombo.value === "") {
        alert("들어갈 방이 없습니다. 방을 생성하세요.");
        roomName.focus();
        return;
    }
    document.getElementById("start").action = '/room/' + roomsCombo.value;
    document.getElementById("start").submit();
};

deleteRoomButton.onclick = () => {
    if (roomsCombo.value === "") {
        alert("삭제할 방이 없습니다. 방을 만들어보면 어떨까요?");
        roomName.focus();
        return;
    }
    deleteRoom();

    function deleteRoom() {
        let roomId = roomsCombo.value;
        fetch("/api/room", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                roomId
            })
        }).then(res => res.json()).then(data => {
            initialRooms();
            roomSetting(data);
            roomName.value = "";
        })
    }
};

creatRoomButton.onclick = () => {
    fetch('/api/room', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            roomName: roomName.value, roomPassword: ''
        })
    }).then(res => res.json()).then(data => {
        initialRooms();
        roomSetting(data);
        roomName.value = "";
    })
};

function initialRooms() {
    for (let index = 0; index < roomsCombo.childElementCount;) {
        roomsCombo.removeChild(roomsCombo.children.item(index));
    }
}

function roomSetting(data) {
    for (key in data.rooms) {
        if (data.rooms.hasOwnProperty(key)) {
            let opt = document.createElement("option");
            opt.value = key;
            opt.textContent = "#" + key + " - " + data.rooms[key];
            roomsCombo.appendChild(opt);
        }
    }
}
