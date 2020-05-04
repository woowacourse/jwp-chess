window.onload = function () {
  document.querySelector("#new").addEventListener("click", () => {
    let gameName = prompt("게임 이름을 입력하세요.");
    fetch("http://localhost:8080/new", {
      method: "POST",
      body: JSON.stringify({
        gameName: gameName
      }),
      headers: {"Content-Type": "application/json"}
    })
      .then(async function (response) {
        return await response.json();
      })
      .then(data => {
        console.log(data);
        let gameId = data["gameId"];
        window.location.href = `/game/${gameId}`;
      });
  });

  function showGames() {
    fetch("http://localhost:8080/games")
      .then(res => res.json())
      .then(games => {
        const loadingDiv = document.querySelector("#loading");
        loadingDiv.innerHTML += generateGames(games, Object.keys(games["games"]));
      });
  }

  function generateGames(games, keys) {
    console.log(games);
    console.log(keys);
    return keys.map(key => `<div class="game info"> <a href=/game/${key}>${games["games"][key]}</a></div>`)
      .join("");
  }

  showGames();
};