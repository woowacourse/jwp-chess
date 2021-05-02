class CommandExecutor {
  #board;

  async execute(response) {
    switch (response.form) {
      case 'UPDATE_ROOM' :
        document.querySelector('.contents');
        const roomList = document.querySelector('.room-list');
        roomList.innerHTML = '';
        roomList.innerHTML = response.data.reduce((prev, curr) => {
          const {roomId, title, locked, playerAmount} = curr;
          return prev + room(roomId, title, locked, playerAmount);
        }, '').toString() + createRoom;
        break;

      case 'LOAD_GAME' :
        const contents = document.querySelector('.contents');
        contents.innerHTML = gameRoom(response.data.whitePlayer,
            response.data.blackPlayer);
        this.#board = new Board(response.data.player, response.data.teamColor);
        this.#board.drawBoard(response.data.pieces)
        break;

      case 'NEW_USER_NAME' :
        let enemyName = document.querySelector('.enemy .info-name span');
        let myName = document.querySelector('.me .info-name span');
        enemyName.innerHTML = response.data.blackName;
        myName.innerHTML = response.data.whiteName;
        break;

      case 'UPDATE_STATUS' :
        this.#board.updateBoardStatus(response.data);
        break;

      case 'MOVE_PIECE' :
        const currentPosition = document.getElementById(
            response.data.currentPosition);
        const targetPosition = document.getElementById(
            response.data.targetPosition);
        this.#board.selectItem(currentPosition);
        this.#board.move(targetPosition);
        break;

      case 'REMOVE_ROOM' :
        await alert(`${response.data}님이 나가 게임이 종료됩니다.`);
        window.location.reload();
        break;

      case 'MESSAGE' :
        let chatContents = document.querySelector('.chat-contents');
        let content = chat(response.data.sender, response.data.contents);
        chatContents.insertAdjacentHTML("beforeend", content);
        chatContents.lastElementChild.scrollIntoView();
        break;
    }
  }

  board() {
    return this.#board;
  }
}