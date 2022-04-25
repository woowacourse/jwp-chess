const startButton = document.querySelector("#startButton");
// document.querySelectorAll('.piece-image')
//     .forEach(cell => cell.addEventListener('click', e => cellClick(e, id)));
const id = new URL(window.location).searchParams.get('id')

let gameOver = "";

let source = "";
let target = "";

async function onloadGameBody() {
    let game = await fetch("/api/chess/rooms/" + id)
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    game = await game.json();
    console.log(game);
    let boards = game.board.boards;
    // console.log(boards);

    //view 세팅 시작------------
    // 1) 방 제목 넣어주기
    document.getElementById("roomName").innerText = game.name;

    // 2) move직전에, gameOver라는 플래그를 통해 게임시작/종료를 확인한다. -> 채워주기
    gameOver = game.gameOver;

    // 3) (추가) 입장했다면, start버튼 hide시키기
    // startButton.classList.add("hide");
    startButton.style.display = 'none';

    // 4) board판을 돌면서, 해당position의 div에 [기존 자식태그(img) 삭제후 -> 새 img태그 추가]
    putPiece(boards);
}

function putPiece(boards) {
    for (let i = 0; i < boards.length; i++) {
        let position = boards[i].position;
        let piece = boards[i].piece;

        let div = document.querySelector("#" + position);
        // (1) 이미 안에 기물용img태그 있다면 지우기
        if (div.hasChildNodes()) {
            div.removeChild(div.firstChild);
        }
        // (2) 기물용img태그 추가해주기
        let img = document.createElement("img");
        img.style.width = '30px';
        img.style.height = '40px';
        img.style.display = 'block';
        img.style.margin = 'auto';
        img.src = "/images/" + piece + ".png";
        div.appendChild(img);
    }
}

startButton.addEventListener('click', async function () {
    // if (startButton.textContent === "Start") {
    let board = startGame();
    // console.log(board)
    await initializeBoard(board);
    // startButton.textContent = "End";
    // return
    // }

    // let board = endGame();
    // await initializeBoard(board);
    // startButton.textContent = "Start";

})

async function startGame() {
    let savedBoard = await fetch("/api/chess/rooms/" + id)
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    savedBoard = await savedBoard.json();
    gameOver = savedBoard.gameOver;
    return savedBoard.board.boards;
}

async function endGame() {
    await getScore()
    let savedBoard = await fetch("/api/chess/rooms/33")
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        });
    savedBoard = await savedBoard.json();
    gameOver = savedBoard.gameOver;
    return savedBoard.board.boards;
}

async function initializeBoard(board) {
    board.then(res => Object.values(res).forEach(function (value) {
        let eachDiv = document.querySelector("#" + value.position);
        putPiece(eachDiv, res, value.piece);
    }))
}

// function putPiece(eachDiv, board, value) {
//     if (eachDiv.hasChildNodes()) {
//         eachDiv.removeChild(eachDiv.firstChild);
//     }
//     const img = document.createElement("img");
//     img.style.width = '30px';
//     img.style.height = '40px';
//     img.style.display = 'block';
//     img.style.margin = 'auto';
//     img.src = "/images/" + value + ".png";
//     eachDiv.appendChild(img);
// }

function clickMovePosition(e) {
    if (gameOver === "true" || gameOver === "") {
        alert("게임이 시작되지 않아 선택 불가");
        return;
    }
    if (source === "") {
        source = e;
        document.getElementById(source).style.backgroundColor = 'yellow';
        return;
    }

    if (source !== "" && target === "") {
        target = e;
        document.getElementById(source).style.backgroundColor = '';
        movePiece(source, target);
        source = "";
        target = "";
    }
}

async function movePiece(source, target) {
    const game = await sendMoveInformation(source, target);
    // await updateBoard(board.board.boards);
    putPiece(game.board.boards)
    await checkGameOver(game.gameOver);
}

async function sendMoveInformation(source, target) {
    const bodyValue = {
        source: source, target: target
    }
    let game = await fetch("/api/chess/rooms/" + id + "/move", {
        method: 'POST', headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        }, body: JSON.stringify(bodyValue)
    }).then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    game = await game.json();
    return game;
}

// async function updateBoard(board) {
//     Object.values(board).forEach(function (value) {
//         let eachDiv = document.querySelector("#" + value.position);
//         putPiece(eachDiv, board, value.piece);
//     })
// }

async function checkGameOver(gameOverMessage) {
    gameOver = gameOverMessage;
    if (gameOver === "true") {
        alert("게임이 종료되었습니다.");
        let board = endGame();
        await initializeBoard(board);

        // startButton.textContent = "Start";
        startButton.style.display = 'block';
    }
}

async function getScore() {
    if (gameOver === "") {
        alert("게임이 시작되지 않아 선택 불가");
        return;
    }
    let score = await fetch("/api/chess/rooms/33/status")
        .then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    score = await score.json();
    alert("Black 팀 점수 = " + score.blackScore + "\nWhite 팀 점수 = " + score.whiteScore + "\n 현재 승자 = " + score.winningTeam);

}

async function handleErrors(response) {
    if (!response.ok) {
        let message = await response.json();
        console.log(response)
        throw Error(message.errorMessage);
    }
    return response;
}
