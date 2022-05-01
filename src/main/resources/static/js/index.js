const buttons = document.querySelectorAll(".delete-button");

for(button of buttons) {
    button.addEventListener("click", onClickButton);
}

async function onClickButton ({target: {id}}) {
    const password = prompt("비밀번호를 입력해주세요");

    const response = await fetch("/game", {
        method: "delete",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({gameId: id, password: password})
    });

    if(!response.ok) {
        return alert(await response.text());
    }

    alert("삭제 성공");
    location.href = "/";
}
