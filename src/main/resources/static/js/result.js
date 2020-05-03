const roomButton = document.getElementById("room-button");
const resetButton = document.getElementById('reset');
const submitButton = document.getElementById('submit');
const winDrawLoseDiv = document.getElementById('win-draw-lose');
const userNamesCombo = document.getElementById('user-names');

roomButton.onclick = () => {
    location.href = '/'
};

resetButton.onclick = () => {
    userNamesCombo.value = 1;
    userNamesCombo.focus();
    winDrawLoseDiv.innerText = "";
};

submitButton.onclick = () => {
    let userName = userNamesCombo.value;

    fetch('/api/result/userResult?userName=' + userName).then(
        res => res.json()).then(data => {
        setWinOrDraw(data);
    });

    function setWinOrDraw(data) {
        let {winCount, drawCount, loseCount} = data;
        winDrawLoseDiv.innerText = userName + ' : '
            + (winCount + drawCount + loseCount) + "전 " + winCount + "승 "
            + drawCount + "무 " + loseCount + "패";
    }
};

fetch('/api/result/users').then(res => res.json()).then(data => {
    setUserNames(data);
});

function setUserNames(data) {
    for (let userName of data.userNames) {
        let opt = document.createElement("option");
        opt.value = userName;
        opt.textContent = userName;
        userNamesCombo.appendChild(opt);
    }
}
