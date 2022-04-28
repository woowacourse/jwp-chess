const gameListButton = document.getElementById("gameList");
const newGameButton = document.getElementById("newGame");
const resumeGameButton = document.getElementById("resumeGame");

gameListButton.addEventListener("click", async function () {
  let games = await fetch("/games", {
    method: "GET"
  }).then(handleErrors)
  .catch(function (error) {
    alert(error.message);
  })
  games = await games.json();

  const size = games.length;
  let trList = "";
  for (let i = 0; i < size; i++) {
    trList += toDom(games[i], i + 1);
  }
  document.querySelector("#chess-game-table"
      + " thead").innerHTML = "<tr><th>번호</th><th>이름</th><th>삭제</th></tr>";
  document.querySelector("#chess-game-table" + " tbody").innerHTML = trList;
});

function toDom(game, no) {
  let tr = "";
  tr += "<tr>";
  tr += "  <td>" + no + "</td>";
  tr += "  <td>" + game.name + "</td>";
  tr += "  <td><button type=button onclick=deleteGame(this)>삭제</button></td>";
  tr += "  <td style='display: none'><input type=hidden id='" + no + "' value="
      + game.id + "></td>"
  tr += "</tr>";
  return tr;
}

async function deleteGame(game) {
  const tr = $(game).parent().parent();
  const td = tr.children();

  let no = td.eq(0).text();
  let id = document.getElementById(no).value;

  await fetch("/game", {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      id: id,
      password: prompt("비밀번호를 입력해주세요")
    }),
  }).then(handleErrors)
  .catch(function (error) {
    alert(error.message);
  });
  tr.remove();
  location.href = "/";
}

newGameButton.addEventListener("click", function () {
  window.location.href = "/game";
});

async function handleErrors(response) {
  if (!response.ok) {
    let errorMessage = await response.json();
    throw Error(errorMessage.message);
  }
  return response;
}
