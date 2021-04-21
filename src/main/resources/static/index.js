let roomListData = [];
const btnCreate = document.getElementById('btn-game-create')

getTotalRoom();

function getTotalRoom() {
    axios.get('/api/rooms')
        .then(function (response) {
            refreshRoomList(response.data)
        }).catch(function (error) {
        alert('방 정보를 갱신하지 못했습니다.');
    });
}

btnCreate.addEventListener('click', function (e) {
    let name = prompt("방이름을 입력해 주세요.");
    let pw = prompt("비밀번호를 입력해 주세요");
    axios.post('/api/room', {
        "name": name,
        "pw": pw
    }).then(function (response) {
        //refreshRoomList(response.data)
        window.location.reload()
    }).catch(function (error) {
        alert('방을 만들지 못했습니다.');
    });
})


function refreshRoomList(data) {
    let list = document.getElementById("list-chess-game");
    roomListData = data;
    list.innerHTML = "";
    for (let i = 0; i < data.length; i++) {
        let room = data[i];
        list.innerHTML += "<div class = box-chess-game data-idx = " + i + ">\n" +
            "<p class=box-chess-game-title> " + room.name + "</p>\n" +
            "</div>\n"
    }
    const roomList = document.querySelectorAll('.box-chess-game');
    for (const room of roomList) {
        room.addEventListener('click', enterGame);
    }
}

function enterGame(event) {
    let idx = event.target.dataset.idx;
    if (idx === undefined) {
        idx = event.target.parentElement.dataset.idx;
    }
    let room = roomListData[idx];
    location.href = '/room/' + room.id;
}

