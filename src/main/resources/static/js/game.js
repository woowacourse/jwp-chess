const roomButton = document.getElementById('room-button');
const resultButton = document.getElementById('result-button');
const choiceButton = document.getElementById('choice-button');
const newButton = document.getElementById('new-button');
const closeButton = document.getElementById('close-button');
const promotions = document.querySelectorAll('.promotion');
const turnLabel = document.getElementById('turn');
const stateLabel = document.getElementById('state');
const blackNameLabel = document.getElementById('blackName');
const whiteNameLabel = document.getElementById('whiteName');

const roomId = window.location.href.match(
    /(?:\w+:)?\/\/[^\/]+([^?#]+)/).pop().split('/')[2];
const gameId = window.location.href.match(
    /(?:\w+:)?\/\/[^\/]+([^?#]+)/).pop().split('/')[4];
const roomUrl = '/api/room/' + roomId;
const gameUrl = roomUrl + '/game/' + gameId;

let cells = document.querySelectorAll('.cell');
let firstClick = true;
let source = null;

roomButton.onclick = () => {
    location.href = '/'
};

resultButton.onclick = () => {
    const resultForm = document.getElementById('result');
    resultForm.submit();
};

choiceButton.onclick = () => {
    const choiceGameForm = document.getElementById('choice-game');
    choiceGameForm.action = '/room/' + roomId;
    choiceGameForm.submit();
};

newButton.onclick = () => {
    fetch(roomUrl + '/game', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            blackName: blackNameLabel.innerText,
            whiteName: whiteNameLabel.innerText,
            way: 'new'
        })
    }).then(res => res.json()).then(data => {
        const newGameForm = document.getElementById('new-game');
        newGameForm.action = '/room/' + roomId + '/game/' + data.gameId;
        newGameForm.submit();
    });
};

closeButton.onclick = () => {
    fetch(gameUrl + '/end', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            gameId
        })
    }).then(res => res.json()).then(data => {
        gameSetting(data);
        gameFinish();
    })
};

cells.forEach(cell => {
    cell.onclick = () => {
        if (firstClick) {
            getMovableAreas();
            return;
        }
        move();

        function getMovableAreas() {
            source = cell.id;
            firstClick = false;
            document.getElementById('clickTiming').innerText
                = '말이 이동할 경로(after)를 선택하세요.';
            stateLabel.innerText = "";
            cell.style.backgroundColor = 'STEELBLUE';
            fetch(gameUrl + '/path?source=' + source).then(
                res => res.json()).then(data => {
                cells.forEach(cell => {
                    cell.classList.remove('movableArea');
                });
                data.movableAreas.forEach(movableArea => {
                    document.getElementById(movableArea).classList.add(
                        'movableArea');
                })
            });
        }

        function move() {
            document.getElementById(source).removeAttribute('style');
            firstClick = true;
            fetch(gameUrl + '/path', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    source, target: cell.id
                })
            }).then(res => res.json()).then(data => {
                gameSetting(data);
                if (data.state.includes("왕")) {
                    gameFinish();
                }
                document.getElementById('clickTiming').innerText
                    = '말이 이동할 경로(before)를 선택하세요.';
            });
        }
    }
});

promotions.forEach(promotion => {
    promotion.onclick = () => {
        fetch(gameUrl + '/promotion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                promotionType: promotion.id
            })
        }).then(res => res.json()).then(data => {
            gameSetting(data);
        })
    }
});

fetch(gameUrl + '/board').then(
    res => res.json()).then(data => {
    gameSetting(data);
});

function gameSetting(data) {
    const blackScoreLabel = document.getElementById('blackScore');
    const whiteScoreLabel = document.getElementById('whiteScore');
    const winnerLabel = document.getElementById('winner');

    cells.forEach(cell => {
        cell.innerHTML = data.pieces.shift();
        cell.classList.remove('movableArea');
    });
    turnLabel.innerText = getTurn();
    stateLabel.innerText = getState();
    blackScoreLabel.innerText = data.blackScore;
    whiteScoreLabel.innerText = data.whiteScore;
    blackNameLabel.innerText = data.blackName;
    whiteNameLabel.innerText = data.whiteName;
    winnerLabel.innerText = data.winner;

    function getTurn() {
        if (typeof data.turn === 'undefined') {
            return "";
        }
        return '현재 턴 : ' + data.turn;
    }

    function getState() {
        if (typeof data.state === 'undefined') {
            return "";
        }
        return data.state;
    }
}

function gameFinish() {
    const clickTimingLabel = document.getElementById('clickTiming');

    cells.forEach(cell => {
        cell.innerHTML = "";
        cell.classList.remove('cell');
    });
    cells = null;
    turnLabel.innerText = "";
    clickTimingLabel.innerText = '게임이 종료되었습니다.';
    closeButton.innerText = "종료됨";
    closeButton.disable = true;
}