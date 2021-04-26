const rooms = document.querySelector("#rooms");
const newRoom = document.querySelector("#newRoom");
const form = document.querySelector("#form");
const roomId = document.querySelector("#roomId");

newRoom.addEventListener("click", onNewRoom);
rooms.addEventListener("click", onRoomSelect);

function onRoomSelect(event) {
    if (event.target && event.target.nodeName == "LI" && event.target.classList.contains("clickable")) {
        form.action = "/room/" + event.target.id;
        form.submit();
    }
}

function onNewRoom() {
    fetch("/chessgames", {method: 'POST'})
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error("새 게임을 만들 수 없습니다.");
        })
        .then(chessRoomDto => {
            form.action = "/room/" + chessRoomDto.roomId;
            form.submit();
        })
        .catch(error => {
            console.error('fetch error:', error);
        });
}
