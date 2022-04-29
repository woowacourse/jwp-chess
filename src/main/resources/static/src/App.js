const start = document.getElementById('start-button');
const status = document.getElementById('status-button');
const IMAGE_PATH = ".././images/";
const BOARD = document.querySelector("#board");
const CURRENT_TEAM = document.querySelector("#current-team");
const SYMBOL_TO_IMAGE_PATH = {
    "p": IMAGE_PATH + "whitePawn.png",
    "P": IMAGE_PATH + "blackPawn.png",
    "r": IMAGE_PATH + "whiteRook.png",
    "R": IMAGE_PATH + "blackRook.png",
    "n": IMAGE_PATH + "whiteKnight.png",
    "N": IMAGE_PATH + "blackKnight.png",
    "b": IMAGE_PATH + "whiteBishop.png",
    "B": IMAGE_PATH + "blackBishop.png",
    "q": IMAGE_PATH + "whiteQueen.png",
    "Q": IMAGE_PATH + "blackQueen.png",
    "k": IMAGE_PATH + "whiteKing.png",
    "K": IMAGE_PATH + "blackKing.png"
};

let boardInfo = "";
let isChoiced = false;
let currentTurn = "";
let gameId = "";

function showStatusButton(id) {
    gameId = id;
    status.style.visibility = 'visible';
}

function initBoard() {
    fetch('/api/restart/' + gameId)
        .then(res => res.json())
        .then(imageSetting)
}

start.addEventListener('click', function () {
    if (start.textContent === "START") {
        loadBoard();
        move();
        start.textContent = "RESTART";
        return
    }
    window.alert("성공적으로 게임이 재시작되었습니다!")
    initBoard();
})

function getStatus(scoreResponse) {
    const blackScore = scoreResponse["blackTeamScore"];
    const whiteScore = scoreResponse["whiteTeamScore"];

    window.alert("블랙팀 점수 :" + blackScore + ", 하얀팀 점수 : " + whiteScore);
}

status.addEventListener('click', function () {
    fetch('/api/status/' + gameId)
        .then(res => res.json())
        .then(getStatus)
})

function loadBoard() {
    fetch('/api/load/' + gameId)
        .then(res => res.json())
        .then(imageSetting)
}

function imageSetting(response) {
    const divs = BOARD.querySelectorAll("div");
    boardInfo = response;
    pieces = response["board"];
    for (const div of divs) {
        const key = div.getAttribute("id");
        if (SYMBOL_TO_IMAGE_PATH[pieces[key]] !== undefined) {
            div.style.backgroundImage = "url(" + SYMBOL_TO_IMAGE_PATH[pieces[key]] + ")";
        } else {
            div.style.backgroundImage = null;
        }
    }
    turnSetting(response)
}

function turnSetting(response) {
    if (response["finish"] === true) {
        document.querySelector("#view-type").textContent = "승리자 :ㅤ";
        return;
    } else {
        document.querySelector("#view-type").textContent = "현재 턴 :ㅤ"
    }

    if (response["turn"] === "white") {
        currentTurn = "하얀색"
    } else {
        currentTurn = "검정색"
    }

    CURRENT_TEAM.textContent = currentTurn
}

let toTarget = "";
let fromTarget = "";

function initPosition() {
    fromTarget = "";
    toTarget = "";
}

function eventMove(event) {
    const turn = boardInfo["turn"];
    if (boardInfo["finish"] === true) {
        window.alert("게임이 종료되었습니다. RESTART 또는 STATUS만 눌러주세요.");
        return;
    }
    if (isChoiced === false) {
        fromTarget = event.target;
        const position = fromTarget.id

        if (pieces[position] === ".") {
            window.alert("기물을 선택하세요!");
            return
        }
        if (turn === "white" && pieces[position] !== pieces[position].toLowerCase() ||
            turn === "black" && pieces[position] !== pieces[position].toUpperCase()) {
            window.alert("자신의 기물을 선택하세요!");
            return;
        }
        event.target.style.backgroundColor = "skyblue";
        isChoiced = true;
        return;
    }
    if (isChoiced === true) {
        toTarget = event.target
        isChoiced = false;
    }
    if (toTarget.id !== "") {
        fromTarget.style.backgroundColor = "";
        if (fromTarget.id === toTarget.id) {
            initPosition();
            return;
        }
        movePiece(fromTarget.id, toTarget.id)
    }
}

function movePiece(from, to) {
    const request = {
        from: from,
        to: to
    }

    fetch('/api/move/' + gameId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
        .then(res => imageSetting(res));
}

function move() {

    const divs = BOARD.querySelectorAll("div");

    for (const div of divs) {
        div.addEventListener("click", eventMove);
    }
}

function enterCheckPassword(id, password) {
    var pass = prompt('패스워드를 입력하세요');
    if (password === pass) {
        window.location.href = "/room/" + id
        return;
    }
    window.alert("비밀번호가 틀렸습니다!")
}

function deleteCheckPassword(id, isFinish, password) {
    var pass = prompt('패스워드를 입력하세요');
    if (password !== pass) {
        window.alert("비밀번호가 틀렸습니다! 삭제에 실패했습니다.")
        return;
    }
    if (isFinish === "false") {
        window.alert("게임이 끝나지 않았습니다! 삭제에 실패했습니다.");
        return;
    }
    deleteRoom(id, password)
    window.alert("성공적으로 삭제가 되었습니다!");
    goHome();
}

function deleteRoom(id, password) {
    const request = {
        password: password
    }

    fetch('/api/' + id, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
}

function goHome() {
    window.location.href = "/";
}

// async function createRoom() {
//     let title = document.getElementById("title").value;
//     let password = document.getElementById("password").value;
//
//     const request = {
//         title: title,
//         password: password
//     };
//
//     fetch('/new', {
//         method: 'post',
//         headers: {
//             "Content-Type": "application/json",
//         },
//         body: JSON.stringify(request)
//     }).then(response => response.json())
//         .then((response) => {
//             if (!response.ok) {
//                 window.alert("제목이 중복되었습니다.")
//             }
//         });
// }
