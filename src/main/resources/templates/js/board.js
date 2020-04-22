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
    console.log(turn);
    showTurn(turn);

    let source;
    let isFirstClick = true;
    let cell;
    let before;
    var blackUserName = document.getElementsByClassName("user-name")[0].innerHTML;

    $(".cell").click(function () {
        if (document.querySelector(".selected-cell") !== null) {
            before = $(".selected-cell");
            source = before.attr("id");
            isFirstClick = false;
        }
        cell = $(this);
        let cellPosition = $(this).attr("id");
        console.log($(this).attr('id'));

        // 첫 클릭이라면 source에 포지션 저장
        if (isFirstClick) {
            source = cellPosition;

            $.ajax({
                url: "/path",
                data: { source: source, blackUserName: blackUserName },
                method: "POST",
            }).done(function (list) {
                if (list === '이동할 수 없습니다.\n해당 플레이어의 턴이 아닙니다.') {
                    alert(list);
                    return;
                }
                console.log(list);
                document.getElementById(cellPosition).classList.add("selected-cell");
                const path = list.split(",");
                path.forEach(position => {
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
            data: { source: source, target: cellPosition, blackUserName: blackUserName },
            method: "POST",
        }).done(function (model) {
            console.log(model);
            var data = JSON.parse(model);
            console.log(data);
            if (data["status"] == false) {
                if (turn % 2 == 0) {
                    alert("게임이 끝났습니다. 백팀이 승리했습니다.");
                } else {
                    alert("게임이 끝났습니다. 흑팀이 승리했습니다.");
                }
                window.location.href = '/main';
                return;
            } else if (data["message"] !== "") {
                alert(data["message"]);
                before.removeClass("selected-cell");
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
