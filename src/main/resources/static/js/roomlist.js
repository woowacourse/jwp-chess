const makeChessRoomButton = document.querySelector("#make-chess-room-button");
makeChessRoomButton.addEventListener("click", onClickMakeChessRoomButton);

async function onClickMakeChessRoomButton () {
    location.href="/join";
}

const gamesUrl = `/games`;
const roomContainer = document.getElementById('roomContainer');

fetch(gamesUrl)
    .then(res => res.json())
    .then(json => {
        json.forEach(roomDto => roomContainer.appendChild(createRoomDiv(roomDto)))
    });

const createRoomDiv = (RoomDto) => {
    const roomUrl = `/game/${RoomDto.id}`;

    const roomDiv = new DOMParser()
        .parseFromString(
            `<div class="room">
                         <div class="roomNumber">
                            <Button class="roomDeleteButton">삭제</Button>
                        </div>
                         <div class="roomNumber">${RoomDto.id}</div>
                         <div class="roomName"><a href=${roomUrl}>${RoomDto.name}</a></div>
                 </div>
                 `
            , "text/html")
        .body
        .firstElementChild;

    return roomDiv;
}
