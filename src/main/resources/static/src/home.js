window.onload = async function () {
    await makeRoomTable();
};

async function makeRoomTable() {
    $.ajax({
        url: "/rooms",
        type: 'get',
        success(data) {
            document.write(`<div class="board_table">`);
            document.write(`<button class="button" id="create_room_btn" onClick="createRoom()">체스방 만들기</button>`);
            document.write("<table><tr><th>제목</th><th>삭제</th></tr>");
            for (let i = 0; i < data.length; i++) {
                const id = data[i].id;
                const gameName = data[i].title;
                document.write(`<tr><td><button class="button" id="room" onclick="loadRoom(${id})">${gameName} 입장하기</button></td>`);
                document.write(`<td><button onclick='deleteRoom(${id})' class = 'button'>삭제</button></td></tr>`);
            }
            document.write("</table></div>");
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });
}

async function createRoom() {
    const title = prompt("방 이름 입력해주세요.");
    const password = prompt("방 비밀번호를 입력해주세요.");
    $.ajax({
        url: "/rooms",
        type: 'post',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type", "application/json");
        },
        data: JSON.stringify({
            title: title,
            password: password,
        }),
        success() {
            location.reload();
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });

}

async function loadRoom(id) {
    localStorage.setItem("id", id);
    window.location.href = "/games";
}

async function deleteRoom(id) {
    let password = prompt("비밀번호를 입력해주세요.");

    $.ajax({
        url: "/rooms/" + id,
        type: 'delete',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type", "application/json");
        },
        data: JSON.stringify({
            password: password,
        }),
        success() {
            location.reload();
        },
        error(request) {
            let obj = JSON.parse(request.responseText);
            alert(obj.message);
        }
    });
}

