const gameListButton = document.getElementById("gameList");
const newGameButton = document.getElementById("newGame");

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
  document.querySelector("#chess-game-table thead").innerHTML =
      "<tr><th>번호</th><th>이름</th><th>삭제</th><th>상태</th></tr>";
  document.querySelector("#chess-game-table" + " tbody").innerHTML = trList;
});

function toDom(game, no) {
  const running = game.running === true ? "진행 중" : "종료";
  let tr = "";
  tr += "<tr>";
  tr += "  <td>" + no + "</td>";
  tr += "  <td onclick='loadGame(this)'>" + game.name + "</td>";
  tr += "  <td><button type=button onclick=deleteGame(this)>삭제</button></td>";
  tr += "  <td>" + running + "</td>";
  tr += "  <td style='display: none'><input type=hidden id='" + no + "' value="
      + game.id + "></td>"
  tr += "</tr>";
  return tr;
}

async function loadGame(object) {
  console.log(object);
  const tr = $(object).parent();
  const td = tr.children();
  const no = td.eq(0).text();
  const id = document.getElementById(no).value;

  location.href = "/load/" + id;
}

async function deleteGame(game) {
  const tr = $(game).parent().parent();
  const td = tr.children();
  const no = td.eq(0).text();
  const id = document.getElementById(no).value;

  const response = await fetch("/game", {
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

  if (response.status === 204) {
    tr.remove();
    location.href = "/";
  }
}

newGameButton.addEventListener("click", function () {
  location.href = "/new-game";
});

async function handleErrors(response) {
  if (!response.ok) {
    let errorMessage = await response.json();
    throw Error(errorMessage.message);
  }
  return response;
}
