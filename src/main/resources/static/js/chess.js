function score() {
    $.ajax({
        type: 'get',
        url: '/status/' + fetchId(),
        dataType: 'text',
        success(data, status, jqXHR) {
            console.log(status);
            console.log(jqXHR.responseText);
            alert(data);
        },
        error(jpXHR, textError, errorThrown) {
            console.log(textError);
            console.log(errorThrown);
            alert(jpXHR.responseText);
        }
    });
}

function save() {
    $.ajax({
        type: 'post',
        url: '/save/' + fetchId(),
        dataType: 'text',
        success(data, status, jqXHR) {
            alert("저장을 완료했습니다.");
        },
        error(jpXHR, textError, errorThrown) {
            console.log(textError);
            console.log(errorThrown);
            alert(jpXHR.responseText);
        }
    });
}

function restart() {
    $.ajax({
        type: 'post',
        url: '/restart/' + fetchId(),
        dataType: 'text',
        success(data, status, jqXHR) {
            location.href = "/chess/" + fetchId();
        },
        error(jpXHR, textError, errorThrown) {
            console.log(textError);
            console.log(errorThrown);
            alert(jpXHR.responseText);
        }
    });
}

function end() {
    $.ajax({
        type: 'post',
        url: '/end/' + fetchId(),
        dataType: 'text',
        success(data, status, jqXHR) {
            alert("게임을 종료했습니다");
            location.href = "/";
        },
        error(jpXHR, textError, errorThrown) {
            console.log(textError);
            console.log(errorThrown);
            alert(jpXHR.responseText);
        }
    });
}
