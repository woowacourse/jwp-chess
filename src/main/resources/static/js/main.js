async function init() {
    this.$chessBoard = document.querySelector('.chessBoard')
    this.$controller = document.querySelector('.controller')
    this.$controller.addEventListener('click', btnHandler)

    this.$blackResult = document.getElementById('BLACK')
    this.$whiteResult = document.getElementById('WHITE')
    displayResult('none')

    const url = window.location.href.split('/')
    this.gameId = url[url.length - 1]

    await setBoard()
    // await checkFinished()
    await moveHandler()
    await changeTurn(await getTurn())
}

async function getBoard() {
    return await fetch(
        `/chessboard/${this.gameId}`
    )
}

async function setBoard() {
    this.$chessBoard.innerHTML = ''
    let chessBoard = await getBoard()
    chessBoard = await chessBoard.json()
    chessBoard = chessBoard.positionAndPieceName
    for (const [position, piece] of Object.entries(chessBoard)) {
        this.$chessBoard.insertAdjacentHTML('beforeend',
            boardTemplate(position, piece)
        )
    }
}

function boardTemplate(position, piece) {
    return `<div id=${position}>
  ${squareTemplate(position, piece)}
  </div>`
}

function squareTemplate(position, piece) {
    return `<div id=${position} class='square ${positionColor(
        position)} ${piece}'>
    <img class='piece' src=${imageMap[piece]} alt=${piece}/>
    </div>`
}

imageMap = {
    "R": './images/rook_black.png',
    "B": './images/bishop_black.png',
    "Q": './images/queen_black.png',
    "N": './images/knight_black.png',
    "P": './images/pawn_black.png',
    "K": './images/king_black.png',
    "r": './images/rook_white.png',
    "p": './images/pawn_white.png',
    "b": './images/bishop_white.png',
    "q": './images/queen_white.png',
    "n": './images/knight_white.png',
    "k": './images/king_white.png',
    ".": './images/blank.png'
}

async function checkFinished() {
    const finished = await fetch(
        `${this.gameId}/finish`
    ).then(res => res.json())
        .then(data => data)
    if (finished === true) {
        alert('게임이 종료되었습니다.')
        toggleFinish()
    }
}

async function getTurn() {
    return await fetch(
        `/turn/${this.gameId}`
    )
}

function moveHandler() {
    let source
    let target
    this.$chessBoard.addEventListener("drag", function (e) {
    }, false);

    this.$chessBoard.addEventListener("dragstart", function (e) {
        source = e.target.closest('div').parentNode
    }, false);

    this.$chessBoard.addEventListener("dragend", function (e) {
    }, false);

    this.$chessBoard.addEventListener("dragover", function (e) {
        e.preventDefault();
    }, false);

    this.$chessBoard.addEventListener("dragenter", function (e) {
        e.target.style.background = "#78c2c6";
    }, false);

    this.$chessBoard.addEventListener("dragleave", function (e) {
        e.target.style.background = "";
    }, false);

    this.$chessBoard.addEventListener("drop", async function (e) {
        e.preventDefault();
        e.target.style.background = "";
        target = e.target.closest('div').parentNode
        console.log(source, target)
        let response = await movable(source, target)
        if (response.status === 200) {
            move(source, target)
            await changeTurn(await getTurn())
            return
        }
        if (response.status === 400) {
            alert("잘못된 이동입니다.")
        }
    }, false);
}

function move(source, target) {
    const $source = document.getElementById(source.id)
    const $target = document.getElementById(target.id)
    $target.innerHTML = squareTemplate(target.id, document.getElementById(source.id).firstElementChild.classList.item(2))
    $source.innerHTML = squareTemplate(source.id, '.')

}

async function movable(source, target) {
    console.log(source.id)
    console.log(source.piece)
    return await fetch(
        `/move/${this.gameId}`,
        {
            method: 'PUT',
            body: JSON.stringify({
                source: source.id,
                target: target.id
            }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Accept': 'application/json'
            }
        }
    )
}


function positionColor(position) {
    if (position[1] % 2 === 0) {
        return position[0].charCodeAt(0) % 2 === 0 ? 'b-white' : 'b-black'
    }
    return position[0].charCodeAt(0) % 2 === 0 ? 'b-black' : 'b-white'
}

async function changeTurn(response) {
    response = await response.json()
    const $blackTurn = document.querySelector('.black-turn')
    const $whiteTurn = document.querySelector('.white-turn')
    if (response.turn === 'WHITE') {
        $blackTurn.src = 'images/up.png'
        $whiteTurn.src = 'images/down_turn.png'
    }
    if (response.turn === 'BLACK') {
        $blackTurn.src = 'images/up_turn.png'
        $whiteTurn.src = 'images/down.png'
    }
}

async function result() {
    const result = await fetch(
        `/${this.gameId}/result`
    ).then(res => res.json()).then(data => data)

    const blackResult = result.black
    const whiteResult = result.white

    this.$blackResult.getElementsByClassName(
        'score')[0].innerHTML = `<span>${blackResult.score}</span>`
    this.$whiteResult.getElementsByClassName(
        'score')[0].innerHTML = `<span>${whiteResult.score}</span>`

    if (blackResult.outcome === '승') {
        this.$blackResult.getElementsByTagName(
            'img')[0].src = "./images/player_win.png"
        this.$whiteResult.getElementsByTagName(
            'img')[0].src = "./images/player_lose.png"
        return
    }
    if (blackResult.outcome === '패') {
        this.$blackResult.getElementsByTagName(
            'img')[0].src = "./images/player_lose.png"
        this.$whiteResult.getElementsByTagName(
            'img')[0].src = "./images/player_win.png"
        return
    }
    this.$blackResult.getElementsByTagName(
        'img')[0].src = "./images/player_lose.png"
    this.$whiteResult.getElementsByTagName(
        'img')[0].src = "./images/player_lose.png"
}

function displayResult(val) {
    this.$blackResult.style.display = val;
    this.$whiteResult.style.display = val;
}

async function btnHandler({target}) {
    if (target.id === 'restart') {
        await newGame()
    }
    if (target.id === "finish") {
        if (await finish() === 202) {
            toggleFinish()
        }
    }
    if (target.id === "status") {
        displayResult('flex')
        await result()
        target.disabled = true;
    }
}

async function finish() {
    const url = window.location.href.split('/')
    const gameId = url[url.length - 1]
    return await fetch(
        `${gameId}/finish`,
        {
            method: 'POST'
        }
    ).then(res => res.json())
}

function toggleFinish() {
    document.querySelector('#finish').disabled = true
    document.querySelector('#status').disabled = false
}

window.onload = () => {
    init()
}

