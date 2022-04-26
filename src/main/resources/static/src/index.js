let first = '';
let second = '';

function clicked(id) {
    setVariable(id);
    changeClickedBackground(id);
    checkSendToServer();
}

function setVariable(id) {
    if (first !== '') {
        second = id;
    } else {
        first = id;
    }
}

function changeClickedBackground(id) {
    let elementById = document.getElementById(id);
    elementById.style.backgroundColor = "#ff0";
}

function checkSendToServer() {
    if (first !== '' && second !== '') {
        sendToServer(first, second);
        first = '';
        second = '';
    }
}

function checkStatus() {
    let element = document.getElementById("roomId");

    fetch('/room/' + element.value + 'status', {
        method: "GET",
        headers: {
            "Content-Type": "text/plain",
        }
    }).then((response) => response.json())
        .then(data => {
            console.log(data);
            console.log(JSON.stringify(data));
            document.getElementById("statusResult")
                .innerHTML = "검은말 : " + data.score.BLACK + "<br >"
                + "흰말 : " + data.score.WHITE + "<br >"
                + "우승자 : " + data.winner;
        });
}

function sendToServer(first, second) {
    let element = document.getElementById("roomId");
    const moveCommand = {"source": first, "target": second};
    let moveUrl = '/room/' + element.value + 'move';
    fetch(moveUrl, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(moveCommand)
    }).then((response) => {
        if (response.status === 400) {
            response.text().then(data => {
                alert(data);
            })
            location.reload();
            return;
        }
        response.text().then(data => {
            if (data === 'true') {
                alert("게임이 종료되었습니다.");
                document.location.href = '/'
            }
            location.reload();
        });
    })
}
