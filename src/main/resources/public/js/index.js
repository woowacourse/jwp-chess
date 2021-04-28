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
    .then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.json();
    })
    .then(responseJson => {
      const rooms = responseJson.rooms;
      for (let i = 0; i < rooms.length; i++) {
        this.roomList.insertAdjacentHTML("beforeend", this.renderRoom(rooms[i]));
      }
    })
    .catch(err => err.json().then(json => alert(json.detailMessage)));
  }

  this.addNewRoom = () => {
    const newRoomName = this.newRoomInput.value;
    fetch(`http://localhost:8080/rooms/${newRoomName}`, {
      method: 'POST',
      headers: {'content-type': 'application/json'}
    })
    .then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.json();
    })
    .then(responseJson => {
      this.roomList.insertAdjacentHTML("beforeend", this.renderRoom(responseJson));
    })
    .catch(err => {
      err.json().then(json => alert(json.detailMessage));
    });

    this.newRoomInput.value = "";
  }

  this.newRoomBtn.addEventListener('click', this.addNewRoom);
}

window.onload = () => {
  const index = new Index();
  index.constructor();
}
