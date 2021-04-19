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
    const $home = document.getElementById('home')
    const url = window.location.href.split('/')
    this.gameId = url[url.length - 1]

    $home.addEventListener('click', function () {
        window.location.href = '/'
    })

    await initBoard(await start())
    await moveHandler()
    await changeTurn()
    await result()
    await finishHandler()
    await toggleAvatar()
}

async function initBoard(chessBoard) {
    this.$chessBoard.innerHTML = ''
    chessBoard = await chessBoard.json()
    chessBoard = chessBoard.positionAndPieceName
    for (const [position, piece] of Object.entries(chessBoard)) {
        this.$chessBoard.insertAdjacentHTML('beforeend',
            insertPiece(position, piece)
        )
    }
}

async function start() {
    return await fetch(
        `/chessboard/${this.gameId}`
    )
}

function insertPiece(position, piece) {
    return `<div id=${position} class='square ${positionColor(position)} ${piece}'>
                <img class='piece' src=${imageMap[piece]} alt=${piece}/>
            </div>`
}

async function toggleAvatar() {
    const blackScore = this.$blackResult.querySelector('.score').textContent
    const whiteScore = this.$whiteResult.querySelector('.score').textContent

    if (blackScore > whiteScore) {
        this.$blackResult.getElementsByTagName(
            'img')[0].src = "./images/player_win.png"
    }
    if (whiteScore > blackScore) {
        this.$whiteResult.getElementsByTagName(
            'img')[0].src = "./images/player_win.png"
    }
    if (blackScore === whiteScore) {
        this.$blackResult.getElementsByTagName(
            'img')[0].src = './images/player_lose.png'
        this.$whiteResult.getElementsByTagName(
            'img')[0].src = './images/player_lose.png'
    }
}

function removeTurn() {
    document.querySelector('.black-turn').src = './images/up.png'
    document.querySelector('.white-turn').src = './images/down.png'
}

async function finishHandler() {
    let response = await fetch(
        `/finishById/${this.gameId}`
    )
    response = await response.json()
    if (response.finished === true) {
        const $modal = document.querySelector('.game')
        $modal.style.display = null
        $modal.querySelector('label').textContent = '게임이 종료되었습니다.'
        document.querySelector('#finish').disabled = false
        setTimeout(() => {
            $modal.style.display = 'none'
        }, 1500)
        await deactivateDrag()
        await toggleFinish()
        await toggleAvatar()
        removeTurn()
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
        `/scoreById/${this.gameId}`
    )
    response = await response.json()
    const blackScore = response.blackScore
    const whiteScore = response.whiteScore

    this.$blackResult.querySelector(
        '.score').textContent = blackScore
    this.$whiteResult.querySelector(
        '.score').textContent = whiteScore
}

async function btnHandler({target}) {
    const url = window.location.href.split('/')
    const gameId = url[url.length - 1]
    if (target.id === 'home') {
        window.location.href = '/'
        return
    }

    if (target.id === 'restart') {
        const response = await fetch(
            `/restart/${gameId}`,
            {
                method: 'POST'
            }
        )
        const $modal = document.querySelector('.game')
        $modal.style.display = null
        $modal.querySelector('label').textContent = '게임을 초기화합니다.'
        document.querySelector('#finish').disabled = false
        setTimeout(() => {
            $modal.style.display = 'none'
        }, 1500)
        await initBoard(response)
        await moveHandler()
        await changeTurn()
        await result()
        await finishHandler()
        await toggleAvatar()
    }
    if (target.id === "finish") {
        const response = await finish()
        if (response.status === 200) {
            await finishHandler()
            removeTurn()
        }
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
}

function moveHandler() {
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
        await result()
        await finishHandler()
        return
    }
    const $modal = document.querySelector('.game')
    $modal.style.display = null
    $modal.querySelector('label').textContent = '잘못된 이동입니다.'
    document.querySelector('#finish').disabled = false
    setTimeout(() => {
        $modal.style.display = 'none'
    }, 1000)
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
    await result()
}