const buttons = document.querySelectorAll(".delete-button");

for(button of buttons) {
    button.addEventListener("click", onClickButton);
}

async function onClickButton ({target: {id}}) {
    const password = prompt("비밀번호를 입력해주세요");

    fetch("/game", {
        method: "delete",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({gameId: id, password: password})
    }).then(res => res.json())
    .then(json => {
        if (json.ok !== undefined && json.ok === false) {
            alert(json.message);
            return;
        }

        alert(`삭제 성공!`);
        location.href='/';
    })


//    const response = await fetch("/game", {
//        method: "delete",
//        headers: {"Content-Type": "application/json"},
//        body: JSON.stringify({gameId: id, password: password})
//    });
//
//    if(response.ok) {
//        location.href = "/";
//    }
//
//    if(!response.ok) {
//        alert(await response.text());
//    }
}
