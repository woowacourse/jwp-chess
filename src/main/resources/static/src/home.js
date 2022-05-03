function makeRoom() {

    let roomName = document.getElementById("roomName").value;
    let whiteName = document.getElementById("whiteName").value;
    let blackName = document.getElementById("blackName").value;
    let password = document.getElementById("password").value;

    if (roomName === '' || whiteName === '' || blackName === '' || password === '') {
        alert("입력값을 모두 입력해주세요.");
        return;
    }

    let roomForm = document.getElementById("roomForm");
    roomForm.submit();
}

function deleteRoom(roomId, gameStatus) {

    if (gameStatus === 'running') {
        alert("진행중인 게임은 삭제할 수 없습니다.");
        return;
    }
    var password = prompt("비밀번호를 입력하세요");
    let deleteUrl = '/room/' + roomId;
    fetch(deleteUrl, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json',
        },
        body: password
    }).then(response => {
            response.text().then(data => {
                if (data === '') {
                    data = "삭제되었습니다.";
                }
                alert(data);
                location.reload();
    })})
}
