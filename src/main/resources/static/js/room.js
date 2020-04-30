const formGame = document.getElementById("form-game");
const startButton = document.getElementById("start-button");
const loadButton = document.getElementById("load-button");
const blackName = document.getElementById("black-name");
const whiteName = document.getElementById("white-name");
const roomButton = document.getElementById("room-button");
const roomId = window.location.href.match(
    /(?:\w+:)?\/\/[^\/]+([^?#]+)/).pop().split('/')[2];

roomButton.onclick = () => {
    location.href = '/'
};

startButton.onclick = () => {
    if (checkNames()) {
        goGame('new');
    }
};

loadButton.onclick = () => {
    if (checkNames()) {
        goGame('load');
    }
};

function goGame(way) {
    fetch('/api/room/' + roomId + '/game?way=' + way, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            blackName, whiteName
        })
    }).then(res => res.json()).then(data => {
        formGame.action = '/room/' + roomId + '/game/' + data.gameId;
        formGame.submit();
    });
}

function checkNames() {
    if (blackName.value.toUpperCase() === "WHITE") {
        alert("Black팀의 이름은 WHITE로 지정할 수 없습니다.");
        return false;
    }
    if (whiteName.value.toUpperCase() === "BLACK") {
        alert("White팀의 이름은 BLACK으로 지정할 수 없습니다.");
        return false;
    }
    if ((blackName.value !== "" || whiteName.value !== "")
        && blackName.value === whiteName.value) {
        alert("Black팀과 White 팀의 이름은 같을 수 없습니다.");
        return false;
    }
    return true;
}

