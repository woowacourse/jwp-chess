const BLOCK_SIZE_PIXEL = 80;

const $chessBoard = document.getElementById('chess-board');

const colorTranslationTable = {BLACK: '흑', WHITE: '백'};
const matchResultTranslationTable = {DRAW: '무승부', WHITE_WIN: '백 승리', BLACK_WIN: '흑 승리'};

const squareBuffer = new SquareBuffer();

let gameId;

loadGameListIntoBox();

addSelectionEventOnChessBoard();
addEventOnStartButton();
addEventOnRegameButton();
addEventOnLoadGameButton();

function SquareBuffer() {
    this.buffer = [];
    this.add = addAndRequestMove;
}

async function processResponse(response) {
    const responseBody = await response.json();

    console.log(responseBody.message);
    updateMessage(responseBody.message);

    if (response.ok) {
        if (responseBody.item.gameId !== undefined) {
            gameId = responseBody.item.gameId;
        }
        updateGameData(responseBody);
    }
}

async function loadGameListIntoBox() {
    const gameListBox = document.getElementById("gameListBox");

    const response = await fetch("/game");
    if (response.ok) {
        const responseBody = await response.json();

        console.log(responseBody.message);
        const gameNumbers = responseBody.item.gamesId;

        for (let gameNumber of gameNumbers) {
            let option = document.createElement("option");
            option.text = `${gameNumber}번 게임`;
            option.setAttribute("value", gameNumber);
            gameListBox.add(option);
        }
    }
}

async function addAndRequestMove(square) {
    this.buffer.push(square);
    if (this.buffer.length === 2) {
        const toSquare = this.buffer.pop();
        const fromSquare = this.buffer.pop();

        toSquare.classList.toggle('opaque');
        fromSquare.classList.toggle('opaque');

        console.log(`request [POST]/move, body: from: ${fromSquare.id}, \nto: ${toSquare.id}\n`);

        try {
            fetch(`/game/${gameId}/pieces`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    from: fromSquare.id,
                    to: toSquare.id,
                }),
            })
                .then(res => processResponse(res));
        } catch (error) {
            console.error(error.messages);
        }
    }
}

async function addEventOnStartButton() {
    await document.getElementById('start-button').addEventListener('click', event => {
        try {
            fetch('/game', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({}),
            })
                .then(res => processResponse(res));
            turnOnPanel();
        } catch (error) {
            console.error(error.messages);
        }
    });
}

async function addSelectionEventOnChessBoard() {
    $chessBoard.addEventListener('click', event => {
        const selectedSquare = event.target.closest('div');
        squareBuffer.add(selectedSquare);
        selectedSquare.classList.toggle('opaque');
    })
}

async function addEventOnRegameButton() {
    await document.getElementById('regame-button').addEventListener('click', event => {
        try {
            fetch('/game', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({}),
            })
                .then(res => processResponse(res));
            turnOnPanel();
        } catch (error) {
            console.error(error.messages);
        }
    });
}

async function addEventOnLoadGameButton() {
    document.getElementById('load-button').addEventListener('click', event => {
        try {
            const gameListBox = document.getElementById("gameListBox");
            gameId = gameListBox.options[gameListBox.selectedIndex].value;
            fetch(`/game/${gameId}`)
                .then(res => processResponse(res));
            turnOnPanel();
        } catch (error) {
            console.error(error.message);
        }
    })
}

async function updateGameData(responseJsonBody) {
    updateBoard(responseJsonBody.item.chessBoard);
    if (responseJsonBody.item.end) {
        updateWinner(responseJsonBody);
        updateMessage('게임이 끝났습니다.');
        return;
    }
    updateScoreAndTurn(responseJsonBody);
}

async function updateBoard(piecesMap) {
    for (const square of $chessBoard.querySelectorAll('div')) {
        square.innerHTML = '';
    }
    for (const [position, piece] of Object.entries(piecesMap)) {
        updateSquare(position, piece);
    }
}

async function updateSquare(position, piece) {
    const square = document.getElementById(position);
    square.appendChild(makeImage(piece.color + '-' + piece.name));
}

function makeImage(imageName) {
    const img = document.createElement('img');
    img.setAttribute('src', '/images/' + imageName + '.png');
    img.height = BLOCK_SIZE_PIXEL;
    return img;
}

async function updateScoreAndTurn(responseJsonBody) {
    const blackScore = responseJsonBody.item.colorsScore.BLACK;
    const whiteScore = responseJsonBody.item.colorsScore.WHITE;
    const currentTurn = responseJsonBody.item.currentTurnColor;
    document.getElementById('score-console').innerText = `백: ${whiteScore}점 흑: ${blackScore}점\n현재 순서: ${colorTranslationTable[currentTurn]}`;
}

async function updateWinner(responseJsonBody) {
    const blackScore = responseJsonBody.item.colorsScore.BLACK;
    const whiteScore = responseJsonBody.item.colorsScore.WHITE;
    const winner = responseJsonBody.item.matchResult;
    document.getElementById('score-console').innerText = `백: ${whiteScore}점 흑: ${blackScore}점\n승: ${matchResultTranslationTable[winner]}`;
}

async function updateMessage(message) {
    document.getElementById('message-console').innerText = message;
}

async function turnOnPanel() {
    for (const button of document.getElementById('panel').querySelectorAll('button')) {
        button.style.display = 'none';
    }
    document.getElementById('gameListBox').style.display = 'none';

    document.getElementById('regame-button').style.display = 'block';
    document.getElementById('message-console').style.display = 'block';
    document.getElementById('score-console').style.display = 'block';
}

