window.onload = function() {
    for (let i = 0; i < 64; i++) {
        let cell = "white-cell"
        if (((Math.floor(i / 8) % 2 === 0) && (i % 2 === 0)) || (Math.floor(i / 8) % 2 === 1) && (i % 2 === 1)) {
            // 홀수 줄의 홀수 번째 칸 또는 짝수줄의 짝수번째 칸이라면
            cell = "black-cell"
        }
        $(".cell:eq(" + i + ")").addClass(cell);
    }

    let turn = $(".turn-number").text();
    showTurn(turn);

    let source;
    let isFirstClick = true;
    let cell;
    let before;
    var roomName = $("#roomName").val();

    $(".cell").click(function () {
        if (document.querySelector(".selected-cell") !== null) {
            before = $(".selected-cell");
            source = before.attr("id");
            isFirstClick = false;
        }
        cell = $(this);
        let cellPosition = $(this).attr("id");

        // 첫 클릭이라면 source에 포지션 저장
        if (isFirstClick) {
            source = cellPosition;
            console.log(source);

            $.ajax({
                url: "/path",
                data: { source: source, roomName: roomName },
                method: "POST",
            }).done(function (list) {
                console.log(list);
                if (list[0] === '이동할 수 없습니다.\n해당 플레이어의 턴이 아닙니다.') {
                    alert(list);
                    return;
                }
                document.getElementById(cellPosition).classList.add("selected-cell");
                list.forEach(position => {
                    document.getElementById(position).classList.add("y");
                })
            });
            return;
        }

        // 두번째 클릭이지만 똑같은 기물을 선택했다면 선택 취소
        if (!isFirstClick && (source === cellPosition)) {
            source = null;
            isFirstClick = true;
            before.removeClass("selected-cell");
            $(".y").removeClass("y");
            return;
        }

        // 두번째 클릭이라면 이동
        $.ajax({
            url: "/move",
            data: { source: source, target: cellPosition, roomName: roomName },
            method: "POST",
        }).done(function (model) {
            var data = model;  //JSON.parse(model);
            console.log(data);
            console.log()
            if (data["isNotFinished"] == false) {
                if (turn % 2 == 0) {
                    alert("게임이 끝났습니다. 백팀이 승리했습니다.");
                } else {
                    alert("게임이 끝났습니다. 흑팀이 승리했습니다.");
                }
                window.location.href = '/';
                return;
            } else if (data["message"] !== "") {
                alert(data["message"]);
                return;
            }
            move(source, cellPosition);
            before.removeClass("selected-cell");
            $(".y").removeClass("y");
            isFirstClick = true;
            source = null;
            turn++;
            showTurn(turn);
            document.getElementById("black-score").innerHTML = data["black"] + "점";
            document.getElementById("white-score").innerHTML = data["white"] + "점";
        });
    });

    function move(from, to) {
        var img = document.createElement("IMG");
        img.src = "./img/NONE_..png";
        document.getElementById(to).replaceChild(document.getElementById(from).firstElementChild,
            document.getElementById(to).firstElementChild);
        document.getElementById(from).appendChild(img);
    }

    function showTurn(turn) {
        if (turn % 2 === 0) {
            document.getElementById("black-turn").style.opacity = 0;
            document.getElementById("white-turn").style.opacity = 1;
        } else {
            document.getElementById("black-turn").style.opacity = 1;
            document.getElementById("white-turn").style.opacity = 0;
        }
    }
}
