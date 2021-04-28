function init() {
    const $startBtn = document.getElementById('start')
    const $loadBtn = document.getElementById('load')
    const $exitBtn = document.getElementById('exit')
    const $modalBtn = document.getElementById('enter')
    const $modalExitBtn = document.getElementById('cancel')
    $startBtn.addEventListener('click', modalHandler)
    $loadBtn.addEventListener('click', loadHandler)
    $exitBtn.addEventListener('click', exitHandler)
    $modalBtn.addEventListener('click', enterHandler)
    $modalExitBtn.addEventListener('click', cancelHandler)

    this.roomId = 0;

    showRoomList()
}

async function showRoomList() {
    const $roomList = document.querySelector('.room-list ')
    let response = await fetch(
        '/games'
    )
    response = await response.json()
    rooms = response.roomList
    for (const room of rooms) {
        $roomList.insertAdjacentHTML('beforeend',
            await roomTemplate(room)
        )
    }
}

async function roomTemplate(room) {
    const id = room.id
    const name = room.title.trim()
    const blackScore = room.blackScore
    const whiteScore = room.whiteScore
    const isFinished = room.finished

    return `<div class="room-info-item">
                <div class="room-info">
                    <div class="room-status">
                        <img style="max-width: 2rem" alt="black-score" src="../images/pawn_black.png"/>
                        <span>${blackScore}</span>
                        <img style="max-width: 2rem" alt="white-score" src="../images/pawn_white.png"/>
                        <span style="margin-right: 1rem">${whiteScore}</span>
                        <img class="playing" style="max-width: 2rem" alt="playing" src="../images/chess_clock.png"/>
                        <span class="${isFinished ? "finished" : "playing"}">
                            ${isFinished ? 'FINISHED' : 'PLAYING'}
                        </span>
                    </div>
                    <div class="room-name">
                        <span class="room-name">${name}</span>     
                    </div> 
                </div>
                <button style="display: block" class="room-btn" onclick="routeToRoom(${id})">
                    <i class="fas fa-play"></i>
                </button>     
            </div>`
}

function routeToRoom(id) {
    window.location.href = `/game/${id}`
}

async function modalHandler(e) {
    const $modal = document.querySelector('.modal')
    const $nameDuplicate = document.querySelector('#name-duplicate')
    const $nameLength = document.querySelector('#name-length')
    const $blank = document.querySelector('#blank')
    const $chessImage = document.querySelector('#chess-image')
    const $controller = document.querySelector('.controller')
    $nameDuplicate.style.display = 'none'
    $nameLength.style.display = 'none'
    $blank.style.display = null
    $chessImage.style.opacity = '.4'
    $controller.style.opacity = '.4'
    $controller.childNodes.forEach(ctrl => ctrl.disabled = true)

    $modal.id = e.target.id
    $modal.style.display = 'flex'
}

async function enterHandler(e) {
    const $nameDuplicate = document.querySelector('#name-duplicate')
    const $nameLength = document.querySelector('#name-length')
    const $blank = document.querySelector('#blank')
    $blank.style.display = null

    let roomName = document.querySelector('#room-name').value
    roomName = roomName.trim()

    const action = e.path[2].id

    if (action === 'start') {
        response = await fetch(
            '/game',
            {
                method: 'POST',
                body: JSON.stringify({
                    'title': roomName
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8',
                    'Accept': 'application/json'
                }
            }
        )
        if (response.status === 400) {
            response = await response.text()
            $nameLength.textContent = response
            $nameDuplicate.style.display = "none"
            $blank.style.display = "none"
            $nameLength.style.display = null
            document.querySelector('#room-name').value = ''
            return;
        }
        const id = await response.json()
        window.location.href = `/game/${id}`
        return;
    }
}

function cancelHandler() {
    const $modal = document.querySelector('.modal')
    const $chessImage = document.querySelector('#chess-image')
    const $controller = document.querySelector('.controller')
    $modal.style.display = 'none'
    $chessImage.style.opacity = '1'
    $controller.style.opacity = '1'
    $controller.childNodes.forEach(ctrl => ctrl.disabled = false)
}

function loadHandler() {
    document.querySelector('.main').style.display = 'none'
    document.querySelector('.list').style.display = 'flex'

}

function exitHandler() {
    document.querySelector('.main').style.display = 'flex'
    document.querySelector('.list').style.display = 'none'
}


window.onload = () => {
    init()
}