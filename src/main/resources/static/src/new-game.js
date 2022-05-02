const newGameButton = document.getElementById("newGame");

newGameButton.addEventListener("click", async function () {
  const gameName = document.getElementById("newRoomName").value;
  const password = document.getElementById("new-room-password").value;
  const passwordCheck = document.getElementById("new-room-password-check").value;
  const gameId = await createNewGame(gameName, password, passwordCheck);

  location.href = "/games/" + gameId + "/load";
});

async function createNewGame(gameName, password, passwordCheck) {
  if (gameName === '' || password === '' || passwordCheck === '') {
    alert("방 이름, 패스워드를 입력해주세요.");
    return;
  }

  let gameInfo = await fetch("/api/games", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      chessGameName: gameName,
      password: password,
      passwordCheck: passwordCheck
    }),
  }).then(handleErrors)
  .catch(function (error) {
    alert(error.message);
  });
  gameInfo = await gameInfo.json();
  return gameInfo.id;
}

async function handleErrors(response) {
  if (!response.ok) {
    let errorMessage = await response.json();
    throw Error(errorMessage.message);
  }
  return response;
}
