function onClick(value) {
    if (document.getElementsByClassName('sourcePosition').length) {
        return selectDestinationPiece(value);
    }
    sourcePosition(value);
}

function sourcePosition(value) {
    document.getElementById(value).classList.add('sourcePosition');
}

function resetSourcePosition() {
    document.getElementsByClassName('sourcePosition')[0].classList.remove('sourcePosition');
}

function movePiece(value) {
    function setPiece() {
        document.getElementById(value).innerHTML =
            document.getElementsByClassName('sourcePosition')[0].innerHTML;
        document.getElementsByClassName('sourcePosition')[0].innerHTML = "";
    }

    setPiece();
    resetSourcePosition();
}

const selectDestinationPiece = (value) => {
    let url = "/move/" + fetchId();
    let moveRequest = {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body:
            `source=${document.getElementsByClassName("sourcePosition")[0].id}
            &target=${document.getElementById(value).id}`
    };
    fetch(url, moveRequest)
        .then(response => {
            if (response.data) {
                alert("게임이 종료되었습니다.");
            }
            movePiece(value);
        })
        .catch(reason => {
            alert(reason.data);
        })

};

const fetchId = () => {
    return document.getElementById("id-container").innerText;
};