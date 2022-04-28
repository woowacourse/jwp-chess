const modal = document.getElementById('modal');

const modalOpenButton = document.getElementById('find-game-button');
modalOpenButton.addEventListener('click', async () => {
  modal.style.visibility = 'visible';
  await findAllGames();
});

const modalCloseButton = document.getElementById('box__close-button');
modalCloseButton.addEventListener('click', () => {
  modal.style.visibility = 'hidden';
});

async function findAllGames() {
  const res = await fetch("/games", {
    method: 'GET'
  });
  const games = await res.json();
  if (!res.ok) {
    alert("방 목록을 불러올 수 없습니다.");
    return;
  }
  loadAllGames(games);
}

function loadAllGames(games) {
  const gamesBox = document.getElementById('modal__games');
  deleteAllGames(gamesBox);
  games.map(({id, name}) => {
    gamesBox.appendChild(createGame(id, name));
  });
}

function deleteAllGames(gamesBox) {
  gamesBox.innerHTML = '';
}


function createGame(id, name) {
  const game = document.createElement('div');
  game.className = 'games__game';
  game.innerHTML = `
        <span class="game__title" id=${id}>${name}</span>
        <button type="button" class="box__close-button" id="${id}">X</button>
    `;
  return game;
}

document.addEventListener('click', async (e) => {
  const {target: {className, id}} = e;
  console.log(className, id);
  if (className === 'game__title') {
    await enterGame(id);
  } else if (className === 'box__close-button') {
    await deleteGame(id);
  }
});

async function deleteGame(gameId) {
  const password = inputPassword();
  const requestJson = JSON.stringify({gameId, password});
  const res = await fetch(`/games/existed-game/${gameId}`, {
    method: 'DELETE',
    headers: {
      'content-type': 'application/json'
    },
    body: requestJson
  });
  if (res.status === 204) {
    deleteGameFromList(gameId);
    return;
  }
  const {message} = await res.json();
  alert(message);
}

function deleteGameFromList(gameId) {
  const oldGames = document.querySelectorAll('.games__game');
  const gamesAfterDelete = Array.from(oldGames).filter((node) => {
    return node.firstElementChild.id !== gameId
  });
  const modal = document.getElementById('modal__games');
  modal.innerHTML = '';
  gamesAfterDelete.forEach(node => modal.appendChild(node));
}

async function enterGame(gameId) {
  const password = inputPassword();
  const requestJson = JSON.stringify({
    "gameId": gameId,
    password
  });

  const res = await fetch(`/games/existed-game/${gameId}`, {
    method: "POST",
    headers: {
      'content-type': 'application/json'
    },
    body: requestJson
  });
  if (res.status === 204) {
    location.href = `/game/${gameId}`;
    return;
  }
  const {message} = await res.json();
  alert(message);
}

function inputPassword() {
  return prompt("Please input chess game password.");
}




