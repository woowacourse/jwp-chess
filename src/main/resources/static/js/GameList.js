import {Rooms} from "./room/rooms.js"
import {getData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = async function () {
  const response = await getData(`${url}/api/rooms`);
  const rooms = new Rooms(response["roomResponseDtos"]);
}
