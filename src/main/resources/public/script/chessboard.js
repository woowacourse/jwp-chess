window.onload = () => {
    init()
}

async function init() {
    const url = window.location.href.split('/')
    this.gameId = url[url.length-1]

    const $home = document.getElementById('home')
    $home.addEventListener('click', () => window.location.href='/')

    let chessGame = await start()
    chessGame = await chessGame.json()
    await setBoard(chessGame)
    await setScore(chessGame)
    await setTurn(chessGame)
}

async function start() {
    return await fetch(
        `/games/${this.gameId}/load`
    )
}

async function setBoard(chessGame) {
    for (const [position, piece] of Object.entries(chessGame.boardDto.maps)) {
        document.getElementById(position).textContent = piece;
    }
}

async function setScore(chessGame) {
    document.getElementById("blackScore").textContent = chessGame.blackScore;
    document.getElementById("whiteScore").textContent = chessGame.whiteScore;
}

async function setTurn(chessGame) {
    document.getElementById("turn").textContent = chessGame.turn;
}

