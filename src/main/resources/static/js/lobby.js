function init() {
  const $startBtn = document.getElementById('start')
  const $loadBtn = document.getElementById('load')

  $startBtn.addEventListener('click', newGame)
  $loadBtn.addEventListener('click', loadGame)
}

async function newGame() {
  let response = await fetch(
    '/lobby/new'
  )
  response = await response.json()
  if (response.roomId > 0) {
    alert(`${response.roomId}번 게임에 입장합니다.`)
    window.location.href = `/${response.roomId}`
  }
}

async function loadGame() {
  const gameId = prompt('게임 번호를 입력해주세요.')
  alert(`${gameId}번 게임에 입장합니다.`)
  window.location.href = `/${gameId}`
}

window.onload = () => {
  init()
}