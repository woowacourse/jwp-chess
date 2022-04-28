let source = '';
let target = '';

function join(id){
    location.replace(`/board?id=${id}`);
}

function create(){
    let title = document.getElementById('titleId').value;
    let password = document.getElementById('passwordId').value;

    fetch("/create", {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify({
            title: title,
            password: password
        })
    }).then(res => {
        res.json().then(data => {
            if(data.statusCode == 200){
                location.replace("/");
            } else{
                alert(data.errorMessage);
            }
        })
    })
}

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
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    fetch(`/board/move?id=${boardId}`, {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: source + " " + target
    }).then((res) => {
        res.json().then(data => {
            document.getElementById(source).style.backgroundColor = '';
            document.getElementById(target).style.backgroundColor = '';
            source = '';
            target = '';
            if (data.statusCode === 301) {
                alert("킹이 잡혀 게임이 종료 되었습니다!")
                end();
            }
            if (data.statusCode === 302) {
                location.reload();
            }
            if (data.statusCode === 501) {
                alert(data.errorMessage);
            }
        })
    })
}

function start() {
    fetch("/start").then(res => {
        res.json().then(data => {
            if (data.statusCode === 501) {
                alert(data.errorMessage);
                return;
            }
            location.replace("/chess");
        })
    })
}

function end() {
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    fetch(`/board/end?id=${boardId}`, {
        method: "POST"
    }).then(res => {
        res.json().then(data => {
            if (data.statusCode === 501) {
                alert(data.errorMessage);
                return;
            }
            location.replace(`/board/chess-result?id=${boardId}`);
        })
    })
}

function restart() {
    const params = new URLSearchParams(location.search);
    const boardId = params.get('id');
    fetch(`/board/restart?id=${boardId}`, {
        method: "POST"
    }).then(res => {
        res.json().then(data => {
            if (data.statusCode === 301){
                location.replace(`/board?id=${boardId}`);
            } else{
                alert(data.errorMessage);
            }
        })
    })
}

