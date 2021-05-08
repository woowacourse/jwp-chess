window.onload = () => {
    init()
}

async function init() {
    this.gameId = document.getElementById('gameId').innerText;

    const pieces = document.getElementsByClassName('piece');
    Array.from(pieces).forEach((el) => {
        el.addEventListener('click', click);
    })

    const home = document.getElementById('home')
    home.addEventListener('click', () => window.location.href = '/')

    let chessStatus = await start()
    chessStatus = await chessStatus.json()
    await setBoard(chessStatus)
    await setScore(chessStatus)
    await setTurn(chessStatus)
}

let state = "non-clicked";
let source = "";
let target = "";

async function click(event) {
    if (state === "non-clicked") {
        source = event.target.id;
        state = "clicked";
        return;
    }

    if (state === "clicked") {
        console.log(event.target.id)
        await clickWhereToMove(event.target);
        source = "";
        target = "";
        state = "stay";
    }
}

async function clickWhereToMove(eventTarget) {
    target = eventTarget.id;
    await submitMove(source, target);
}

async function submitMove(src, tar) {
    await fetch(
        `/games/${this.gameId}/move`, {
            method: 'POST',
            body: JSON.stringify({
                from: src,
                to: tar
            }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Accept': 'application/json'
            }
        }
    ).then(response => {
            if (response.status !== 200) {
                response.text()
                    .then(text => alert(text))
            }
            location.replace(`/games/${this.gameId}`)
        }
    )
}

async function start() {
    return await fetch(
        `/games/${this.gameId}/load`
    )
}

async function setBoard(chessStatus) {
    for (const [position, piece] of Object.entries(chessStatus.boardDto.maps)) {
        document.getElementById(position).textContent = piece;
    }
}

async function setScore(chessStatus) {
    document.getElementById("blackScore").textContent = chessStatus.blackScore;
    document.getElementById("whiteScore").textContent = chessStatus.whiteScore;
}

async function setTurn(chessStatus) {
    document.getElementById("turn").textContent = chessStatus.turn;
}

