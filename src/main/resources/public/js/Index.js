const MainApp = function() {
  this.$createBtn = document.querySelector("#create");
  this.$roomTable = document.querySelector("#room_table");

  this.$createBtn.addEventListener('click', () => {
    const roomName = prompt("입장하실 방의 이름을 입력해주세요.\n 존재하지 않은 이름을 입력시 새로운 방이 만들어집니다.");
    if (roomName === "") {
      alert("다시 입력해주세요 :)");
      return this.constructor();
    }
    fetch(`http://localhost:8080/api/room`, {
      method: 'POST',
      headers: {'content-type': 'application/json'},
      body: JSON.stringify({name: roomName})
    })
    .then(response => response.json())
    .then(responseJson => {
      if (responseJson.status === 400) {
        throw new Error(responseJson.message);
      }
      this.$roomTable.insertAdjacentHTML("beforeend",
          `<li><a href="/room/${responseJson.name}">${responseJson.name}</a></li>`);
    })
    .catch(err => {
      alert(err);
    });
  });

  this.constructor = function() {
    fetch(`http://localhost:8080/api/room/all`)
    .then(response => response.json())
    .then(responseJson => {
      const rooms = responseJson.rooms;
      for (let i = 0; i < rooms.length; ++i) {
        this.$roomTable.insertAdjacentHTML("beforeend",
            `<li><a href="/room/${rooms[i].name}">${rooms[i].name}</a></li>`)
      }
    })
    .catch(err => alert(err));
  }.bind(this);
}

window.onload = () => {
  const mainApp = new MainApp();
  mainApp.constructor();
}