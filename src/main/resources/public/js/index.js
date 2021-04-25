const Index = function() {
  this.newRoomInput = document.querySelector(".new-room");
  this.newRoomBtn = document.querySelector("#create");
  this.roomList = document.querySelector("#room_list");

  this.renderRoom = (room) => {
    return `<li><a href="/room/${room.name}">${room.name}</a></li>`
  }

  this.constructor = () => {
    fetch("http://localhost:8080/rooms/", {
      method: 'GET',
      headers: {'content-type': 'application/json'}
    })
    .then(response => response.json())
    .then(responseJson => {
      if (responseJson.status != "OK") {
        alert(responseJson.detailMessage);
        return;
      }
      const rooms = responseJson.payload;
      for (let i = 0; i < rooms.length; i++) {
        this.roomList.insertAdjacentHTML("beforeend", this.renderRoom(rooms[i]));
      }
    })
    .catch(err => alert(err));
  }

  this.addNewRoom = () => {
    const newRoomName = this.newRoomInput.value;
    fetch(`http://localhost:8080/rooms/${newRoomName}`, {
      method: 'POST',
      headers: {'content-type': 'application/json'}
    })
    .then(response => response.json())
    .then(responseJson => {
      if (responseJson.status != "OK") {
        alert(responseJson.detailMessage);
        return;
      }
      this.roomList.insertAdjacentHTML("beforeend", this.renderRoom(responseJson.payload));
    })
    .catch(err => alert(err));

    this.newRoomInput.value = "";
  }

  this.newRoomBtn.addEventListener('click', this.addNewRoom);

  this.constructor();
}

window.onload = () => {
  const index = new Index();
}
