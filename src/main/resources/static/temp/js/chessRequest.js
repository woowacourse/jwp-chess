class ChessRequest {
  createRoom(title, locked, password) {
    let requestForm = new RequestForm('CREATE_ROOM');
    requestForm.setData({
      'title' : title,
      'locked' : locked,
      'password' : password
    });
    webSocket.send(JSON.stringify(requestForm));
  }
}

class RequestForm {
  command;
  id;
  data;

  constructor(command) {
    this.command = command;
  }

  setId(id) {
    this.id = id;
  }

  setData(data) {
    this.data = data;
  }
}