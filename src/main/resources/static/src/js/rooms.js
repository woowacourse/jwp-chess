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
            console.log(rooms);
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });
}
