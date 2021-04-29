import {putData} from "./utils/FetchUtil.js";

const roomNames = document.getElementsByClassName('roomNames');

for (let i = 0; i < roomNames.length; i++) {
    roomNames[i].addEventListener('click', (event) => {
        const roomNameId = event.target.id;
        const roomId = roomNameId.substr(4);
        window.location.href = 'http://localhost:8080/games/' + roomId;
    })
}

const homeBtn = document.getElementById('home-btn');

homeBtn.addEventListener('click', function () {
    window.location.href = '/';
});


const deleteBtn = document.getElementsByClassName('roomDelete');

for (let i = 0; i < deleteBtn.length; i++) {
    deleteBtn[i].addEventListener('click', (event) => {
        const roomNameId = event.target.id;
        const roomId = roomNameId.substr(10);
        putData('http://localhost:8080/games/' + roomId + '/delete', roomId)
        alert(roomId + "가 삭제되었습니다");
        location.reload(true)

    })
}

