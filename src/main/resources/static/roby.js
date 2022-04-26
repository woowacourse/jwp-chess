async function create() {
    let inputName = prompt("방 이름을 입력하세요.");
    let inputPassword = prompt("삭제할 때 사용할 비밀번호를 입력하세요.");

    let response = await fetch("/create", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            name: inputName,
            password: inputPassword
        })
    })

    if (!response.ok) {
        errorMessage = await response.json();
        alert("[ERROR] " + errorMessage.message);
        return;
    }

    alert("방이 생성되었습니다!");
}
