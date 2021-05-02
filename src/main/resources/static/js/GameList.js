import {Rooms} from "./room/rooms.js"
import {getData} from "./utils/FetchUtil.js"
import {getCookie, USER_ID_KEY} from "./utils/CookieUtil.js";

const url = "http://localhost:8080";

window.onload = async function () {
  const response = await getData(`${url}/api/games`);
  const rooms = new Rooms(response["roomResponseDtos"]);

  const guestId = getCookie(USER_ID_KEY);
}
