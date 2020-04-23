const templatePiece = color => `<div class="chess-piece ${color}" draggable="true" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateBlank = `<div class="chess-piece blank" draggable="true" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateGame = id => `<a href="/rooms/${id}" class="chess-game"><div class="chess-game-title">체스 게임 ${id}</div></a>`

const blackScoreElement = document.querySelector('#black-score')
const whiteScoreElement = document.querySelector('#white-score')
const chessAlertElement = document.querySelector('.chess-alert')
const chessAlertMessageElement = document.querySelector('.chess-alert-message')

const chessResultElement = document.querySelector('.chess-result')
const chessResultMessageElement = document.querySelector('.chess-result-message')
const chessResultRestartElement = document.querySelector('.chess-result-restart')
const chessResultDeleteElement = document.querySelector('.chess-result-delete')

const chessGamesElement = document.querySelector('.chess-games')
const chessCreateSubmitElement = document.querySelector('.chess-create-submit')

const chessCellElements = document.querySelectorAll('.chess-col')

chessCreateSubmitElement.onclick = () => {
    fetch('/rooms', {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            if (data.statusCode == 1) {
                location.replace('/rooms/' + data.dto)
            }
        })
}

chessResultRestartElement.onclick = () => {
    fetch(`/rooms/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
        .then(response => response.json())
        .then(data => {
            if (data.statusCode == 1) {
                location.reload()
            }
        })
}

chessResultDeleteElement.onclick = () => {
    fetch(`/rooms/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
        .then(response => response.json())
        .then(data => {
            if (data.statusCode == 1) {
                location.replace('/')
            }
        })
}

function showAlert(message, delay) {
    show(chessAlertElement)
    chessAlertMessageElement.innerHTML = message
    setTimeout(() => {
        hide(chessAlertElement)
    }, delay)
}

function show(element) {
    element.classList.remove('hide')
    element.classList.add('show')
}

function hide(element) {
    element.classList.remove('show')
    element.classList.add('hide')
}

fetch(`/rooms/${id}/board`)
    .then(response => response.json())
    .then(data => drawChessGame(data.dto))

fetch('/rooms')
    .then(response => response.json())
    .then(data => drawGameList(data.dto))

function drawGameList(games) {
    games.forEach(id => chessGamesElement.innerHTML += templateGame(id))
}

function drawChessGame({boardDto, turnDto, statusDto, isFinished}) {
    drawBoard(boardDto.board)
    drawStatus(statusDto)
    drawResult(statusDto, isFinished)
}

function drawResult(statusDto, isFinished) {
    if (!isFinished) {
        return
    }
    show(chessResultElement)
    chessResultMessageElement.innerHTML = statusDto.winner + ' 승'
}

function drawBoard(board) {
    chessCellElements.forEach((element, i) => {
        const symbol = board[i]
        element.innerHTML = symbol == '.' ? templateBlank : templatePiece(symbol)
    })
}

function drawStatus(statusDto) {
    blackScoreElement.innerHTML = statusDto.black
    whiteScoreElement.innerHTML = statusDto.white
}

function onDragStart(e) {
    e.dataTransfer.setData('src_pos', e.target.parentElement.dataset.pos)
}

function onDragOver(e) {
    e.preventDefault()
}

function onDrop(e) {
    fetch(`/rooms/${id}/board`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `source=${e.dataTransfer.getData('src_pos')}&target=${e.target.parentElement.dataset.pos}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.statusCode === 1) {
                drawChessGame(data.dto)
                return
            }
            showAlert(data.dto, 1000)
        })
}