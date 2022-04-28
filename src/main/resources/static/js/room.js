function getRooms() {
  $.ajax({
    url: "/room",
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "get",
    success: function (data) {
      $.each(data, (rowNumber, rowData) => {
        document.getElementById("room_list").innerHTML +=
            "<tr>"
            + "<td class='table_title' id='" + rowData.id + "'"
            + " onclick='enterRoom(this.id)'>"
            + rowData.id + "</td>"
            + "<td class='table_title'>" + rowData.title + "</td>"
            + "<td class='table_title'>" + rowData.status + "</td>"
            + "</tr>";
      })
    },
    error: function (data) {
      alert(JSON.stringify(data))
    }
  })
}

function enterRoom(roomId) {
  $.ajax({
    url: "/room/" + roomId,
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "get",
    success: function (data) {
      localStorage.setItem("roomId", roomId);
      location.href = "index.html";
    },
    error: function (data) {
      alert(JSON.stringify(data))
    }
  })
}

function deleteRoom() {
  alert(document.getElementById("delete_room_id").value)
  const object = {
    "roomId": document.getElementById("delete_room_id").value,
    "password": document.getElementById("delete_room_password").value,
  }

  $.ajax({
    url: "/room/delete",
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "post",
    data: JSON.stringify(object),
    success: function (data) {
      alert("제거되었습니다.")
      getRooms();
      window.reload();
    },
    error: function (data) {
      alert(JSON.stringify(data))
    }
  })
}

function createRoom() {
  const object = {
    "title": document.getElementById("create_room_title").value,
    "password": document.getElementById("create_room_password").value,
  }

  $.ajax({
    url: "/room",
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    type: "post",
    data: JSON.stringify(object),
    success: function (data) {
      alert("생성되었습니다.")
      getRooms();
      window.reload();
    },
    error: function (data) {
      alert(JSON.stringify(data))
    }
  })
}


/// Modal
// Get the modal
var modal = document.getElementById("modalDelete");

// Get the button that opens the modal
var btn = document.getElementById("myBtnDelete");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on the button, open the modal
btn.onclick = function() {
  modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target === modal) {
    modal.style.display = "none";
  }
}

/// Modal
// Get the modal
var modal2 = document.getElementById("modalCreate");

// Get the button that opens the modal
var btn2 = document.getElementById("myBtnCreate");

// Get the <span> element that closes the modal
var span2 = document.getElementsByClassName("close2")[0];

// When the user clicks on the button, open the modal
btn2.onclick = function() {
  modal2.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span2.onclick = function() {
  modal2.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target === modal2) {
    modal2.style.display = "none";
  }
}


