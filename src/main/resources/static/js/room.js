const roomButton = document.getElementById("room-button");
const startButton = document.getElementById("start-button");
const loadButton = document.getElementById("load-button");

roomButton.onclick = () => {
    location.href = '/'
};

startButton.onclick = () => {
    checkNameAndGo('new');
};

loadButton.onclick = () => {
    checkNameAndGo('load');
};

function checkNameAndGo(way) {
    const blackNameInput = document.getElementById("black-name");
    const whiteNameInput = document.getElementById("white-name");

    if (checkNames()) {
        goGame(way);
    }

    function checkNames() {
        if (blackNameInput.value.toUpperCase() === "WHITE") {
            alert("Black팀의 이름은 WHITE로 지정할 수 없습니다.");
            return false;
        }
        if (whiteNameInput.value.toUpperCase() === "BLACK") {
            alert("White팀의 이름은 BLACK으로 지정할 수 없습니다.");
            return false;
        }
        if ((blackNameInput.value !== "" || whiteNameInput.value !== "")
            && blackNameInput.value === whiteNameInput.value) {
            alert("Black팀과 White 팀의 이름은 같을 수 없습니다.");
            return false;
        }
        return true;
    }

    function goGame(way) {
        const roomId = window.location.href.match(
            /(?:\w+:)?\/\/[^\/]+([^?#]+)/).pop().split('/')[2];
        fetch('/api/room/' + roomId + '/game/' + way, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                blackName: blackNameInput.value,
                whiteName: whiteNameInput.value
            })
        }).then(res => res.json()).then(data => {
            const formGame = document.getElementById("form-game");
            formGame.action = '/room/' + roomId + '/game/' + data.gameId;
            formGame.submit();
        });
    }
}


