const roomTable = document.querySelector(".room-table");
const inputText = document.querySelector(".input-name");
inputText.addEventListener('keyup', addRoom);

renderRooms();

async function addRoom(event){
    const roomName = event.target.value;
    if (event.key === "Enter" && roomName !== "") {
        if(0 > roomName.length && 10 < roomName.length){
            alert("방 이름은 한 글자 이상 열 글자 이하여야 합니다.")
            return;
        }
        let data = {
            roomName:roomName
        }
        const response = await fetch('/checkRoomName', {
            method: 'post',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            return res.json();
        });
        if(response.code === "200") {
            createRoom(roomName);
        }
        alert(response.message);
        event.target.value = '';
    }
    renderRooms();
}

async function renderRooms() {
    initList();
    roomTable.insertAdjacentHTML("beforeend", `<tr><th>방 이름</th></tr>`);
    const response = await fetch('/rooms', {
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        return res.json();
    });

    for (let i = 0; i < response.length; i++) {
        renderRoom(response[i]);
    }
}

async function createRoom(roomName) {
    let data = {
        roomName:roomName
    }
    await fetch('/createRoom', {
        method: 'post',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        return res.json();
    });
}

function initList() {
    while(roomTable.hasChildNodes()) {
        roomTable.removeChild(roomTable.firstChild);
    }
}
function renderRoom(roomName) {
    roomTable.insertAdjacentHTML("beforeend", `<tr><td>${roomName}</td></tr>`);
}
