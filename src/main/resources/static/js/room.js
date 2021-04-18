function addSubmitButtonEvent() {
    const submitButton = document.getElementById('submit-button');
    submitButton.addEventListener('click', function (event) {
        const xmlHttp = new XMLHttpRequest();
        const url = 'http://localhost:8080/rooms';
        const roomName = document.getElementById('room-name').value;
        const request = JSON.stringify(roomName);
        xmlHttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                const room = JSON.parse(this.responseText);
                addRoom(room);
                document.getElementById('room-name').value = '';
            }
        };
        xmlHttp.open('POST', url, true);
        xmlHttp.setRequestHeader('Content-Type', 'application/json');
        xmlHttp.send(request);
    });
}

function initiate() {
    const xmlHttp = new XMLHttpRequest();
    const url = 'http://localhost:8080/rooms';
    xmlHttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const rooms = JSON.parse(this.responseText);
            rooms.forEach(room => addRoom(room));
        }
    };
    xmlHttp.open('GET', url, true);
    xmlHttp.send();
}

function addRoom(room) {
    const roomListNode = document.getElementById('room-list');
    const node = document.createElement('li');
    const urlNode = document.createElement('a');
    urlNode.href = '/chessgame/' + room.id;
    urlNode.textContent = room.name;
    node.appendChild(urlNode);
    roomListNode.appendChild(node);
}

addSubmitButtonEvent();
initiate();

