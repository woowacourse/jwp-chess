window.onload = () => {
    init()
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

async function init() {
    this.$chessBoard = document.querySelector('.chessBoard')
    this.$controller = document.querySelector('.controller')

    this.$controller.addEventListener('click', btnHandler)
    this.$blackResult = document.getElementById('BLACK')
    this.$whiteResult = document.getElementById('WHITE')
    this.$whiteResult.style.display = "none";
    this.$blackResult.style.display = "none";
    const url = window.location.href.split('/')

    this.gameId = url[url.length - 1]
    await initBoard()
    await finishHandler()
    await moveHandler()
    await changeTurn()

}

async function initBoard() {
    this.$chessBoard.innerHTML = ''
    let chessBoard = await fetch(
        `/chessboard/${this.gameId}`
    )
    chessBoard = await chessBoard.json()
    chessBoard = chessBoard.positionAndPieceName
    for (const [position, piece] of Object.entries(chessBoard)) {
        this.$chessBoard.insertAdjacentHTML('beforeend',
            insertPiece(position, piece)
        )
    }
}

function insertPiece(position, piece) {
    return `<div id=${position} class='square ${positionColor(position)} ${piece}'>
                <img class='piece' src=${imageMap[piece]} alt=${piece}/>
            </div>`
}

async function finishHandler() {
    let response = await fetch(
        `/finish/${this.gameId}`
    )
    response = await response.json()
    if (response.finished === true) {
        await deactivateDrag()
        alert('게임이 종료되었습니다.')
        toggleFinish()
    }
}

function deactivateDrag() {
    document.querySelector('.chessBoard').removeEventListener("drag", drag);
    document.querySelector('.chessBoard').removeEventListener("dragstart", dragstart);
    document.querySelector('.chessBoard').removeEventListener("dragend", dragend);
    document.querySelector('.chessBoard').removeEventListener("dragover", dragover);
    document.querySelector('.chessBoard').removeEventListener("dragenter", dragenter);
    document.querySelector('.chessBoard').removeEventListener("dragleave", dragleave);
    document.querySelector('.chessBoard').removeEventListener("drop", drop);
}

function positionColor(position) {
    if (position[1] % 2 === 0) {
        return position[0].charCodeAt(0) % 2 === 0 ? 'b-white' : 'b-black'
    }
    return position[0].charCodeAt(0) % 2 === 0 ? 'b-black' : 'b-white'
}

async function changeTurn() {
    let response = await fetch(
        `/turn/${this.gameId}`
    )
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
    let response = await fetch(
        `/${this.gameId}/result`
    )
    response = await response.json()

    const blackResult = response.black
    const whiteResult = response.white

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

async function btnHandler({target}) {
    if (target.id === 'restart') {
        await newGame()
    }
    if (target.id === "finish") {
        const response = await finish()
        if (response.status === 200) {
            await finishHandler()
        }
    }
    if (target.id === "status") {
        await result()
        target.disabled = true;
    }
}

async function finish() {
    const url = window.location.href.split('/')
    const gameId = url[url.length - 1]
    return await fetch(
        `/finish/${gameId}`,
        {
            method: 'POST'
        }
    )
}

function toggleFinish() {
    document.querySelector('#finish').disabled = true
    document.querySelector('#status').disabled = false
}

function moveHandler() {
    let source
    let target
    this.$chessBoard.addEventListener("drag", drag, false);
    this.$chessBoard.addEventListener("dragstart", dragstart, false);
    this.$chessBoard.addEventListener("dragend", dragend, false);
    this.$chessBoard.addEventListener("dragover", dragover, false);
    this.$chessBoard.addEventListener("dragenter", dragenter, false);
    this.$chessBoard.addEventListener("dragleave", dragleave, false);
    this.$chessBoard.addEventListener("drop", drop, false);
}

async function move(source, target) {
    let response = await fetch(
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
    if (response.status === 200) {
        assignPieceImage(source, target)
        await changeTurn()
        await finishHandler()
        return
    }
    alert("잘못된 이동입니다.")
}

function assignPieceImage(source, target) {
    const $source = document.getElementById(source.id)
    const $target = document.getElementById(target.id)

    $target.outerHTML = insertPiece(target.id, document.getElementById(source.id).classList.item(2))
    $source.outerHTML = insertPiece(source.id, '.')
}

drag = function (e) {
}
dragstart = function (e) {
    source = e.target.closest('div')
}
dragend = function (e) {
}
dragover = function (e) {
    e.preventDefault();
}
dragenter = function (e) {
    e.target.style.background = "#78c2c6";
}
dragleave = function (e) {
    e.target.style.background = "";
}
drop = async function (e) {
    e.preventDefault();
    e.target.style.background = "";
    target = e.target.closest('div')
    await move(source, target)
}
