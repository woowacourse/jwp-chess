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
    fetch("/move", {
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
            location.replace("/chess");
        }
        if (res.status === 501) {
            res.text().then(message => alert(message))
        }
    })
}


function end() {
    fetch("/end").then(res => {
        if (res.status === 501) {
            res.text().then(message => alert(message))
            return;
        }
        location.replace("/chess-result");
    })
}

