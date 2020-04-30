let cells = document.querySelectorAll('.cell');
const turnLabel = document.getElementById('turn');
const clickTimingLabel = document.getElementById('clickTiming');
const stateLabel = document.getElementById('state');
const promotions = document.querySelectorAll('.promotion');
const blackScoreLabel = document.getElementById('blackScore');
const whiteScoreLabel = document.getElementById('whiteScore');
const blackNameLabel = document.getElementById('blackName');
const whiteNameLabel = document.getElementById('whiteName');
const winnerLabel = document.getElementById('winner');
const closeButton = document.getElementById('close-button');
const choiceGameForm = document.getElementById('choice-game');
const choiceButton = document.getElementById('choice-button');
const roomButton = document.getElementById('room-button');
const resultButton = document.getElementById('result-button');
const resultForm = document.getElementById('result');
const newGameForm = document.getElementById('new-game');
const newButton = document.getElementById('new-button');
const roomId = window.location.href.match(
    /(?:\w+:)?\/\/[^\/]+([^?#]+)/).pop().split('/')[2];
const gameId = window.location.href.match(
    /(?:\w+:)?\/\/[^\/]+([^?#]+)/).pop().split('/')[4];
const roomUrl = '/api/room/' + roomId;
const gameUrl = roomUrl + '/game/' + gameId;

let firstClick = true;
let source = null;
let target = null;

roomButton.onclick = () => {
    location.href = '/'
};

resultButton.onclick = () => {
    resultForm.submit();
};

choiceButton.onclick = () => {
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
            return;
        }
        document.getElementById(source).removeAttribute('style');
        target = cell.id;
        firstClick = true;
        fetch(gameUrl + '/path', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                source, target
            })
        }).then(res => res.json()).then(data => {
            gameSetting(data);
            if (data.state.includes("왕")) {
                gameFinish();
            }
            document.getElementById('clickTiming').innerText
                = '말이 이동할 경로(before)를 선택하세요.';
        })
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
    cells.forEach(cell => {
        cell.innerHTML = data.pieces.shift();
        cell.classList.remove('movableArea');
    });
    if (typeof data.turn === 'undefined') {
        turnLabel.innerText = "";
    } else {
        turnLabel.innerText = '현재 턴 : ' + data.turn;
    }
    if (typeof data.state === 'undefined') {
        stateLabel.innerText = "";
    } else {
        stateLabel.innerText = data.state;
    }
    blackScoreLabel.innerText = data.blackScore;
    whiteScoreLabel.innerText = data.whiteScore;
    blackNameLabel.innerText = data.blackName;
    whiteNameLabel.innerText = data.whiteName;
    winnerLabel.innerText = data.winner;
}

function gameFinish() {
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