const roomId = document.getElementById("roomId").innerText;

const file = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
const rank = ['1', '2', '3', '4', '5', '6', '7', '8'];

const cells = [];
for (let i = 0; i < file.length; i++) {
    for (let j = 0; j < rank.length; j++) {
        cells.push(file[i] + rank[j]);
    }
}

let is_start_position_clicked = false;
let start_position = null;
let destination = null;
let first_click;
let second_click;

runChessGame();

async function runChessGame() {
    let boardInfo = await boardDto();

    const chessBoard = boardInfo['chessBoard'];
    const currentTurn = boardInfo['currentTurn'];

    for (let i = 0; i < cells.length; i++) {
        if (chessBoard[cells[i]]) {
            const divCell = document.getElementById(cells[i]);
            let piece = chessBoard[cells[i]]['piece'];

            const img = document.createElement('img');
            img.style.width = '100%';
            img.style.height = '100%';

            if (piece === 'P') {
                piece = '';
                img.src = '/images/black-pawn.png';
                img.id = divCell.id;
            }
            if (piece === 'R') {
                piece = '';
                img.src = '/images/black-rook.png';
                img.id = divCell.id;
            }
            if (piece === 'N') {
                piece = '';
                img.src = '/images/black-knight.png';
                img.id = divCell.id;
            }
            if (piece === 'B') {
                piece = '';
                img.src = '/images/black-bishop.png';
                img.id = divCell.id;
            }
            if (piece === 'Q') {
                piece = '';
                img.src = '/images/black-queen.png';
                img.id = divCell.id;
            }
            if (piece === 'K') {
                piece = '';
                img.src = '/images/black-king.png';
                img.id = divCell.id;
            }
            if (piece === 'p') {
                piece = '';
                img.src = '/images/white-pawn.png';
                img.id = divCell.id;
            }
            if (piece === 'r') {
                piece = '';
                img.src = '/images/white-rook.png';
                img.id = divCell.id;
            }
            if (piece === 'n') {
                piece = '';
                img.src = '/images/white-knight.png';
                img.id = divCell.id;
            }
            if (piece === 'b') {
                piece = '';
                img.src = '/images/white-bishop.png';
                img.id = divCell.id;
            }
            if (piece === 'q') {
                piece = '';
                img.src = '/images/white-queen.png';
                img.id = divCell.id;
            }
            if (piece === 'k') {
                piece = '';
                img.src = '/images/white-king.png';
                img.id = divCell.id;
            }

            divCell.appendChild(img);
        }
    }

    const current_turn = document.getElementById('current-turn');
    current_turn.innerText = currentTurn;

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
            request_move_post(first_click, second_click, currentTurn);
        });
    }
}

async function boardDto() {
    let boardDto = await fetch('/board/' + roomId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    boardDto = await boardDto.json();
    return boardDto;
}

function request_move_post(first_click, second_click, current_turn) {
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
    moveXhr.responseType = 'json';
    moveXhr.send(JSON.stringify({
        source: start_position,
        target: destination,
        roomId: roomId
    }));

    start_position = null;
    destination = null;
    is_start_position_clicked = false;

    moveXhr.onload = function () {
        const move_response = moveXhr.response;

        if (!move_response['movable']) {
            alert(move_response['errorMessage']);
            first_click.style.backgroundColor = '';
            second_click.style.backgroundColor = '';
            return;
        }

        let next_turn;

        if (current_turn === 'white') {
            next_turn = 'black';
        }
        if (current_turn === 'black') {
            next_turn = 'white';
        }

        turnXhr.open('POST', '/turn', true);
        turnXhr.setRequestHeader('Content-Type', 'application/json');
        turnXhr.responseType = 'json';
        turnXhr.send(JSON.stringify({
            currentTurn: current_turn,
            nextTurn: next_turn,
            roomId: roomId
        }));
        window.location.href = 'http://127.0.0.1:8080/chess/' + roomId;
    };
}

const statusBtn = document.getElementById('status-btn');

statusBtn.addEventListener('click', function () {
    const whiteScore = document.getElementById('whiteScore');
    const blackScore = document.getElementById('blackScore');
    alert('하얀색 기물 점수는: ' + whiteScore.textContent + '\n' +
        '검정색 기물 점수는: ' + blackScore.textContent);
});

const homeBtn = document.getElementById('home-btn');

homeBtn.addEventListener('click', function () {
    window.location.href = "/";
});