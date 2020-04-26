let player1Name = prompt("당신은 플레이어 1입니다. 이름을 설정해주세요.");
let player1Password = prompt("당신은 플레이어 1입니다. 비밀번호를 설정해주세요.");
let player2Name = prompt("당신은 플레이어 2입니다. 이름을 설정해주세요.");
let player2Password = prompt("당신은 플레이어 2입니다. 비밀번호를 설정해주세요.");

fetch("http://localhost:4567/createNewGame", {
    method: 'POST', body: JSON.stringify({
        player1Name, player1Password, player2Name, player2Password
    })
}).then(res => res.json())
    .then(data => {
        location.replace("/startGame/" + data.id);
    });