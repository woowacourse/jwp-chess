let roomListData = [];
const btnCreateRoom = document.getElementById('btn-game-create')
const btnCreateUser = document.getElementById('btn-user-create')
const btnLogin = document.getElementById('btn-login')

refresh();
checkCookie()

function checkCookie() {
    const cookie = getCookie('user');
    if (cookie) {
        axios.get('/api/user/' + cookie)
            .then(function (response) {
                if (response.data.roomId !== 0) {
                    location.href = '/room/' + response.data.roomId;
                }
            }).catch(function (error) {
        });
    }
}

function refresh() {
    refreshTitle()
    getTotalRoom();
}

function refreshTitle() {
    const title = document.getElementById('title_text')
    const user = getCookie('user');
    if (user) {
        title.innerText = `${user}님 환영합니다.`
    } else {
        title.innerText = '로그인 해주세요.'
    }
}

function getTotalRoom() {
    axios.get('/api/rooms')
        .then(function (response) {
            refreshRoomList(response.data)
        }).catch(function (error) {
            alert('방 정보를 갱신하지 못했습니다.');
    });
}

btnCreateRoom.addEventListener('click', function (e) {
    const name = prompt("방이름을 입력해 주세요.");
    const pw = prompt("비밀번호를 입력해 주세요");
    const cookieUser = getCookie('user');
    if (cookieUser === null) {
        console.log('로그인 먼저 해주세요.');
        return;
    }

    axios.post('/api/room', {
        "name": name,
        "pw": pw,
        "user": cookieUser
    }).then(function (response) {
        location.href = '/room/' + response.data.id;
    }).catch(function (error) {
        alert(error.response.data);
    });
});

btnCreateUser.addEventListener('click', function (e) {
    const name = prompt("계정을 입력해 주세요.");
    const pw = prompt("비밀번호를 입력해 주세요");

    axios.post('/api/user', {
        "name": name,
        "pw": pw
    }).then(function (response) {
        console.log('계정생성이 완료되었습니다.')
    }).catch(function (error) {
        alert('계정 만들지 못했습니다.');
    });
});

btnLogin.addEventListener('click', function (e) {
    let name = prompt("계정을 입력해 주세요.");
    let pw = prompt("비밀번호를 입력해 주세요");

    axios.post('/api/user/login', {
        "name": name,
        "pw": pw
    }).then(function (response) {
        setCookie('user', response.data.name, 720)
        refreshTitle()
    }).catch(function (error) {
        alert('로그인에 실패했습니다.');
    });
});

function refreshRoomList(data) {
    console.log(data);
    let list = document.getElementById("list-chess-game");
    roomListData = data;
    list.innerHTML = "";
    for (let i = 0; i < data.length; i++) {
        let room = data[i];
        if (room.isFull) {
            list.innerHTML += "<div class = box-chess-game data-idx = " + i + ">\n" +
                "<p class=box-chess-game-title> " + room.name + "</p>\n" +
                "</div>\n"
        } else {

            list.innerHTML += "<div class = box-chess-game data-idx = " + i + ">\n" +
                "<p class=box-chess-game-title> " + room.name + " (입장가능) </p>\n" +
                "</div>\n"
        }
    }

    const roomList = document.querySelectorAll('.box-chess-game');
    for (const room of roomList) {
        room.addEventListener('click', clickRoom);
    }
}


function clickRoom(event) {
    let idx = event.target.dataset.idx;
    if (idx === undefined) {
        idx = event.target.parentElement.dataset.idx;
    }
    let room = roomListData[idx];
    enterGame(room.id)
}

function enterGame(id) {
    const pw = prompt('방 비밀번호를 입력 해 주세요')
    axios.post('/api/room/' + id + '/enter', {
        "id": id,
        "user": getCookie('user'),
        "pw": pw
    })
        .then(function (response) {
            location.href = '/room/' + id;
        }).catch(function (error) {
        if (error.response.status === 400) {
            alert(error.response.data);
        } else {
            alert('게임을 로드 할 수 없습니다.');
        }
    });
}

function setCookie(name, value, min) {
    const exdate = new Date();
    exdate.setMinutes(exdate.getMinutes() + min);
    const cookie_value = escape(value) + ((min == null) ? '' : '; expires=' + exdate.toUTCString());
    document.cookie = name + '=' + cookie_value;

}

function getCookie(name) {
    const value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value ? value[2] : null;
}

function deleteCookie(name) {
    var temp = getCookie(name);
    if (temp) {
        setCookie(name, temp, (new Date(1)));
    }
}

