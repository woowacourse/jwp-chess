function init() {
    const $startBtn = document.getElementById('start')
    const $modalBtn = document.getElementById('enter')
    const $modalExitBtn = document.getElementById('cancel')
    const $roomList = document.querySelector('.room-select > select')
    $startBtn.addEventListener('click', modalHandler)
    $modalBtn.addEventListener('click', enterHandler)
    $modalExitBtn.addEventListener('click', cancelHandler)
    $roomList.addEventListener('change', selectHandler)


    this.roomId = 0;

    showRoomList()
}

async function showRoomList() {
    const $roomList = document.querySelector('.room-select > select')
    let response = await fetch(
        '/rooms'
    )
    response = await response.json()
    rooms = response.roomList
    for (const [id, name] of Object.entries(rooms)) {
        $roomList.insertAdjacentHTML('beforeend',
            roomTemplate(id, name)
        )
    }
}

function roomTemplate(id, name) {
    return `<option id=${id} value=${parseRoomName(name)}>${name}</option>`
}

function parseRoomName(name){
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

async function showRoomInfo(roomName) {
    const $roomInfo = document.querySelector('.room-info')
    const $roomBtn = $roomInfo.querySelector('button')
    $roomInfo.style.display = null

    let score = await fetch(
        `/scoreByName/${roomName}`
    )
    score = await score.json()
    let finished = await fetch(
        `/finishByName/${roomName}`
    )
    finished = await finished.json()

    const blackScore = score.blackScore
    const whiteScore = score.whiteScore
    const isFinished = finished.finished
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
    roomId = response.roomId

    $roomInfo.querySelector('#black-score').textContent = `BLACK: ${blackScore}`
    $roomInfo.querySelector('#white-score').textContent = `WHITE: ${whiteScore}`
    $roomInfo.querySelector('#status').textContent = `STATUS: ${isFinished ? 'FINISHED' : 'ONGOING'}`
    $roomBtn.onclick = function () {
        window.location.href = `/${roomId}`
    }
}


window.onload = () => {
    init()
}