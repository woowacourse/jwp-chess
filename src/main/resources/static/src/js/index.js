async function onloadGameBody() {
    const id = new URL(window.location).searchParams.get('id')

    let game = await fetch("/api/chess/rooms/" + id)
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    game = await game.json();

    console.log(game);

    let boards = game.board.boards;

    document.querySelectorAll('.piece-image')
        .forEach(cell => cell.addEventListener('click', e => cellClick(e, id)));
}


async function onloadIndexBody() {
    //1) GET 요청
    let rooms = await fetch("/api/chess/rooms/")
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    rooms = await rooms.json();
    let roomSpace = document.querySelector("ul.class-list");
    Object.values(rooms.roomResponseDtos).forEach(function (value) {

        const li = document.createElement("li");
        li.className = "class-card";

        const img = document.createElement("img");
        img.className = "class-image";
        img.setAttribute("src", "images/room.jpg");
        li.appendChild(img);

        const div = document.createElement("div");
        div.className = "class-container";

        const divInnerFirst = document.createElement("div");
        divInnerFirst.className = "class-skill";

        const divInnerFirstId = document.createElement("div");
        divInnerFirstId.className = "class-type";
        divInnerFirstId.innerText = value.id + "번방";

        const divInnerFirstUpdate = document.createElement("div");
        divInnerFirstUpdate.className = "class-update";
        divInnerFirstUpdate.setAttribute("onclick", "updateRoomName(" + value.id + ");");
        divInnerFirstUpdate.innerText = "수정";

        const divInnerFirstDelete = document.createElement("div");
        divInnerFirstDelete.className = "class-delete";
        divInnerFirstDelete.setAttribute("onclick", "deleteRoom(" + value.id + ");");
        divInnerFirstDelete.innerText = "삭제";

        divInnerFirst.appendChild(divInnerFirstId);
        divInnerFirst.appendChild(divInnerFirstUpdate);
        divInnerFirst.appendChild(divInnerFirstDelete);

        const divInnerSecond = document.createElement("div");
        divInnerSecond.className = "class-title-container";

        const divInnerSecondTitle = document.createElement("div");
        divInnerSecondTitle.className = "class-title";
        divInnerSecondTitle.innerText = value.name;

        const divInnerSecondJoin = document.createElement("div");
        divInnerSecondJoin.className = "class-join";
        divInnerSecondJoin.setAttribute("onclick", "enterRoom(" + value.id + ");");
        divInnerSecondJoin.innerText = "입 장";

        divInnerSecond.appendChild(divInnerSecondTitle);
        divInnerSecond.appendChild(divInnerSecondJoin);
        div.appendChild(divInnerFirst);
        div.appendChild(divInnerSecond);
        li.appendChild(div);

        roomSpace.appendChild(li);
    });
}

async function createRoom() {
    const roomName = window.prompt("방 제목을 중복되지 않도록 입력해주세요.");

    const bodyValue = {
        name: roomName
    }
    let response = await fetch("/api/chess/rooms/", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    })
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    window.location.reload();
}

async function handleErrors(response) {
    if (!response.ok) {
        let message = await response.json();
        throw Error(message.errorMessage);
    }
    return response;
}

async function enterRoom(id) {
    let game = await fetch("/api/chess/rooms/" + id)
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    game = await game.json();
    console.log("game>>>", game);
    window.location.replace("/game.html?id=" + game.id);
}


async function updateRoomName(id) {

    const roomName = window.prompt("바꿀 방 제목을 입력해주세요");

    if (roomName === null) {
        return;
    }

    const bodyValue = {
        name: roomName
    }

    await fetch("/api/chess/rooms/" + id + "/update",{
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    })
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    window.location.reload();
}

async function deleteRoom(id) {
    if(confirm("정말 삭제하시겠습니까?")) {
        await fetch("/api/chess/rooms/" + id + "/end",{ method:"PATCH"})
            .then(handleErrors)
            .catch(function (error) {
                alert(error.message);
            });
        window.location.reload()
    }
}
