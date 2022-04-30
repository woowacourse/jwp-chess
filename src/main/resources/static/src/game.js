const movePosition = {
    source: undefined,
    target: undefined
};

function changeButton(value) {
    const button = document.getElementById("game-button")
    button.innerText = value;
}

const clickButton = (path) => {
    const id = path.substring(1);
    const button = document.getElementById("game-button");
    const buttonText = button.innerText;

    if (buttonText.includes("end")) {
        endGame(id);
    } else if (buttonText.includes("status")) {
        getStatus(id);
    }
}

const startGame = (path) => {
    const id = path.substring(1);
    const response = fetch(`/game/${id}`, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    });

    response.then(data => data.json())
        .then(body => {
            drawBoard(body);
            changeButton("end!");
            drawTurnBox(id);
        });

    movePiece(id);
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

function drawTurnBox(id) {
    const turnBox = document.getElementById("turn-box")
    const response = fetch(`/game/${id}/turn`, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    });
    response
        .then(data => data.text())
        .then(body => {
            turnBox.innerText = body + "팀 차례!";
            if (body === "NONE") {
                turnBox.innerText = "게임이 끝났습니다.";
            }
        });
}


const movePiece = (id) => {
    const blocks = document.querySelectorAll('#chess-board tr td');

    blocks.forEach(block => {
        block.addEventListener('click', (e) => clickBLock(e, block, id));
    })
}

const clickBLock = (e, block, id) => {
    if (block.className.includes('click')) {
        block.className = block.className.replace('click', '')
        deleteMovePosition(block.id);
    } else {
        block.className = block.className + 'click'
        addMovePosition(block.id)
    }

    if (isMovePositionAllSelected()) {
        const response = fetch(`/game/${id}/move`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(movePosition),
        });

        response.then(data => data.json())
            .then(body => {
                drawBoard(body)
                drawTurnBox(id);
            })
            .then(() => {
                kingDeadEndGame(id);
            })
            .catch(err => {
                alert("움직일 수 없는 위치입니다.")
            })
        initTurn();

    }
}

function endGame() {
    removeEventListener();
    changeButton("status!")
    const turnBox = document.getElementById("turn-box")
    turnBox.innerText = "게임 종료";
}

const kingDeadEndGame = (id) => {
    const response = fetch(`/game/${id}/dead`, {
        method: "GET",
        header: {"Content-Type": "application/json"}
    });

    response.then(data => data.json())
        .then(body => {
            if (body === true) {
                alert("왕이 죽었다!")
                endGame();
            }
        })
}

const removeEventListener = () => {
    const blocks = document.querySelectorAll('#chess-board tr td');

    blocks.forEach(block => {
        block.replaceWith(block.cloneNode(true));
    })
}

const getStatus = (id) => {
    const response = fetch(`/game/${id}/status`, {
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
    window.location.href = "/";
}