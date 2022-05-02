$(document).ready(function () {
  triggerEvents();
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
