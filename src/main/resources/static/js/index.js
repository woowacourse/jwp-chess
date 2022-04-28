const roomCreateUrl = `/games`;
const gamesUrl = `/games`;

const roomContainer = document.getElementById('roomContainer');
const roomCreatePopup = document.getElementById('roomCreatePopup');
const roomCreateButton = document.getElementById('roomCreateButton');
const roomNameInput = document.getElementById('roomNameInput');
const roomPasswordInput = document.getElementById('roomPasswordInput');
const whiteNameInput = document.getElementById('whiteNameInput');
const blackNameInput = document.getElementById('blackNameInput');

const closeRoomCreatePopup = () => {
    roomCreatePopup.classList.toggle('invisible');
}

roomCreateButton.addEventListener('click', (e) => {
    roomCreatePopup.classList.toggle('invisible');
})

document.getElementById('closeRoomCreatePopupButton').addEventListener('click', closeRoomCreatePopup);

document.getElementById('sendRoomCreateRequestButton').addEventListener('click', (e) => {
    if (roomNameInput.value.length === ''
        || roomPasswordInput.value === ''
        || whiteNameInput.value === ''
        || blackNameInput.value === '') {
        return;
    }

    fetch(roomCreateUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            roomName: roomNameInput.value,
            roomPassword: roomPasswordInput.value,
            whiteName: whiteNameInput.value,
            blackName: blackNameInput.value
        })
    }).then(res => res.json())
        .then(json => location.href = `/game/${json.id}`);
});


fetch(gamesUrl)
    .then(res => res.json())
    .then(json => {
        json.forEach(e => roomContainer.appendChild(createRoomElement(e)));
    });

const deleteRoom = (event) => {
    fetch(gamesUrl, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            id: event.target.dataset.id,
            password: prompt('비밀번호를 입력해주세요')
        })
    }).then(res => res.json())
        .then(json => {
            if (json.ok === true) {
                alert(`${json.message} 번 방이 삭제되었습니다 👍`);
                location.reload();
                return;
            }
            alert(json.message);
        });
}

const createRoomElement = (gameRoomDto) => {
    const gameEnterUrl = `/game/${gameRoomDto.id}`;
    let onPlayingText = '&nbsp; &nbsp; &nbsp; &nbsp;  🔥 진행중'
    if (gameRoomDto.finished === true) {
        onPlayingText = '&nbsp; &nbsp; &nbsp; &nbsp; ✅ 게임 종료';
    }
    const roomElement = new DOMParser()
        .parseFromString(
            `<div class="room">
                    <button class="button-30 deleteRoomButton" onclick="deleteRoom(event)" data-id="${gameRoomDto.id}">X</button>
                    <span>No. ${gameRoomDto.id} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <span>${gameRoomDto.whiteName} vs ${gameRoomDto.blackName}</span>
                    <span>${onPlayingText}</span>
                    <h3>
                        <a href=${gameEnterUrl}> ${gameRoomDto.roomName}</a>
                    </h3>
                </div>`
            , "text/html")
        .body
        .firstElementChild;

    if (gameRoomDto.finished === false) {
        roomElement.querySelector('button').disabled = true;
    }

    return roomElement;
}
