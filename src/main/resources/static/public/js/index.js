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
      .then(async function(response) {
        return await response.json();
      })
      .then(data => {
        console.log(data);
        let gameId = data["gameId"];
        window.location.href = `/game/${gameId}`;
      });
  });
};