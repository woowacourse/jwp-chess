function createGameButton() {
    const roomName = document.querySelector("#room-name");
    const password = document.querySelector("#room-password");

    if (!roomName.value || !password.value) {
        return alert('이름과 패스워드를 모두 입력해주세요');
    }

    fetch('/chess-game', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            roomName: roomName.value,
            password: password.value
        })
    })
    .then(handleCreatedErrors)
    .then(r=>r.json())
    .then(data => {
        roomName.value = "";
        password.value = "";
        alert("방 생성이 완료되었습니다.");
        location.reload();
        return data;
    })
    .catch(function (error) {
        alert(error.message);
    });
}

window.onload = async () => {
    const rooms = await chessGameRoom;
    console.log("rooms: " + rooms);

    JsonSender.setChessRoom(rooms);
}

const chessGameRoom = fetch("/chess-game", {
    method: "GET"
})
.then(r=>r.json())
.then(data => {
    return data;
});

async function handleCreatedErrors(response) {
    if (response.status != 201) {
        throw Error("방 생성이 실패하였습니다.");
    }
    return response;
}

const start = function () {
    window.location.replace("/start");
}

const play = function () {
    window.location.replace("/play");
}

const JsonSender = {
    setChessRoom: function (rooms) {
        let data = rooms;
        let tbody = document.querySelector(".tbody-list");

        let html = '';
        for(let roomNumber in data) {
            let roomName = data[roomNumber]['roomName']['roomName'];
            console.log('key:' + roomNumber + ' / ' + 'value:' + roomName);
            html += '<tr>';
            html += '<td>' + roomNumber + '</td><td>' + roomName + '</td>';
            html += '<td><button class="delete-room" onclick="deleteGameButton()">삭제하기</button></td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;
    }
}

function deleteGameButton() {

}