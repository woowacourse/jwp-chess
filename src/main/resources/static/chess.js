var main = {
    variables: {
        pieces: {
            k: {
                img: '&#9812;'
            },
            q: {
                img: '&#9813;'
            },
            b: {
                img: '&#9815;'
            },
            n: {
                img: '&#9816;'
            },
            r: {
                img: '&#9814;'
            },
            p: {
                img: '&#9817;'
            },
            K: {
                img: '&#9818;'
            },
            Q: {
                img: '&#9819;'
            },
            B: {
                img: '&#9821;'
            },
            N: {
                img: '&#9822;'
            },
            R: {
                img: '&#9820;'
            },
            P: {
                img: '&#9823;'
            }
        }
    }
}

$(document).ready(function () {
    $('.gamecell').attr({
        "draggable": "true",
        "ondragstart": "drag(event)",
        "ondrop": "drop(event)",
        "ondragover": "allowDrop(event)"
    });
});

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var now = ev.dataTransfer.getData("text");
    var des = ev.target.id;
    var game_id = document.getElementById("game_id").innerHTML;
    move({"game_id": game_id, "now": now.toString(), "des": des.toString()});
}

function postNewGame() {
    const game_name = document.getElementById("game_name").value;
    $.ajax({
        type: 'post',
        url: '/api/game',
        data: {"game_name": game_name},
        dataType: 'text',
        error: function (xhr, status, error) {
            alert(error.toString());
        },
        success: function (data) {
            const jsonData = JSON.parse(data);

            const game_id = jsonData.id;

            console.log(game_id);
            window.location.href = "/index/" + game_id;
        }
    });
}

function move(json) {
    $.ajax({
        type: 'put',
        url: '/api/piece',
        data: json,
        dataType: 'text',
        error: function (xhr, status, error) {
            alert(error.toString());
        },
        success: function (data) {
            var jsonData = JSON.parse(data);

            if (jsonData.progress == "CONTINUE") {
                var nowImg = $('#' + json.now).html();
                $('#' + json.des).html(nowImg);
                $('#' + json.des).attr('chess', $('#' + json.now).chess);
                $('#' + json.now).html('');
                $('#' + json.now).attr('chess', 'null');

                $('#whiteScore').html("whiteScore : " + jsonData.chessGameScoresDto.whiteScore.value);
                $('#blackScore').html("blackScore : " + jsonData.chessGameScoresDto.blackScore.value);

                $('#turn').html("It's " + jsonData.turn + " Turn!");
            }
            if (jsonData.progress == "ERROR") {
                alert("움직일 수 없는 경우입니다.");
            }
            if (jsonData.progress == "END") {
                getChessBoardResult();
                deleteChessGame();
            }
        }
    });
}

function getChessBoardResult() {
    const id = document.getElementById("game_id").innerHTML;
    $.ajax({
        url: "/api/games/" + id + "/result",
        type: "get",
        success: function (data) {
            var jason = JSON.parse(data);
            alert("승자는" + jason.name + "입니다.");
            if (confirm('메인 페이지로 이동하겠습니까?')) {
                window.location.href = "/"
            }
        },
        error: function (errorThrown) {
            alert(errorThrown);
        },
    });
}

function deleteChessGame() {
    const game_id = document.getElementById("game_id").innerHTML;
    $.ajax({
        url: "/api/games/" + game_id,
        type: "delete",
        success: function (data) {
        },
        error: function (errorThrown) {
            alert(errorThrown);
        },
    });
}

function load() {
    const gameId = document.getElementById("game_id").innerHTML;

    $.ajax({
        url: "/api/game/" + gameId,
        type: "get",
        success: function (data) {

            $('.board').css('display', 'block');
            $('.gamecell').html('');
            $('.gamecell').attr('chess', 'null');
            $('#game_id').html(gameId);

            $('.gamecell grey').html('');
            $('.gamecell grey').attr('chess', 'null');

            var jsonData = JSON.parse(data);
            for (var i = 0; i < jsonData.boardDto.boardValue.length; i++) {
                var piece = jsonData.boardDto.boardValue[i];
                $('#' + piece.location).html(main.variables.pieces[piece.pieceName].img);
                $('#' + piece.location).attr('chess', piece.pieceName);
            }

            $('#whiteScore').html("White score : " + jsonData.whiteScore);
            $('#blackScore').html("Black score : " + jsonData.blackScore);

            var turn = "white";
            if (jsonData.turnIsBlack === "1") {
                turn = "black";
            }
            $('#turn').html("It's " + turn + " Turn!");
        },
        error: function (errorThrown) {
            alert(JSON.stringify(errorThrown));
        },
    });
}