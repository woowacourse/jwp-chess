const randomStartButton = document.getElementById("random-start-button");
const gamesDiv = document.getElementById("games");

randomStartButton.addEventListener("click", async () => {
  const gameId = Math.floor(Math.random() * 10000);

  const res = await fetch(`/games/${gameId}`);
  if (!res.ok) {
    await create(gameId);
    location.href = `/game/${gameId}`;
  }
});

async function create(gameId) {
  const res = await fetch(`/games/${gameId}`, {
    method: "post"
  });
  if (!res.ok) {
    const data = await res.json();
    alert(data.message);
  }
}

window.onload = async function () {
  const res = await fetch("/games");
  const body = await res.json();
  body.gameIds.forEach(id => {
    const game = document.createElement("ul");
    game.innerText = `${id}번 게임`;
    game.id = id;
    game.classList.add("game-id");
    game.addEventListener("click", enterGame);
    gamesDiv.appendChild(game);
  });
}

function enterGame(event) {
  const gameId = event.target.id;
  location.href = `/game/${gameId}`;
}
