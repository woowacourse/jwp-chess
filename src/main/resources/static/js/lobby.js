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
        '/rooms'
    )
    response = await response.json()
    rooms = response.roomList
    for (const [id, name] of Object.entries(rooms)) {
        $roomList.insertAdjacentHTML('beforeend',
            await roomTemplate(id, name)
        )

    }
}

async function roomTemplate(id, name) {
    score = await roomScore(name)
    isFinished = await roomFinished(name)
    id = await findRoomId(name)

    return `<div class="room-info-item">
                <div class="room-info">
                    <div class="room-status">
                        <img style="max-width: 2rem" alt="black-score" src="../images/pawn_black.png"/>
                        <span>${score.blackScore}</span>
                        <img style="max-width: 2rem" alt="white-score" src="../images/pawn_white.png"/>
                        <span style="margin-right: 1rem">${score.whiteScore}</span>
                        <img class="playing" style="max-width: 2rem" alt="playing" src="../images/chess_clock.png"/>
                        <span class="${isFinished.finished ? "finished" : "playing"}">
                            ${isFinished.finished ? 'FINISHED' : 'PLAYING'}
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

async function roomScore(roomName) {
    let score = await fetch(
        `/scoreByName/${roomName}`
    )
    score = await score.json()
    return score
}

async function roomFinished(roomName) {
    let finished = await fetch(
        `/finishByName/${roomName}`
    )
    finished = await finished.json()
    return finished
}

async function findRoomId(roomName) {
    let response = await fetch(
        `/findRoomId`,
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
    response = await response.json()
    return response.roomId
}

function parseRoomName(name) {
    return name.replaceAll(' ', '%20')
}

function routeToRoom(id) {
    window.location.href = `${id}`
}

async function modalHandler(e) {
    const $modal = document.querySelector('.modal')
    const $nameDuplicate = document.querySelector('#name-duplicate')
    const $nameLength = document.querySelector('#name-length')
    const $blank = document.querySelector('#blank')
    const $chessImage = document.querySelector('#chess-image')
    $nameDuplicate.style.display = 'none'
    $nameLength.style.display = 'none'
    $blank.style.display = null
    $chessImage.style.opacity = '.4'

    $modal.id = e.target.id
    $modal.style.display = 'flex'
}

async function enterHandler(e) {
    const roomName = document.querySelector('#room-name').value
    const $nameDuplicate = document.querySelector('#name-duplicate')
    const $nameLength = document.querySelector('#name-length')
    const $blank = document.querySelector('#blank')
    $blank.style.display = null

    const action = e.path[2].id

    if (action === 'start') {
        let response = await fetch(
            '/isDuplicate',
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
        response = await response.json()
        if (response.duplicate === false) {
            response = await fetch(
                '/lobby/new',
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
            response = await response.json()
            window.location.href = `/${response.roomId}`
            return;
        }

        if (response.duplicate === true) {
            $nameDuplicate.style.display = null
            $nameDuplicate.textContent = "이미 존재하는 방 이름입니다."
            $nameLength.style.display = "none"
            $blank.style.display = "none"
            document.querySelector('#room-name').value = ''
            return;
        }
    }
}

function cancelHandler() {
    const $modal = document.querySelector('.modal')
    const $chessImage = document.querySelector('#chess-image')
    $modal.style.display = 'none'
    $chessImage.style.opacity = '1'
}

async function selectHandler(e) {
    const roomName = e.target.value
    if (roomName === 'none') {
        document.querySelector('.room-info').style.display = 'none'
        return
    }
    await showRoomInfo(roomName)
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