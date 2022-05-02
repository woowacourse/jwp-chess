function createBoard() {
    let title = document.getElementById('board-title').value;
    let password = document.getElementById('board-password').value;

    const request = {
        title: title,
        password: password
    };

    fetch('/api/boards', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
        .then(value => {
            if (value["statusCode"] !== 200) {
                alert(value["errorMessage"]);
                return;
            }
            alert("생성된 방 번호는 " + value + "입니다.")
            location.reload();
        });
}

function deleteBoard(boardId) {
    let password = prompt("비밀번호를 입력하세요");

    fetch('/api/boards/' + boardId, {
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
                alert(boardId + " 방이 삭제되었습니다.");
                location.reload();
                return;
            }
            alert(boardId + " 방 삭제에 실패했습니다.");
            location.reload();
        });
}
