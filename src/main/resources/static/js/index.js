$(document).ready(function () {
  triggerEvents();
  loadRooms();
});

function triggerEvents() {
  $("button#room-delete").click(function () {
    deleteClickEvent();
  });

  $("button#first-game").click(function () {
    debugger;
    location.replace("/games/first");
  });
}

function deleteClickEvent() {
  let isDel = confirm("delete this event ? ");
  if (isDel) {
    alert("deleted ... ");
    return;
  }
  console.log("cancel");
}

function loadRooms() {
  $.ajax({
    url: "/rooms",
    method: "GET",
  })
    .done(function (data) {
      for (let element of data.values) {
        console.log(element);
        $("#room-list").append(`<tr id=${element.gameId}>`);
        $("#room-list tr")
          .last()
          .append(`<td id="room-name">${element.roomName}</td>`)
          .append(`<td id="room-create-at">${element.createdAt}</td>`)
          .append(
            `<td><input type="password" id="room-password" name="room-password"></td>`
          )
          .append(`<td><button id="room-delete">DELETE</button></td>`);
      }
    })
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });
}
