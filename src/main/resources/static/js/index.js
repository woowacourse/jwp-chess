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
        indexPage.addNewRoom(newRoomId);
      });
}

IndexPage.prototype.getRooms = function () {
  const roomList = document.querySelector(".room-list");
  fetch(indexPage.roomsApiUrl, {
    method: 'GET'
  }).then(res => res.json())
  .then(function (data) {
    for (let i = 0; i < data.roomIds.length; i++) {
      roomList.innerHTML +=
          `<li class="room">
                <button class="room-button" onclick=indexPage.addNewRoom(${data.roomIds[i]})>
                   ${data.roomIds[i]}
                </button>
            </li>`;
    }
  });
}

IndexPage.prototype.addNewRoom = function (newRoomId) {
  fetch(indexPage.roomsApiUrl + "/" + newRoomId, {
    method: 'POST',
    body: JSON.stringify({
      roomId: newRoomId
    }),
    headers: {
      'Content-Type': 'application/json',
    }
  }).then(res => res.json())
  .then(data => {
    localStorage.setItem("roomId", newRoomId);
    localStorage.setItem("pieces", JSON.stringify(data));
    location.href = 'chess'
  });
}