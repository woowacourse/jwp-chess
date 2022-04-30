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
            location.reload();
        });
}

function deleteRoom(roomId) {
    let password = prompt("비밀번호를 입력하세요");

    fetch('/chess/' + roomId, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: password,
    }).then(res => res.json())
        .then(value => {
            if (value["statusCode"] === 400) {
                alert(value["errorMessage"]);
                return;
            }
            if (value) {
                alert(roomId + " 방이 삭제되었습니다.");
                location.reload();
                return;
            }
            alert("게임이 진행중입니다.");
            location.reload();
        });
}
