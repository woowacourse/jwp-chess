export const HOST = "host";
export const GUEST = "guest";

export class Role {
  #value

  constructor(value) {
    this.#value = value;
  }

  isHost() {
    return this.#value === HOST;
  }

  isGuest() {
    return this.#value === GUEST;
  }
}
