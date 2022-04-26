const createGameButton = document.getElementById("create-game-button");
const gamesDiv = document.getElementById("games");
const modal = document.getElementById("modal");
const closeButton = document.getElementById("close-button");
const createGameForm = document.getElementById("create-game-form");

window.onload = async () => {
  const res = await fetch("/games");
  const body = await res.json();
  body.games.forEach(game => {
    const gameDiv = document.createElement("ul");
    gameDiv.innerText = `${game.name}`;
    gameDiv.id = game.id;
    gameDiv.classList.add("game-info");
    gameDiv.addEventListener("click", enterGame);
    gamesDiv.appendChild(gameDiv);
  });
}

createGameButton.addEventListener("click", () => {
  modal.classList.remove("hidden");
});

closeButton.addEventListener("click", () => {
  modal.classList.add("hidden");
});

createGameForm.addEventListener("submit", createGame);

function enterGame(event) {
  const gameId = event.target.id;
  location.href = `/game/${gameId}`;
}

async function createGame() {
  modal.classList.add("hidden");
  const res = await fetch(`/games`, {
    method: "post",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      gameName: this.gameName.value,
      password: this.password.value,
    })
  });
  if (res.ok) {
    location.href = res.headers.get("Location");
    return;
  }
  const data = await res.json();
  alert(data.message);
  location.href = "/";
}
