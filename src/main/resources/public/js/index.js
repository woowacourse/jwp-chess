setTurn();

// 점수 현황 동적 렌더링
let scoreStatus = document.getElementById("score-status");
 let score = document.querySelector("#score-status span");
 if (score.innerText === "") {
    scoreStatus.style.visibility = "hidden"
} else {
    scoreStatus.style.visibility = "visible";
}

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
    if(response === "BLACK" || response === "WHITE"){
        let finish_box = document.getElementById("winner-box");
        finish_box.style.visibility = "visible";

        const winner = document.getElementById("winner-message");
        winner.innerText = response + "이(가) 승리했습니다. " + " 다시 시작하기 버튼을 눌러 새로 시작해주세요.";
        return;
    }

    const moveSquares = response.split(" ");
    const fromPiece = document.getElementById(moveSquares[0]);
    const toPiece = document.getElementById(moveSquares[1]);

    toPiece.src = fromPiece.src;
    fromPiece.src = "../images/BLANK.png";
    scoreStatus.style.visibility = 'hidden';
}

// 체스말 이동 : 비동기
function move() {
    $.ajax({
        type: 'POST',
        async: true,
        url: '/move',
        data: {
            fromPiece: document.querySelector('.fromPiece').id,
            toPiece: document.querySelector('.toPiece').id
        },
        dataType: 'text',
        error: alertMessage,
        success: setPiece,
        complete: function(){
            clear();
            setTurn();
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

// 게임 턴 : 비동기
function setTurn(){
    $.ajax({
        type : 'GET',
        async: true,
        url: '/turn',
        dataType: 'text',
        error: alertMessage,
        success: function (response) {
            document.getElementById("current-turn").innerText = response + " 의 차례입니다.";
        }
    })
}

// 에러 메세지
 function alertMessage(response) {
     alert(response.responseText);
 }
