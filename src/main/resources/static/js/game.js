const $board = document.querySelector('#board');
const $gameId = document.querySelector('#gameId');
const $currentTurn = document.querySelector('#currentTurn');

const $gameSetResult = document.querySelector('#game-result');
const $winner = document.querySelector('#winner');
const $currentBlackScore = document.querySelector('#blackScore');
const $currentWhiteScore = document.querySelector('#whiteScore');

$board.addEventListener('click', onClickSquare)

let sourceId;
let turn;

function gameId() {
    return $gameId.value
}

function onClickSquare(e) {
    if (sourceId === null) {
        sourceId = e.target.id;
        document.getElementById(sourceId).style.backgroundColor = 'green';
        return;
    }
    move(sourceId, e.target.id, turn)
    document.getElementById(sourceId).style.backgroundColor = '';
}

function move(source, target, team) {
    $.ajax({
        type: "POST",
        url: "/chess/game/" + gameId() + "/move",
        data: {
            "source": source,
            "target": target,
            "team": team
        },
        dataType: "json",
        success: update,
        error: alertError,
        complete: initSource
    })
}

function initBoard() {
    $.ajax({
        type: "GET",
        url: "/chess/game/" + gameId() + "/board",
        dataType: "json",
        success: update,
        error: alertError,
        complete: initSource
    })
}

function update(response) {
    boardRender(response.board);
    $currentTurn.innerHTML = response.currentTurn;
    turn = response.currentTurn;
    $currentBlackScore.innerHTML = response.blackScore;
    $currentWhiteScore.innerHTML = response.whiteScore;
    console.log(response);
    if (response.gameSet) {
        showResult(response)
    }
}

function showResult(response) {
    $winner.innerHTML = response.winner

    $gameSetResult.classList.remove("none")
    $board.removeEventListener('click', onClickSquare)
}

function alertError(response) {
    alert(response.responseText);
}

function initSource() {
    sourceId = null;
}

initBoard();

function boardRender(board) {
    $board.innerHTML =
        `
<div class="square black ${board.A8}" id="A8"></div>
<div class="square white ${board.B8}" id="B8"></div>
<div class="square black ${board.C8}" id="C8"></div>
<div class="square white ${board.D8}" id="D8"></div>
<div class="square black ${board.E8}" id="E8"></div>
<div class="square white ${board.F8}" id="F8"></div>
<div class="square black ${board.G8}" id="G8"></div>
<div class="square white ${board.H8}" id="H8"></div>

<div class="square white ${board.A7}" id="A7"></div>
<div class="square black ${board.B7}" id="B7"></div>
<div class="square white ${board.C7}" id="C7"></div>
<div class="square black ${board.D7}" id="D7"></div>
<div class="square white ${board.E7}" id="E7"></div>
<div class="square black ${board.F7}" id="F7"></div>
<div class="square white ${board.G7}" id="G7"></div>
<div class="square black ${board.H7}" id="H7"></div>

<div class="square black ${board.A6}" id="A6"></div>
<div class="square white ${board.B6}" id="B6"></div>
<div class="square black ${board.C6}" id="C6"></div>
<div class="square white ${board.D6}" id="D6"></div>
<div class="square black ${board.E6}" id="E6"></div>
<div class="square white ${board.F6}" id="F6"></div>
<div class="square black ${board.G6}" id="G6"></div>
<div class="square white ${board.H6}" id="H6"></div>

<div class="square white ${board.A5}" id="A5"></div>
<div class="square black ${board.B5}" id="B5"></div>
<div class="square white ${board.C5}" id="C5"></div>
<div class="square black ${board.D5}" id="D5"></div>
<div class="square white ${board.E5}" id="E5"></div>
<div class="square black ${board.F5}" id="F5"></div>
<div class="square white ${board.G5}" id="G5"></div>
<div class="square black ${board.H5}" id="H5"></div>

<div class="square black ${board.A4}" id="A4"></div>
<div class="square white ${board.B4}" id="B4"></div>
<div class="square black ${board.C4}" id="C4"></div>
<div class="square white ${board.D4}" id="D4"></div>
<div class="square black ${board.E4}" id="E4"></div>
<div class="square white ${board.F4}" id="F4"></div>
<div class="square black ${board.G4}" id="G4"></div>
<div class="square white ${board.H4}" id="H4"></div>

<div class="square white ${board.A3}" id="A3"></div>
<div class="square black ${board.B3}" id="B3"></div>
<div class="square white ${board.C3}" id="C3"></div>
<div class="square black ${board.D3}" id="D3"></div>
<div class="square white ${board.E3}" id="E3"></div>
<div class="square black ${board.F3}" id="F3"></div>
<div class="square white ${board.G3}" id="G3"></div>
<div class="square black ${board.H3}" id="H3"></div>

<div class="square black ${board.A2}" id="A2"></div>
<div class="square white ${board.B2}" id="B2"></div>
<div class="square black ${board.C2}" id="C2"></div>
<div class="square white ${board.D2}" id="D2"></div>
<div class="square black ${board.E2}" id="E2"></div>
<div class="square white ${board.F2}" id="F2"></div>
<div class="square black ${board.G2}" id="G2"></div>
<div class="square white ${board.H2}" id="H2"></div>

<div class="square white ${board.A1}" id="A1"></div>
<div class="square black ${board.B1}" id="B1"></div>
<div class="square white ${board.C1}" id="C1"></div>
<div class="square black ${board.D1}" id="D1"></div>
<div class="square white ${board.E1}" id="E1"></div>
<div class="square black ${board.F1}" id="F1"></div>
<div class="square white ${board.G1}" id="G1"></div>
<div class="square black ${board.H1}" id="H1"></div>
`
}