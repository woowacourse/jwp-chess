const deleteButtons = document.querySelectorAll(".delete-button");

for (deleteButton of deleteButtons) {
    deleteButton.addEventListener("click", onClickDeleteButton);
}

async function onClickDeleteButton({target: {id}}) {
    const password = prompt("비밀번호를 입력해주세요");

    const response = await fetch(`/games/${id}`, {
        method: "delete",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({password: password})
    });

    if (!response.ok) {
        return alert(await response.text());
    }

    alert("삭제 성공");
    location.href = "/";
}
