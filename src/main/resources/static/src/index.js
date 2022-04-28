const movePosition = {
    source: undefined,
    target: undefined
};

function changeButton(value) {
    const button = document.getElementById("game-button")
    button.innerText = value;
}

function createNewGame() {
    const password = prompt("방 비밀번호를 입력해주세요.");
    fetch("/start/new", {
        method : "post",
        body : password
    }).then(async response => {
        let gameId = await response.text();
        enterGame(gameId);
    })
}

function enterGame(gameId) {
    location.href = "/enter/" + gameId;
}

const clickButton = (gameId) => {
    const button = document.getElementById("game-button");
    const buttonText = button.innerText;

    if (buttonText.includes("restart")) {
        removeEventListener();
        restartGame(gameId);

        const wtime = Date.now() + 500
        while(Date.now() < wtime) {}

        startGame(gameId);
    } else if (buttonText.includes("end")) {
        endGame(gameId);
    } else if (buttonText.includes("status")) {
        getStatus(gameId);
    } else if (buttonText.includes("start")) {
        startGame(gameId);
    }
}

const startGame = (gameId) => {
    const response = fetch(`/start/` + gameId, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    });
    response.then(data => data.json())
        .then(body => {
            drawBoard(body);
            changeButton("end!");
            drawTurnBox(gameId);
        });

    movePiece(gameId);
}

function drawBoard(body) {
    Object.entries(body).forEach((entry) => {
        const block = document.getElementById(entry[0]);
        if (entry[1].includes('.')) {
            block.innerHTML = null;
            return;
        }
        const imgSrc = "/images/" + entry[1] + ".svg";
        block.innerHTML = '<img id = "piece-image" class="piece-image" src=' + imgSrc + '/>'
    })
}

// function initBoard() {
//     const blocks = document.querySelectorAll('#chess-board tr td');
//     blocks.forEach(block => {
//         block.innerHTML = null;
//     })
// }

function drawTurnBox(gameId) {
    const turnBox = document.getElementById("turn-box")
    const response = fetch(`/turn/` + gameId, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    });
    response
        .then(data => data.text())
        .then(body => {
            turnBox.innerText = body + "팀 차례!";
            if (body === "end") {
                changeButton("restart");
                turnBox.innerText = "게임이 끝났습니다.";
            }
        });
}

function restartGame(gameId) {
    fetch(`/restart/` + gameId, {
        method: "get"
    });
}

const movePiece = (gameId) => {
    const blocks = document.querySelectorAll('#chess-board tr td');

    blocks.forEach(block => {
        block.addEventListener('click', (e) => clickBLock(e, block, gameId));
    })
}

const clickBLock = (e, block, gameId) => {
    if (block.className.includes('click')) {
        block.className = block.className.replace('click', '')
        deleteMovePosition(block.id);
    } else {
        block.className = block.className + 'click'
        addMovePosition(block.id)
    }

    if (isMovePositionAllSelected()) {
        const response = fetch(`/move/` + gameId, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(movePosition),
        });
        response.then(data => data.json())
            .then(body => {
                drawBoard(body)
                drawTurnBox(gameId);
            })
            .catch(err => {
                alert("움직일 수 없는 위치입니다.")
            })
        initTurn();
        setTimeout(kingDeadEndGame(gameId));
    }
}

function endGame(gameId) {
    const turnBox = document.getElementById("turn-box")
    turnBox.innerText = "게임 종료";

    fetch("/exit/" + gameId, {
        method : "post"
    });

    changeButton("status!");
}

const kingDeadEndGame = (gameId) => {
    const response = fetch(`/king/dead/` + gameId, {
        method: "GET",
        header: {"Content-Type": "application/json"}
    });
    response.then(data => data.json())
        .then(body => {
            if (body === true) {
                alert("왕이 죽었다!")
                endGame(gameId);
            }
        })
}

const removeEventListener = () => {
    const blocks = document.querySelectorAll('#chess-board tr td');

    blocks.forEach(block => {
        block.replaceWith(block.cloneNode(true));
    })
}

const getStatus = (gameId) => {
    const response = fetch(`/status/` + gameId, {
        method: "GET",
        header: {"Content-Type": "application/json"}
    });
    response.then(data => data.json())
        .then(body => {
            const turnBox = document.getElementById("turn-box")
            turnBox.innerHTML = "<div> " +
                "<div> BLACK TEAM 점수:" + body.blackScore + "</div>" +
                "<div> WHITE TEAM 점수:" + body.whiteScore + "</div>" +
                "<div> 우승 팀:" + body.winningTeam + "</div>" +
                "</div> "
        })
    changeButton("restart");
}

const initTurn = () => {
    const source = document.getElementById(movePosition.source);
    source.className = source.className.replace('click', '');
    const target = document.getElementById(movePosition.target);
    target.className = target.className.replace('click', '');
    movePosition.source = undefined;
    movePosition.target = undefined;
}

const addMovePosition = (position) => {
    if (movePosition.source !== undefined && movePosition.target !== undefined) {
        alert("이미 추가되었습니다!")
        return;
    }
    if (movePosition.source === undefined) {
        movePosition.source = position;
        return;
    }
    movePosition.target = position;
}

const deleteMovePosition = (position) => {
    if (movePosition.source === undefined && movePosition.target === undefined) {
        alert("삭제할 수 없습니다!")
        return;
    }
    if (movePosition.source === position) {
        movePosition.source = undefined;
        return;
    }
    movePosition.target = undefined;
}

const isMovePositionAllSelected = () => {
    return movePosition.source !== undefined && movePosition.target !== undefined;
}

const quit = () => {
    location.href = "/";
    // changeButton("start!")
    // const turnBox = document.getElementById("turn-box")
    // turnBox.innerText = "아직 게임 시작을 하지 않았습니다."
    // initBoard();
    //
    // fetch(`/exit`, {
    //     method: "POST",
    //     headers: {"Content-Type": "application/json"},
    // }).catch(error => alert("게임 정보를 삭제하는데 문제가 발생했습니다."));
}