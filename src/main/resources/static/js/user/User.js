export class User {
  #id
  #name
  #createdTime

  constructor(plainUser) {
    this.#id = plainUser["id"];
    this.#name = plainUser["name"];
    this.#createdTime = plainUser["createdTime"];
  }

  get id() {
    return this.#id;
  }

  get name() {
    return this.#name;
  }

  get createdTime() {
    return this.#createdTime;
  }
}
