const randomStartButton = document.getElementById("random-start-button");
const gamesDiv = document.getElementById("games");

randomStartButton.addEventListener("click", () => {
  const gameId = Math.floor(Math.random() * 10000);
  location.href = `/game/${gameId}`;
});

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
