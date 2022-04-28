const body = document.getElementsByTagName('body')[0]
body.addEventListener('click', onClick)
const squareIdList = [];

const startButtons = document.getElementsByClassName('start-button');
for (const startButton of startButtons) {
    startButton.addEventListener('click', async () => {
        await putNewBoard();
        await allocateAllPiece();
    });
}

async function putNewBoard() {
    let id = location.href.split("/").pop();
    const res = await fetch("../new-board/"+id, {
        method: 'PUT'
    });
    if (!res.ok) {
        console.log("체스판 초기화 실패!")
    }
    return;
}

async function allocateAllPiece() {
    let id = location.href.split("/").pop();
    const res = await fetch("../board/"+id, {
        method: 'GET'
    });
    const pieces = await res.json();
    if (!res.ok) {
        alert("체스판 로드를 실패했습니다.");
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
        //TODO : 셀 선택 색깔 칠하기 버그
        //target.closest("td").classList.toggle("clicked")
        const table = target.parentElement.parentElement
        makeAllCellsNotClicked(table)
        return target.closest("td").id
    }

    async function postTwoCells() {
        if (squareIdList.length === 2) {
            const res = await postMove()
            if (!res.ok) {
                const data = await res.json();
                console.log(data.message)
                alert(data.message)
            }
            await allocateAllPiece()
        }
        async function postMove() {
            let id = location.href.split("/").pop();
            return await fetch('/move/' + id, {
                method: 'POST',
                cache: 'no-cache',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'to='+squareIdList.pop()+'&from='+squareIdList.pop()
            })
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

    async function onButtonClick(event) {
        const classList = target.classList
        let id = location.href.split("/").pop();
        if (classList.contains('status-button')) {
            location.href = "/status/" + id
        }
        if (classList.contains('board-button')) {
            location.href = "/board/" + id
        }
        if (classList.contains('end-button')) {
            location.href = '/game-end/' + id
        }
        if (classList.contains('referrer-button')) {
            location.href = document.referrer;
        }
        if (classList.contains('referrer-start-button')) {
            location.href = "/new-board/" + document.referrer.split("/").pop();
        }
    }
}
