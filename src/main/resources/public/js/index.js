// setTurn();

// 체스 말 선택
function pick(value) {
    console.log(value);
    if (document.querySelector('.fromPiece')) {
        pickAfter(value);
        document.querySelector('.fromPiece').style.removeProperty('border');

    } else {
        pickBefore(value);
    }
}

function pickBefore(beforeValue) {
    document.getElementById(beforeValue).classList.add('fromPiece');
    document.getElementById(beforeValue).style.borderColor = 'red';
}

async function pickAfter(afterValue) {
    await document.getElementById(afterValue).classList.add('toPiece');
    await move();
}

// 체스말 이동
function setPiece(response) {
    console.log(response);
    if (response === "BLACK" || response === "WHITE") {
        let finish_box = document.getElementById("winner-box");
        finish_box.style.visibility = "visible";

        const winner = document.getElementById("winner-message");
        winner.innerText = response + "이(가) 승리했습니다. ";
        return;
    }

    const moveSquares = response.split(" ");
    const fromPiece = document.getElementById(moveSquares[0]);
    const toPiece = document.getElementById(moveSquares[1]);

    toPiece.src = fromPiece.src;
    fromPiece.src = "../images/BLANK.png";
}

// 체스말 이동 : 비동기
function move() {
    let fromPiece = document.querySelector('.fromPiece').id;
    let toPiece = document.querySelector('.toPiece').id;
    let room_id = document.getElementById('room_id').innerText;
    $.ajax({
        type: 'PUT',
        async: true,
        url: `/room/${room_id}/move`,
        data: {
            fromPiece,
            toPiece,
            room_id
        },
        dataType: 'text',
        error: alertMessage,
        success: setPiece,
        complete: function () {
            clear();
            // setTurn()
        }
    });
}

function clear() {
    let before = document.querySelector('.fromPiece');
    let after = document.querySelector('.toPiece');
    if (before) {
        before.classList.remove('fromPiece');
    }
    if (after) {
        after.classList.remove('toPiece');
    }
}

function setScore() {
    let room_id = document.getElementById('room_id').innerText;
    $.ajax({
        type: 'GET',
        async: true,
        url: `/room/${room_id}/score`,
        data: {
            room_id
        },
        dataType: 'json',
        error: alertMessage,
        success: function (response) {
            console.log(response);
            document.getElementById("black-score").innerText = "Black : " + response.black;
            document.getElementById("white-score").innerText = "White : " + response.white;
        },
        complete: function () {
        }
    })
}

// 게임 턴 : 비동기
// function setTurn(){
//     $.ajax({
//         type : 'GET',
//         async: true,
//         url: '/turn',
//         dataType: 'text',
//         error: alertMessage,
//         success: function (response) {
//             document.getElementById("current-turn").innerText = response + " 의 차례입니다.";
//         }
//     })
// }

// 에러 메세지
function alertMessage(response) {
    alert(response.responseText);
}
