function init() {
    const $startBtn = document.getElementById('start')
    const $loadBtn = document.getElementById('load')
    const $modalBtn = document.getElementById('enter')
    $startBtn.addEventListener('click', newGame)
    $loadBtn.addEventListener('click', loadGame)
    $modalBtn.addEventListener('click', enterHandler)

    this.roomId = 0;
}

async function newGame() {
    const $modal = document.querySelector('.modal')
    $modal.style.display = 'flex'
}

async function loadGame() {
    const $modal = document.querySelector('.modal')
    $modal.style.display = 'flex'
}

async function enterHandler() {
    const roomName =  document.querySelector('#room-name').value
    const $nameDuplicate = document.querySelector('#name-duplicate')
    const $nameLength = document.querySelector('#name-length')

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
    console.log(response)
    // if (response.status == 200) {
    //     $nameDuplicate.style.display = null
    //     $nameLength.style.display ="none"
    //     document.querySelector('#room-name').value = ''
    // }
    // if (response.status === 400) {
    //     response = await response.text()
    //
    //     if (response) {
    //         $nameLength.textContent = response
    //         $nameDuplicate.style.display = "none"
    //         $nameLength.style.display = null
    //         document.querySelector('#room-name').value = ''
    //     }
    //
    //     response = await fetch(
    //         '/lobby/new',
    //         {
    //             method: 'POST',
    //             body: JSON.stringify({
    //                 'title': roomName
    //             }),
    //             headers: {
    //                 'Content-type': 'application/json; charset=UTF-8',
    //                 'Accept': 'application/json'
    //             }
    //         }
    //     )
    //     response = await response.json()
    //     window.location.href = `/${response.roomId}`
    // }
}

window.onload = () => {
    init()
}