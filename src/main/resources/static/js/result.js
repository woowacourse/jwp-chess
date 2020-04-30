const reset = document.getElementById('reset');
const winDrawLose = document.getElementById('win-draw-lose');
const submit = document.getElementById('submit');
const userNames = document.getElementById('user-names');
const roomButton = document.getElementById("room-button");

roomButton.onclick = () => {
    location.href = '/'
};

reset.onclick = () => {
    userNames.value = 1;
    userNames.focus();
    winDrawLose.innerText = "";
};

submit.onclick = () => {
    let userName = userNames.value;
    fetch('/api/result/userResult', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            userName
        })
    }).then(res => res.json()).then(data => {
        let {winCount, drawCount, loseCount} = data;
        winDrawLose.innerText = userName + ' : '
            + (winCount + drawCount + loseCount) + "전 " + winCount + "승 "
            + drawCount + "무 " + loseCount + "패";
    })
};

fetch('/api/result/viewUsers').then(res => res.json()).then(data => {
    for (let userName of data.userNames) {
        let opt = document.createElement("option");
        opt.value = userName;
        opt.textContent = userName;
        userNames.appendChild(opt);
    }
});