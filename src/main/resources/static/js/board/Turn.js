const SRC_PATH = "/img/";
const HIGHLIGHTED_BLACK_BEAR_SRC = `${SRC_PATH}/black-bear-piece-yellow.png`;
const BLACK_BEAR_SRC = `${SRC_PATH}/black-bear-piece.png`;
const HIGHLIGHTED_WHITE_BEAR_SRC = `${SRC_PATH}/white-bear-piece-cyan.png`;
const WHITE_BEAR_SRC = `${SRC_PATH}/white-bear-piece.png`;

export class Turn {

  #turn
  #whiteBearComponent
  #blackBearComponent

  constructor(turn) {
    this.#turn = turn;
    this.#whiteBearComponent = document.querySelector(".bear-piece.white");
    this.#blackBearComponent = document.querySelector(".bear-piece.black");
    this.#turn = turn;
    this.#setBearImage();
  }

  get turn() {
    return this.#turn;
  }

  changeTurn() {
    this.#reverseTurn();
    this.#setBearImage();
  }

  #isWhite() {
    return this.#turn == "WHITE";
  }

  #reverseTurn() {
    if (this.#isWhite()) {
      this.#turn = "BLACK";
      return;
    }
    this.#turn = "WHITE";
  }

  #setBearImage() {
    if (this.#isWhite()) {
      this.#whiteBearComponent.src = HIGHLIGHTED_WHITE_BEAR_SRC
      this.#blackBearComponent.src = BLACK_BEAR_SRC;
    } else {
      this.#whiteBearComponent.src = WHITE_BEAR_SRC
      this.#blackBearComponent.src = HIGHLIGHTED_BLACK_BEAR_SRC;
    }
  }
}
