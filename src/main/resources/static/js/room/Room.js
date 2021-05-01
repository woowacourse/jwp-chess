import {User} from "../user/User.js";

const HIGHRIGHTED_BOARD_COLOR = "rgba(144,197,213,0.87)";

export class Room {
  #id
  #name
  #host
  #guest
  #createdTime
  #component

  constructor(roomDto) {
    this.#id = roomDto["id"];
    this.#name = roomDto["name"];
    this.#host = new User(roomDto["host"]);
    this.#guest = new User(roomDto["guest"]);
    this.#createdTime = roomDto["createdTime"];
    this.#insertComponent();
    this.#component = document.querySelector(`.room#room${this.#id}`);
    this.#addEvent();
  }

  #insertComponent() {
    const rooms = document.querySelector(".rooms");
    rooms.insertAdjacentHTML("beforeend", this.#makeRoomTemplate());
  }

  #makeRoomTemplate() {
    return `<li class="room" id="room${this.#id}">
        <div class="name">${this.#name}</div>
        <div class="user-container">
          <div class="white">
            <span class="team-tag white">백팀</span>
            <span class="user-name white">${this.#host.name}</span>
          </div>
          <div class="black">
            <span class="team-tag black">흑팀</span>
            <span class="user-name black">${this.#guest.name}</span>
          </div>
        </div>
      </li>`
  }

  #addEvent() {
    this.#component.addEventListener("mouseenter",
        e => this.#enterMouse(e, this));
    this.#component.addEventListener("mouseleave",
        e => this.#leaveMouse(e, this));
    this.#component.addEventListener("click", e => this.#click(e, this));
  }

  #enterMouse(e, room) {
    room.#highlight();
  }

  #leaveMouse(e, room) {
    room.#unhighlight();
  }

  #click(e, room) {
    // TODO : guest로 참여
  }

  #highlight() {
    this.#component.style.border = `0.3vh solid rgba(255,255,255,1)`;
    this.#component.style.boxShadow = `0 0 2vh 0.8vh ${HIGHRIGHTED_BOARD_COLOR}`;
  }

  #unhighlight() {
    this.#component.style.border = "0.3vh solid #ffffff";
    this.#component.style.boxShadow = `0 0 0.0 0 ${HIGHRIGHTED_BOARD_COLOR}`;
  }
}
