window.onload = function() {
    $("#open_room").click(function() {
        $(".room_info").css("display", "inline");
    });

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
        roomsUl.append("<li>"
        + CHESS_GAME_TURN[rooms[i]['turn']] + " "
        + CHESS_GAME_STATUS[rooms[i]['status']]
        + "</li>");
    }
}
