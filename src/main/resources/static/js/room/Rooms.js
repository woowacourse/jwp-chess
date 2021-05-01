import {Room} from "./Room.js";

export class Rooms {
  #rooms = []

  constructor(roomDtos) {
    for (let i = 0; i < roomDtos.length; i++) {
      this.#rooms.push(new Room(roomDtos[i]));
    }
  }

  get rooms() {
    return this.#rooms;
  }
}
