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
        if (res.status === 301) {
            alert("킹이 잡혀 게임이 종료 되었습니다!")
            end();
        }
        if (res.status === 302) {
            location.replace("/chess" + window.location.search);
        }
        if (res.status === 501) {
            res.text().then(message => alert(message))
        }
    })
}

function chess(id) {
    fetch("/chess?id=" + id).then(res => {
        if (res.status === 501) {
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

function end() {
    fetch("/end" + window.location.search).then(res => {
        if (res.status === 501) {
            res.text().then(message => alert(message))
            return;
        }
        location.replace("/chess-result" + window.location.search);
    })
}

function movePage(targetPage) {
    location.replace(targetPage + window.location.search);
}