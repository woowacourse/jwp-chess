import {getData, postData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = function () {
    const newGameButton = document.querySelector(".new-game");
    const loadGameButton = document.querySelector(".load-game")
    const registerUserButton = document.querySelector(".register-user");

    newGameButton.addEventListener("click", startNewGame);
    loadGameButton.addEventListener("click", loadGame);
    registerUserButton.addEventListener("click", createUser);
}

async function startNewGame(e) {
    const roomcnt = await getData(`${url}/games/roomcnt`)
    if(roomcnt["count"] >= 10) {
        alert("더이상 방을 만들 수 없습니다.")
        return;
    }

    const roomName = prompt("방 제목을 입력하세요");

    try {
        await validateRoomName(roomName);
    } catch (e) {
        return;
    }
    const whiteUserName = prompt("흰색 유저 이름을 입력하세요.");
    try {
        validateName(whiteUserName);
    } catch (e) {
        return;
    }

    const blackUserName = prompt("검정색 유저 이름을 입력하세요.");
    try {
        validateName(blackUserName);
    } catch (e) {
        return;
    }

    if (whiteUserName === blackUserName) {
        alert("같은 유저를 입력하였습니다");
        throw Error("같은 유저를 입력하였습니다.");
    }

    try {
        const host = await getExistentUser(whiteUserName);
        const guest = await getExistentUser(blackUserName);
        if (isEmptyObject(host) || isEmptyObject(guest)) {
            alert("존재하지 않는 유저가 있습니다.");
            return;
        }
        await createGame(roomName, host["id"], guest["id"]);
    } catch (e) {
        alert(e);
    }
}

async function getExistentUser(userName) {
    const params = {
        name: userName
    }
    return await getData(`${url}/users`, params);
}

async function validateRoomName(roomName) {
    if (roomName.length === 0) {
        throw Error("방 제목을 입력하세요");
    }

    if (roomName.length > 10) {
        alert("10자 이하로 입력하세요")
        throw Error("10자 이하로 입력하세요")
    }

    const params = {
        name: roomName
    }
    const isChk = await getData(`${url}/games/rooms/check`, params);
    if (isChk === 1) {
        alert("이미 존재하는 방 이름입니다");
        throw Error("존재하는 방이름 입니다.");
    }

}


function validateName(name) {
    if (name.length === 0) {
        alert("이름을 입력해주세요");
        throw Error("이름을 입력하지 않았습니다.");
    }
}

async function createUser(e) {
    const userName = prompt("아이디를 입력하세요. ");
    const body = {
        name: userName,
        password: "123"
    }
    return await postData(`${url}/users`, body);
}

function isEmptyObject(object) {
    return Object.keys(object).length === 0;
}


async function createGame(name, hostId, guestId) {
    const body = {
        name: name,
        hostId: hostId,
        guestId: guestId
    };
    await postData(`${url}/chess`, body);
}

async function loadGame() {
    window.location.href = '/games/rooms';
}
