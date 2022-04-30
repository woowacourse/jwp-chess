function getRooms() {
  $.ajax({
    url: "/rooms",
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "GET",
    success: function (data) {
      $.each(data, (rowNumber, rowData) => {
        var status = "진행 중";
        if (rowData.status === false) {
          status = "종료됨";
        }
        document.getElementById("room_list").innerHTML +=
            "<tr>"
            + "<td class='table_id'>" + rowData.id + "</td>"
            + "<td class='table_title'>" + rowData.title + "</td>"
            + "<td class='table_status'>" + status + "</td>"
            + "<td class='table_title'>"
            + "<button class='table_title' id='" + rowData.id + "'"
            + " onclick='enterRoom(this.id)'>" +""
            + "방 입장하기</button></td>"
            + "</tr>";
      })
    },
    error: function (data) {
      alert(JSON.stringify(data))
    }
  })
}

function enterRoom(roomId) {
  location.href = `/rooms/${roomId}`;
}

function deleteRoom() {
  const object = {
    "roomId": document.getElementById("delete_room_id").value,
    "password": document.getElementById("delete_room_password").value,
  }

  $.ajax({
    url: "/rooms",
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "DELETE",
    data: JSON.stringify(object),
    success: function () {
      alert("제거되었습니다.")
      getRooms();
      window.reload();
    },
    error: function (data) {
      alert(JSON.stringify(data.responseText))
    }
  })
}

function createRoom() {
  const object = {
    "title": document.getElementById("create_room_title").value,
    "password": document.getElementById("create_room_password").value,
  }

  $.ajax({
    url: "/rooms",
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "POST",
    data: JSON.stringify(object),
    success: function (data) {
      alert("생성되었습니다.")
      window.reload();
    },
    error: function (data) {
      alert(JSON.stringify(data.responseText))
    }
  })
}


/// Modal
var deleteModal = document.getElementById("modalDelete");
var delteBtn = document.getElementById("myBtnDelete");
var deleteSpan = document.getElementsByClassName("delete-modal-close")[0];

delteBtn.onclick = function() {
  deleteModal.style.display = "block";
}

deleteSpan.onclick = function() {
  deleteModal.style.display = "none";
}

window.onclick = function(event) {
  if (event.target === deleteModal) {
    deleteModal.style.display = "none";
  }
}

var createModal = document.getElementById("modalCreate");
var createBtn = document.getElementById("myBtnCreate");
var createSpan = document.getElementsByClassName("create-modal-close")[0];

createBtn.onclick = function() {
  createModal.style.display = "block";
}

createSpan.onclick = function() {
  createModal.style.display = "none";
}

window.onclick = function(event) {
  if (event.target === createModal) {
    createModal.style.display = "none";
  }
}


