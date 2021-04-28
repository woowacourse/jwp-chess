async function initiate() {
    await axios.get('/chessgame/' + roomId + '/chessboard')
        .then(response => {
            const boardDTO = response.data;
            if (boardDTO.checkmate === true) {
                window.location.href = '/result/' + roomId;
            }
            printChessBoard(boardDTO.rows, boardDTO.currentTeamType);
        }).catch(error => alert(error.response.data));
}

function printChessBoard(rows, currentTeamType) {
    printEachRow(rows);
    changeCurrentTeamType(currentTeamType);
    Array.from(document.getElementsByClassName('piece'))
        .forEach(piece => addMovableEvent(piece, currentTeamType));
}

function printEachRow(rows) {
    const $board = document.getElementById('board');
    const $pieceTemplate = document.querySelector('#template-list-piece').innerHTML;
    for (let i = 0; i < rows.length; i++) {
        const columns = rows[i].pieces;
        for (let j = 0; j < columns.length; j++) {
            const piece = columns[j];
            const coordinate = calculateCoordinate(i, j);
            const $pieceHtml = $pieceTemplate.replace('{url}', generatePieceUrl(piece))
                .replace('{coordinate}', coordinate);
            $board.insertAdjacentHTML('beforeend', $pieceHtml);
        }
    }
}

function calculateCoordinate(i, j) {
    const x = j + 97;
    const y = 8 - i;
    return String.fromCharCode(x) + y;
}

function generatePieceUrl(piece) {
    if (piece === null) {
        return 'Blank';
    }
    const teamPrefix = (piece.teamType === 'BLACK') ? 'B' : 'W';
    return teamPrefix + piece.name.toUpperCase();
}

function changeCurrentTeamType(currentTeamType) {
    const $currentTeamType = document.querySelector('.current-team-type');
    $currentTeamType.textContent = currentTeamType;
}

function addMovableEvent(piece, currentTeamType) {
    piece.addEventListener('click', function (event) {
        event.stopPropagation();
        const $currentPosition = document.querySelector('.current-position');
        if (isPieceChosenForTheFirst($currentPosition)) {
            $currentPosition.value = piece.id;
            piece.style.border = '1px solid red';
            return;
        }
        if (isPieceChosenAlready(piece)) {
            $currentPosition.value = '';
            piece.style.border = '1px solid black';
            return;
        }
        sendMoveRequest($currentPosition.value, piece.id, currentTeamType);
        $currentPosition.value = '';
    });
}

function isPieceChosenForTheFirst(current) {
    return current.value.length === 0;
}

function isPieceChosenAlready(piece) {
    return piece.style.border === '1px solid red';
}

async function sendMoveRequest(current, destination, teamType) {
    const requestData = JSON.stringify({
        "current": current,
        "destination": destination,
        "teamType": teamType
    });
    await axios.put('/chessgame/' + roomId + '/chessboard', requestData, {headers: {'Content-Type': 'application/json'}})
        .then(response => {
            const boardDTO = response.data;
            if (boardDTO.checkmate === true) {
                window.location.href = '/result/' + roomId;
            }
            removeOutdatedChessBoard();
            printChessBoard(boardDTO.rows, boardDTO.currentTeamType);
        }).catch(error => {
            const $currentPiece = document.getElementById(current);
            $currentPiece.style.border = '1px solid black';
            alert(error.response.data);
        });
}

function removeOutdatedChessBoard() {
    Array.from(document.getElementsByClassName('piece'))
        .forEach(piece => piece.remove());
}

function addDeleteEvent() {
    const $deleteButton = document.getElementById('delete-button');
    $deleteButton.addEventListener('click', requestDeletion);
}

const requestDeletion = async () => {
    await axios.delete('/logout/' + roomId)
        .then(response => window.location.replace(response.data))
        .catch(error => alert(error.response.data));
}

initiate();
addDeleteEvent();


