const body = document.getElementsByTagName('body')[0]
body.addEventListener('click', onClick)
const squareIdList = [];
const id = location.href.split("/").pop();

const restartButton = document.querySelector("#reStartButton");
restartButton.addEventListener("click", reStartGame);

async function reStartGame() {
    await initBoard();
    await loadAllPiece();
}

async function initBoard() {
    let id = location.href.split("/").pop();
    await fetch("/board/"+id, {
        method: 'POST'
    });
}

async function loadAllPiece() {
    let id = location.href.split("/").pop();
    const res = await fetch("../board/"+id, {
        method: 'GET'
    });
    const pieces = await res.json();
    if (!res.ok) {
        alert("체스판 로드가 실패했습니다.");
        return;
    }

    for (let piece of pieces.pieces) {
        JSON.stringify(piece)
        fillSquare(piece.square, piece.type, piece.color)
    }
}

function fillSquare(squareName, pieceType, pieceColor) {
    pieceColor = pieceColor.toLowerCase()
    pieceType = pieceType.toLowerCase()
    if (pieceColor === "nothing") {
        document.getElementById(squareName).innerHTML =''
    }
    if (pieceColor !== "nothing") {
        document.getElementById(squareName).innerHTML = "<img class=piece-image src='/image/pieces/" + pieceColor + "/"
            + pieceColor + "-" + pieceType + ".svg" + "'/>"
    }
}

async function onClick(event) {
    let target = event.target;
    console.log(target.tagName + '클릭됨')
    if (target.tagName === 'BUTTON') {
        await onButtonClick(event);
    }

    if (target.classList.contains('piece-image') || target.classList.contains('cell')) {
        if (squareIdList.length !== 2) {
            squareIdList.push(onCellClick());
            await postTwoCells();
        }
    }

    function onCellClick() {
        const table = target.parentElement.parentElement
        makeAllCellsNotClicked(table)
        return target.closest("td").id
    }

    async function postTwoCells() {
        if (squareIdList.length === 2) {
            await patchMove()
        }
    }

    function makeAllCellsNotClicked(table) {
        if (squareIdList.length === 2) {
            for (const tr of table.getElementsByTagName('tr')) {
                for (const td of tr.getElementsByTagName('td')) {
                    if (td.classList.contains('clicked')) {
                        td.classList.toggle('clicked')
                    }
                }
            }
        }
    }

    async function onButtonClick() {
        const classList = target.classList

        if (classList.contains('status-button')) {
            await getResult()
            return
        }
        if (classList.contains('end-button')) {
            await endGame()
        }
    }
}

async function patchMove() {
    let id = location.href.split("/").pop();
    const response = await fetch('/board/' + id, {
        method: 'PATCH',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'to=' + squareIdList.pop() + '&from=' + squareIdList.pop()
    });

    const body = await response.json();
    if (!response.ok) {
        alert(body.message);
        return;
    }
    await loadAllPiece();
}

async function getResult() {
    fetch('/game/' + id + '/status', {
        method: 'GET'
    }).then(res => res.json())
    .then(json => {
        if (json.ok !== undefined && json.ok === false) {
            alert(json.message);
            return;
        }
        let matchResult = json.winnerColor + '가 이겼습니다!';
        const whiteScore = json.playerPoints.WHITE
        const blackScore = json.playerPoints.BLACK
        if (json.isDraw) {
            matchResult = '비겼습니다!'
        }
        alert(`흰색 플레이어 점수 : ${whiteScore}\n검은색 플레이어 점수 : ${blackScore}\n${matchResult}`)
        location.href = '/';
    })
}

async function endGame() {
    fetch('/game/' + id + '/end', {
        method: 'PUT'
    }).then(async res => {
        if (res.status === 204) {
            alert(id + '번 방 게임이 종료되었습니다.')
            location.href = '/';
        }
        const json = await res.json()
        alert(json.message);
    })
}
