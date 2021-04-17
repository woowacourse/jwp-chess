import {getData, postData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = function () {
  const newGameButton = document.querySelector(".new-game");
  const loadGameButton = document.querySelector(".load-game")
  const searchRecordButton = document.querySelector(".search-record")

  newGameButton.addEventListener("click", startNewGame);
  loadGameButton.addEventListener("click", loadGame);
}

async function startNewGame(e) {
  const whiteUserName = prompt("흰색 유저 이름을 입력하세요.");
  const blackUserName = prompt("검정색 유저 이름을 입력하세요.");

  try {
    validateName(whiteUserName, blackUserName);
  } catch (e) {
    alert("이름이 비어있거나, 같은 이름을 입력했습니다.");
    return;
  }

  try {
    const host = await getExistentUser(whiteUserName);
    const guest = await getExistentUser(blackUserName);
    if (isEmptyObject(host) || isEmptyObject(guest)) {
      alert("존재하지 않는 유저가 있습니다.");
      return;
    }
    await createGame(host["id"], guest["id"]);
  } catch (e) {
    alert(e);
  }
}

async function getExistentUser(userName) {
  const params = {
    name: userName
  }
  return await getData(`${url}/users`, params);
}

function validateName(whiteName, blackName) {
  if (whiteName.length === 0 || blackName.length === 0) {
    throw Error("이름을 입력하지 않았습니다.");
  }
  if (whiteName === blackName) {
    throw Error("유저의 이름은 같을 수 없습니다.");
  }
}

async function createUser(userName) {
  const body = {
    name: userName,
    password: "123"
  }
  return await postData(`${url}/users`, body);
}

function isEmptyObject(object) {
  return Object.keys(object).length === 0;
}

async function createGame(hostId, guestId) {
  const body = {
    name: "임시 방이름",
    hostId: hostId,
    guestId: guestId
  };
  await postData(`${url}/chess/creation`, body);
}

function loadGame() {
  const gameId = prompt("이동할 방번호를 입력하세요.");
  window.location.href = `${url}/game/${gameId}`
}
