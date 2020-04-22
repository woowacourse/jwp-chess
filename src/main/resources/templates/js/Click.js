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

function selectDestinationPiece(value) {
    $.ajax({
        type: "post",
        url: "/move/" + fetchId(),
        data: {
            source: document.getElementsByClassName("sourcePosition")[0].id,
            target: document.getElementById(value).id
        },
        dataType: "text",
        success: function (data, status, jqXHR) {
            if(data === "true") {
                alert("게임이 종료되었습니다.");
            }
            //Todo 게임이 종료 되었을 때 메시지
            movePiece(value);
        },
        error: function (jqXHR) {
            resetSourcePosition();
            console.log('error');
            alert(jqXHR.responseText);
        }
    });
}

const fetchId = () => {
   return document.getElementById("id-container").innerText;
};