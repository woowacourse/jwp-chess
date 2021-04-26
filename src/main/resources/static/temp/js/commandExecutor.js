class CommandExecutor {
  #board;

  execute(response) {
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
        contents.innerHTML = gameRoom(response.data.whitePlayer, response.data.blackPlayer);
        this.#board = new Board(response.data.player, response.data.teamColor);
        this.#board.drawBoard(response.data.pieces)
          break;

      case 'NEW_USER_NAME' :
        let enemyName = document.querySelector('.enemy .info-name span');
        let myName = document.querySelector('.me .info-name span');
        enemyName.innerHTML = response.data.blackName;
        myName.innerHTML = response.data.whiteName;
        break;


    }
  }
}