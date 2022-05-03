function makeRoom() {

    let roomName = document.getElementById("title").value;
    let whiteName = document.getElementById("firstMemberName").value;
    let blackName = document.getElementById("secondMemberName").value;
    let password = document.getElementById("password").value;

    if (roomName === '' || whiteName === '' || blackName === '' || password === '') {
        alert("입력값을 모두 입력해주세요.");
        return;
    }

    let roomForm = document.getElementById("roomForm");
    roomForm.submit();
}

function deleteRoom(roomId) {

    const password = prompt('비밀번호를 입력하세요.', '');

    fetch(`/room/${roomId}/end`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `password=${password}`
    }).then((response) => {
        if (response.status === 400) {
            throw response;
            return;
        }
        if (response.status === 200) {
            alert("성공적으로 삭제하였습니다.");
            location.reload();
        }
    }).catch(err => {
        err.text().then(msg => {
            alert(msg);
        })
    });
}
