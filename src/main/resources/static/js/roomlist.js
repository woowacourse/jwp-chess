const makeChessRoomButton = document.querySelector("#make-chess-room-button");
makeChessRoomButton.addEventListener("click", onClickMakeChessRoomButton);

async function onClickMakeChessRoomButton () {
    location.href="/join";
}

const gamesUrl = `/games`;
const removeUrl = `/games`;
const roomContainer = document.getElementById('roomContainer');

fetch(gamesUrl)
    .then(res => res.json())
    .then(json => {
        json.forEach(roomDto => roomContainer.appendChild(createRoomDiv(roomDto)))
    });

const createRoomDiv = (RoomDto) => {
    const roomUrl = `/game/${RoomDto.id}`;
    const isEnd = RoomDto.status === 'STOP';

    const roomDiv = new DOMParser()
        .parseFromString(
            `<div class="room">
                         <div class="roomNumber">
                            <Button class="roomDeleteButton" data-id=${RoomDto.id} onclick="deleteRequest(event)">X</Button>
                        </div>
                         <div class="roomNumber">${RoomDto.id}</div>
                         <div class="roomName"><a href=${roomUrl}>${RoomDto.name}</a></div>
                 </div>
                 `
            , "text/html")
        .body
        .firstElementChild;

    if (!isEnd) {
        roomDiv.querySelector('.roomDeleteButton').disabled = true;
    }

    return roomDiv;
}

const deleteRequest = (event) => {
    const id = event.target.dataset.id;
    const password = prompt(`${id} 번방 비밀번호를 입력해주세요.`);

    fetch(removeUrl, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            id: id,
            password: password
        })
    }).then(res => res.json())
        .then(json => {
            if (json.deleted === true) {
                alert(json.message);
                location.href = '/';
            }
        });
}
