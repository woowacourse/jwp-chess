const roomNames = document.getElementsByClassName('roomNames');

for (let i = 0; i < roomNames.length; i++) {
    roomNames[i].addEventListener('click', (event) => {
        const roomNameId = event.target.id;
        const roomId = roomNameId.substr(4);
        window.location.href = 'http://127.0.0.1:8080/chess/' + roomId;
    })
}