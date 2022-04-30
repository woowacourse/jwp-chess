window.onload = function() {
    $("#open_room").click(function() {
        $(".room_info").css("display", "inline");
    });

    $("#start").click(function() {
        start();
    })

    loadRooms();
}

function loadRooms() {
    $.ajax({
        url: "/chess-game/load",
        type: "get",
        success(data) {
            let rooms = parseToJSON(data);
            showRooms(rooms);
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });
}

function showRooms(rooms) {
    let roomsUl = $("#rooms");

    for (let i = 0; i < rooms.length; i++) {
        roomsUl.append(
        "<li>"
        + "<a href=\"/chess-game/" + rooms[i]["id"] + "\">"
        + rooms[i]["title"]
        + "</a>"
        + "<input type=\"button\" value=\"삭제\" onclick=checkPassword(" + rooms[i]["id"] + ") />"
        + "</li>");
    }
}

function start() {
    $.ajax({
        url: "/chess-game/start",
        type: "post",
        beforeSend: function (xhr) {
             xhr.setRequestHeader("Content-type","application/json");
        },
        data: JSON.stringify({
            title: $("#title").val(),
            password: $("#password").val()
        }),
        success(data) {
            let chessId = parseToJSON(data);
            location.href = "/chess-game/" + chessId;
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });
}

function checkPassword(chessId) {
    $("#check_password_wrap").css("display", "block");
    $("#deletedRoomId").val(chessId);
}

function cancel() {
    $("#deletedRoomId").val();
    $("#check_password_wrap").css("display", "none");
}

function deleteGame() {
    let chessId = $("#deletedRoomId").val();

    $.ajax({
        url: "/chess-game/" + chessId,
        type: "delete",
        beforeSend: function (xhr) {
             xhr.setRequestHeader("Content-type","application/json");
        },
        data: JSON.stringify({
            id: chessId,
            password: $("#check_password").val()
        }),
        success(data) {
            location.href = "/chess-game";
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });
}