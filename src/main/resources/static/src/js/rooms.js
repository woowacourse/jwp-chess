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
        roomsUl.append("<li><a href=\"/chess-game/" + rooms[i]["id"] + "\">" + rooms[i]["title"] + "</a></li>");
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