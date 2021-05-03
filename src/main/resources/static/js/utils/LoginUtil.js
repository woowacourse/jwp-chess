import {postData} from "./FetchUtil.js";
import {CHESS_URL} from "../URL.js";

const url = CHESS_URL;

export async function login() {
  const userName = prompt("유저 이름을 입력하세요.");
  if (userName.length === 0) {
    alert("이름을 입력하지 않았습니다.");
    return;
  }
  const password = prompt("비밀번호를 입력하세요.");
  if (password.length === 0) {
    alert("비밀번호를 입력하지 않았습니다.");
    return;
  }
  const response = await requestAuthentication(userName, password);
  if (!response) {
    alert("로그인에 실패했습니다.");
    return;
  }
  return response["id"];
}

export async function requestAuthentication(userName, password) {
  const params = {
    name: userName,
    password: password
  }
  return await postData(`${url}/api/users/authentication`, params);
}
