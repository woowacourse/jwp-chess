function createGameButton() {
    const roomName = document.querySelector("#room-name");
    const password = document.querySelector("#room-password");

    if (!roomName.value || !password.value) {
        return alert('이름과 패스워드를 모두 입력해주세요');
    }

    if (roomName.value.length > 5) {
        return alert('이름은 다섯글자 이하로 입력해주세요.');
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
    JsonSender.setChessRoom(rooms);
}

const chessGameRoom = fetch("/chess-games", {
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

const JsonSender = {
    setChessRoom: function (rooms) {
        let tbody = document.querySelector(".tbody-list");

        let html = '';
        for(let key in rooms) {
            let id = rooms[key]['roomNumber'];
            let roomName = rooms[key]['roomName']['roomName'];
            html += '<tr>';
            html += '<td>' + id + '</td>';
            html += '<td onclick="play(' + id + ')">' + roomName + '</td>';
            html += '<td><button class="delete-room" onclick="deleteGameButton(' + id + ')">삭제하기</button></td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;
    }
}

function play(id) {
    window.location.replace("/chess-game/" + id);
}

async function deleteGameButton(id) {
    const password = prompt("비밀번호를 입력해주세요.");
    await confirmPassword(id, password);

    window.location.reload();
}

async function confirmPassword(id, password) {
    await fetch('/chess-game/' + id + '/password', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            password: password
        })
    })
    .then(handleDeleteErrors)
    .then(() => {
        fetch('/chess-game/' + id, {
            method: 'DELETE',
        });
        alert("방 삭제가 완료되었습니다.");
    })
    .catch(function (error) {
        alert(error.message);
    });
}

async function handleDeleteErrors(response) {
    if (response.status != 200) {
        throw Error("패스워드가 일치하지 않습니다. 다시 한번 확인해주세요.");
    }
    return response;
}