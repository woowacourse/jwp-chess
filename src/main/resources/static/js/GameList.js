import {Rooms} from "./room/rooms.js"
import {getData} from "./utils/FetchUtil.js"
import {CHESS_URL} from "./URL.js";

const url = CHESS_URL;

window.onload = async function () {
  const response = await getData(`${url}/api/rooms`);
  const rooms = new Rooms(response["roomResponseDtos"]);
}
