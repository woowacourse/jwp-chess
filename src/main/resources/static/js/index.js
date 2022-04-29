const gamesUrl = `/game`;
const removeUrl = `/game`;
const roomContainer = document.getElementById('roomContainer');

fetch(gamesUrl)
.then(res => res.json())
.then(json => {
    json.forEach(gameDto => roomContainer.appendChild(createRoomDiv(gameDto)))
});

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
            alert(json.message);
            location.href = '/';
            return;
        });
}

const createRoomDiv = (gameDto) => {
    const roomUrl = `/game/${gameDto.id}`;
    const isEnd = gameDto.state === 'END';

    const roomDiv = new DOMParser()
        .parseFromString(
            `<div class="room">
                         <div class="roomNumber">
                            <Button class="roomDeleteButton" data-id=${gameDto.id} onclick="deleteRequest(event)">X</Button>
                        </div>
                         <div class="roomNumber">${gameDto.id}</div>
                         <div class="roomName"><a href=${roomUrl}>${gameDto.roomName}</a></div>
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


