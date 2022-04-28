let source = '';
let target = '';

function move(id) {
    if (source === '') {
        let elementById = document.getElementById(id);
        elementById.style.backgroundColor = "#FF0000";
        source = id;
        return;
    }

    if (target === '') {
        let elementById = document.getElementById(id);
        elementById.style.backgroundColor = "#FF0000";
        target = id;
        movePiece();
    }
}

function movePiece() {
    fetch("/move" + window.location.search, {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: source + " " + target
    }).then((res) => {
        document.getElementById(source).style.backgroundColor = '';
        document.getElementById(target).style.backgroundColor = '';
        source = '';
        target = '';
        if (res.status === 202) {
            alert("킹이 잡혀 게임이 종료 되었습니다!")
            end();
        }
        if (res.status === 200) {
            location.replace("/chess" + window.location.search);
        }
        if (res.status === 400) {
            res.text().then(message => alert(message))
        }
    })
}

function chess(id) {
    fetch("/chess?id=" + id).then(res => {
        if (res.status === 400) {
            res.text().then(message => {
                if (message !== "이미 완료된 게임입니다") {
                    alert(message);
                    return;
                }
                const watchResult = confirm("완료된 게임입니다. 결과 확인창으로 이동할까요?");
                if (watchResult) {
                    location.replace("/chess-result?id=" + id);
                }
            })
            return;
        }
        location.replace("/chess?id=" + id);
    })
}

function deleteBoard(id) {
    const input = prompt("비밀번호를 입력해주세요.");
    fetch("/delete" + "?id=" + id + "&password=" + input, {
        method: "DELETE",
    }).then((res) => {
        if (res.status !== 200) {
            res.text().then(message => alert(message))
            return;
        }
        alert("삭제되었습니다");
        location.reload();
    })
}

function end() {
    fetch("/end" + window.location.search).then(res => {
        if (res.status !== 200) {
            res.text().then(message => alert(message))
            return;
        }
        location.replace("/chess-result" + window.location.search);
    })
}

function movePage(targetPage) {
    location.replace(targetPage + window.location.search);
}