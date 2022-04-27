let currentClickPosition = '';
let currentPiece = '';
let destinationClickPosition = '';
let isRun = false;
let roomId = 1;

const initMapEvent = () => {
    for (let file = 0; file < 8; file++) {
        for (let rank = 1; rank <= 8; rank++) {
            const positionTag = document.getElementById(intToFile(file) + rank);
            positionTag.addEventListener('click', clickToMove);
        }
    }
}

const showRoom = async () => {
    let rooms = await getRoomList();
    Object.values(rooms).forEach((value) => {
        const roomListTag = document.getElementById("chess-room-list");
        const roomTag = document.createElement("div");
        const roomNameTag = document.createElement("a");
        roomNameTag.id = value.id;
        roomNameTag.innerHTML = value.name;
        roomNameTag.addEventListener('click', enterRoom);
        const roomRemoveBtnTag = document.createElement("button");
        roomRemoveBtnTag.innerHTML = '삭제';
        roomRemoveBtnTag.id = 'roomDelete';
        roomRemoveBtnTag.addEventListener('click', deleteRoom);
        roomNameTag.appendChild(roomRemoveBtnTag);
        roomTag.appendChild(roomNameTag);
        roomListTag.appendChild(roomTag);
    });
}

const getRoomList = async () => {
    let rooms = await fetch('/rooms');
    rooms = await rooms.json();
    return rooms;
}

const makeRoomByRequest = async () => {
    console.log('안녕하세요');
    await roomPost();
    console.log(roomId);
    initMapEvent();
    await restartChess();
}

const roomPost = async () => {
    const bodyValue = {
        name: roomName,
        password: roomPassword
    };
    roomId = await fetch('/start', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    });
}

const enterRoom = async (e) => {
    isRun = true;
    roomId = e.target.id;
    let chessMap = await fetch('/load/' + roomId);
    initMapEvent();
    currentClickPosition = '';
    destinationClickPosition = '';
    chessMap = await chessMap.json();
    showChessMap(chessMap.chessMap);
    showChessMenu();
}

const deleteRoom = async (e) => {
    // fetch로 방 id 받아와서 삭제하기
}

const intToFile = (value) => {
    const map = new Map([
        [0, "a"], [1, "b"], [2, "c"], [3, "d"],
        [4, "e"], [5, "f"], [6, "g"], [7, "h"]
    ]);
    return map.get(value);
}

const toPieceFullName = (name) => {
    const map = new Map([
        ['P', 'black_pawn'], ['R', 'black_rook'], ['N', 'black_knight'], ['B', 'black_bishop'],
        ['Q', 'black_queen'], ['K', 'black_king'], ['p', 'white_pawn'], ['r', 'white_rook'],
        ['n', 'white_knight'], ['b', 'white_bishop'], ['q', 'white_queen'], ['k', 'white_king']
    ]);
    return map.get(name);
}

const markPiece = (position, pieceKind) => {
    const piece = document.createElement("img");
    piece.className = 'chess-piece';
    piece.src = "images/" + pieceKind + ".png";
    position.appendChild(piece);
}

const clickToMove = async (e) => {
    if (!isRun) {
        return;
    }
    if (currentClickPosition === '' && e.target.classList.contains('chess-piece')) {
        markCurrentPiece(e);
        return;
    }
    if (currentClickPosition !== '') {
        await markDestinationPiece(e);
    }
}

const markCurrentPiece = (e) => {
    currentClickPosition = e.currentTarget;
    currentPiece = e.target;
    currentClickPosition.style.backgroundColor = 'red';
}

const markDestinationPiece = async (e) => {
    destinationClickPosition = e.currentTarget;
    currentClickPosition.style.backgroundColor = '';
    const chessMap = await movePiece();
    if (chessMap.chessMap) {
        showChessMap(chessMap.chessMap);
        checkEndGame(chessMap.isRunning);
    }
}

const showChessMap = (chessMap) => {
    for (let file = 0; file < 8; file++) {
        for (let rank = 1; rank <= 8; rank++) {
            const positionTag = document.getElementById(intToFile(file) + rank);
            if (positionTag.hasChildNodes()) {
                positionTag.removeChild(positionTag.firstChild);
            }
            if (chessMap[8 - rank][file] === '.') {
                continue;
            }
            markPiece(positionTag, toPieceFullName(chessMap[8 - rank][file]));
        }
    }
}

const movePiece = async () => {
    const bodyValue = {
        currentPosition: currentClickPosition.id,
        destinationPosition: destinationClickPosition.id
    };
    let chessMap = await fetch('/' + roomId + '/move', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    });
    currentClickPosition = '';
    destinationClickPosition = '';
    chessMap = await chessMap.json();
    await showError(chessMap.message);
    return chessMap;
}

const checkEndGame = (isRunning) => {
    if (!isRunning) {
        alert('게임 종료');
        isRun = false;
        return showResult();
    }
}

const restartChess = async () => {
    isRun = true;
    let chessMap = await fetch('/' + roomId);
    chessMap = await chessMap.json();
    showChessMap(chessMap.chessMap);
    showChessMenu();
}

const showChessMenu = () => {
    document.getElementById("chess-room").style.display = 'none';
    document.getElementById("chess-menu").style.visibility = 'visible';
}

const showStatus = async () => {
    if (!isRun) {
        alert('먼저 게임을 시작하거나 이어해주세요.');
        return;
    }
    let status = await fetch('/' + roomId + '/status');
    status = await status.json();
    alert(status.scoreStatus);
}

const showResult = async () => {
    if (!isRun) {
        alert('먼저 게임을 시작하거나 이어해주세요.');
        return;
    }
    let result = await fetch('/' + roomId + '/end');
    result = await result.json();
    alert(result.result);
    await restartChess();
}

const showError = async (message) => {
    if (message) {
        document.getElementById('message-info').innerHTML = message;
        return;
    }
    document.getElementById('message-info').innerHTML = '';
}