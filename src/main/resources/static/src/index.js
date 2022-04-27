function createRoom() {
    let title = document.getElementById('room-title').value;
    let password = document.getElementById('room-password').value;

    const request = {
        title: title,
        password: password
    };

    fetch('/chess', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
        .then(value => {
            alert("생성된 방 번호는 " + value + "입니다.")
            let link = "/chess/" + value;
            window.open(link);
        });
}

function deleteRoom(boardId, password) {
    const request = {
        password: password
    };

    fetch('/chess/' + boardId, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
        .then(value => {
            if (value) {
                alert(boardId + " 방이 삭제되었습니다.");
                location.reload();
                return;
            }
            alert("게임이 진행중입니다.");
            location.reload();
        });
}
