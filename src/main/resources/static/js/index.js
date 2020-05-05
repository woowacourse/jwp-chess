const games = document.getElementById("game-room");
const addGame = document.getElementById("add-game");

const loadGameRooms = function addGameRooms(datas) {
    for (const data of datas) {
        const list = document.createElement("LI");
        const link = document.createElement("A");
        link.setAttribute("href", "/games/" + data);
        link.innerText = data + " 번방";
        list.appendChild(link);
        games.appendChild(list);
    }
};

const showGames = function showGames() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['message']);
                return;
            }
            loadGameRooms(data['data']);
        }
    };
    xhttp.open("GET", "/games", true);
    xhttp.send();
};

const createGame = function createGame() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const data = JSON.parse(xhttp.responseText);
            if (data['status'] === 'ERROR') {
                alert(data['message']);
                return;
            }
            location.reload();
        }
    };
    xhttp.open("POST", "/games", true);
    xhttp.send();
};

window.onload = showGames;

addGame.onclick = createGame;