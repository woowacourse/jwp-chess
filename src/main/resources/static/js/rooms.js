const roomNames = document.getElementsByClassName('roomNames');

for (let i = 0; i < roomNames.length; i++) {
    roomNames[i].addEventListener('click', (event) => {
        const roomNameId = event.target.id;
        const roomId = roomNameId.substr(4);
        window.location.href = '/chess/' + roomId;
    })
}

const homeBtn = document.getElementById('home-btn');

homeBtn.addEventListener('click', function () {
    window.location.href = '/';
});