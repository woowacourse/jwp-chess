class ChessRequest {
  createRoom(title, locked, password, nickname) {
    let requestForm = new RequestForm('CREATE_ROOM');
    requestForm.setData({
      title,
      locked,
      password,
      nickname
    });
    webSocket.send(JSON.stringify(requestForm));
  }

  enterRoomAsPlayer(roomId, nickname, password) {
    let requestForm = new RequestForm('ENTER_ROOM_PLAYER');
    requestForm.setData({
      roomId,
      nickname,
      password
    });
    webSocket.send(JSON.stringify(requestForm));
  }
}

class RequestForm {
  command;
  data;

  constructor(command) {
    this.command = command;
  }

  setData(data) {
    this.data = data;
  }
}