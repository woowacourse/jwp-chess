document.addEventListener("DOMContentLoaded", function () {

    $('.btn-makeRoom').click(async function () {
        $.ajax({
            type: "POST",
            url: "/room/create",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                roomName: $('#makeRoomName').val(),
                userPassword: $('#creatorPassword').val()
            }),
            success: function (data) {
                if(data.responseCode ==200) {
                    roomId = data.responseData
                    window.location.href = "board.html?id=" + roomId + "?password=" + $('#creatorPassword').val()
                }
                else {
                    alert(data.message)
                }
            },
            error: function (e) {
                console.log(e.message);
            }
        });
    });

    $('.btn-joinRoom').click(async function () {
        $.ajax({
            type: "POST",
            url: "/room/join",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                roomName: $('#joinRoomName').val(),
                userPassword: $('#joinerPassword').val()
            }),
            success: function (data) {
                roomId = data.responseData
                window.location.href = "board.html?id=" + roomId + "?password=" + $('#joinerPassword').val()
            },
            error: function (e) {
                console.log(e.message);
            }
        });
    });

    $('.btn-getRoomList').click(async function () {
        $.ajax({
            type: "GET",
            url: "/room/list",
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                console.log(data)
                rooms = "";
                jQuery.each(data.responseData, function (key, value) {
                    rooms += value + "\r\n"
                })
                $(`#${"roomList"}`).html(rooms)
            },
            error: function (e) {
                console.log(e.message);
            }
        });
    });
});
