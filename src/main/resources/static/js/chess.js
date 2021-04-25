const jsonFormatChessBoard = document.getElementById('jsonFormatChessBoard');
const jsonFormatObject = JSON.parse(jsonFormatChessBoard.innerText);

const file = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
const rank = ['1', '2', '3', '4', '5', '6', '7', '8'];

const cells = [];
for (let i = 0; i < file.length; i++) {
    for (let j = 0; j < rank.length; j++) {
        cells.push(file[i] + rank[j]);
    }
}

for (let i = 0; i < cells.length; i++) {
    if (jsonFormatObject[cells[i]]) {
        const divCell = document.getElementById(cells[i]);
        let pieceName = jsonFormatObject[cells[i]].piece;

        const img = document.createElement('img');
        img.style.width = '100%';
        img.style.height = '100%';

        if (pieceName === 'P') {
            pieceName = '';
            img.src = '/images/black-pawn.png';
            img.id = divCell.id;
        }
        if (pieceName === 'R') {
            pieceName = '';
            img.src = '/images/black-rook.png';
            img.id = divCell.id;
        }
        if (pieceName === 'N') {
            pieceName = '';
            img.src = '/images/black-knight.png';
            img.id = divCell.id;
        }
        if (pieceName === 'B') {
            pieceName = '';
            img.src = '/images/black-bishop.png';
            img.id = divCell.id;
        }
        if (pieceName === 'Q') {
            pieceName = '';
            img.src = '/images/black-queen.png';
            img.id = divCell.id;
        }
        if (pieceName === 'K') {
            pieceName = '';
            img.src = '/images/black-king.png';
            img.id = divCell.id;
        }
        if (pieceName === 'p') {
            pieceName = '';
            img.src = '/images/white-pawn.png';
            img.id = divCell.id;
        }
        if (pieceName === 'r') {
            pieceName = '';
            img.src = '/images/white-rook.png';
            img.id = divCell.id;
        }
        if (pieceName === 'n') {
            pieceName = '';
            img.src = '/images/white-knight.png';
            img.id = divCell.id;
        }
        if (pieceName === 'b') {
            pieceName = '';
            img.src = '/images/white-bishop.png';
            img.id = divCell.id;
        }
        if (pieceName === 'q') {
            pieceName = '';
            img.src = '/images/white-queen.png';
            img.id = divCell.id;
        }
        if (pieceName === 'k') {
            pieceName = '';
            img.src = '/images/white-king.png';
            img.id = divCell.id;
        }

        divCell.appendChild(img);
    }
}

const currentTurn = document.getElementById('currentTurn');
const currentTurnP = document.getElementById('current-turn');
currentTurnP.textContent = currentTurn.innerText;

const statusBtn = document.getElementById('status-btn');

statusBtn.addEventListener('click', function () {
    const whiteScore = document.getElementById('whiteScore');
    const blackScore = document.getElementById('blackScore');
    alert('하얀색 기물 점수는: ' + whiteScore.textContent + '\n' +
        '검정색 기물 점수는: ' + blackScore.textContent);
});

const resetBtn = document.getElementById('reset-btn');

resetBtn.addEventListener('click', function () {
    window.location.href = '/reset';
});

let is_start_position_clicked = false;
let start_position = null;
let destination = null;
let first_click;
let second_click;

const pieceCells = document.getElementsByClassName('piece-cell');

for (let i = 0; i < pieceCells.length; i++) {
    pieceCells[i].addEventListener('click', (event) => {
        event.target.style.backgroundColor = 'gold';
        if (!is_start_position_clicked) {
            start_position = event.target.id;
            is_start_position_clicked = true;
            first_click = event.target;
            return;
        }
        destination = event.target.id;
        second_click = event.target;
        request_move_post(first_click, second_click);
    });
}

function request_move_post(first_click, second_click) {
    if (!(cells.indexOf(start_position) && cells.indexOf(destination))) {
        alert('클릭 오류입니다!');
        start_position = null;
        destination = null;
        is_start_position_clicked = false;
    }

    const moveXhr = new XMLHttpRequest();
    const turnXhr = new XMLHttpRequest();

    moveXhr.open('POST', '/move', true);
    moveXhr.setRequestHeader('Content-Type', 'application/json');
    moveXhr.responseType = "text";
    moveXhr.send(JSON.stringify({
        source: start_position,
        target: destination,
        roomId: window.location.href.split('rooms/')[1]
    }));

    start_position = null;
    destination = null;
    is_start_position_clicked = false;

    moveXhr.onload = function () {
        if (moveXhr.status === 400) {
            alert(moveXhr.response);
            first_click.style.backgroundColor = '';
            second_click.style.backgroundColor = '';
            return;
        }

        let current_turn = document.getElementById('currentTurn').innerText;
        let next_turn;

        if (current_turn === 'white') {
            next_turn = 'black';
        }
        if (current_turn === 'black') {
            next_turn = 'white';
        }

        turnXhr.open('POST', '/turn', true);
        turnXhr.setRequestHeader('Content-Type', 'application/json');
        turnXhr.responseType = "text";
        turnXhr.send(JSON.stringify({
            currentTurn: current_turn,
            nextTurn: next_turn,
            roomId: window.location.href.split('rooms/')[1]
        }));
        window.location.href = "/rooms/" + window.location.href.split('rooms/')[1];
    };
}
