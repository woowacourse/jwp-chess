let indexPage;

document.addEventListener("DOMContentLoaded", function () {
  indexPage = new IndexPage();
  indexPage.initIndexPage();
});

function IndexPage() {
  this.roomsApiUrl = "http://localhost:8080/api/chess/rooms";
}

IndexPage.prototype.initIndexPage = function () {
  this.getRooms();
  this.registerMakeRoomButtonEvent();
}

IndexPage.prototype.registerMakeRoomButtonEvent = function () {
  document.querySelector(".make-room-button").addEventListener("click",
      function () {
        let newRoomId = document.querySelector(".newRoomId").value;
        indexPage.enterNewRoom(newRoomId);
      });
}

IndexPage.prototype.getRooms = function () {
  const roomList = document.querySelector(".room-list");
  fetch(indexPage.roomsApiUrl, {
    method: 'GET'
  }).then(function (response) {
    if (response.ok) {
      return response.json();
    }
    response.text().then(function (data) {
      alert(data);
    })
    throw Error})
  .then(function (data) {
    for (let i = 0; i < data.roomIds.length; i++) {
      roomList.innerHTML +=
          `<li class="room">
                <button class="room-button" onclick=indexPage.enterNewRoom(${data.roomIds[i]})>
                   ${data.roomIds[i]}
                </button>
            </li>`;
    }
  });
}

IndexPage.prototype.enterNewRoom = function (newRoomId) {
  location.href = 'chess/' + newRoomId;
}