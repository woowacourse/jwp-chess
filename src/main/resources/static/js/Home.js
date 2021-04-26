import {getData, postData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = function () {
  const newGameButton = document.querySelector(".new-game");
  const loadGameButton = document.querySelector(".load-game")
  const registerMemberButton = document.querySelector(".register-member")
  const searchRecordButton = document.querySelector(".search-record")

  newGameButton.addEventListener("click", startNewGame);
  loadGameButton.addEventListener("click", loadGame);
  registerMemberButton.addEventListener("click", registerMember);
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

  const host = await getExistentUser(whiteUserName);
  const guest = await getExistentUser(blackUserName);
  if (!host || !guest) {
    alert("존재하지 않는 유저가 있습니다.");
    return;
  }

  await createGame(host["id"], guest["id"]);
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
  if (gameId) {
    window.location.href = `${url}/games/${gameId}`
  }
}

async function registerMember() {
  const name = prompt("원하는 이름을 입력하세요.");
  if (!name) {
    alert("이름을 입력하지 않았습니다.");
    return;
  }
  const response = await getData(`${url}/users`, {name: name});
  if (response) {
    alert("이미 존재하는 이름입니다.");
    return;
  }
  const password = prompt("원하는 비밀번호를 입력하세요.");
  if (!password) {
    alert("비밀번호를 입력하지 않았습니다.");
    return;
  }
  const body = {
    name: name,
    password: password
  };
  await postData(`${url}/users`, body);
  alert("회원가입 완료!")
}
