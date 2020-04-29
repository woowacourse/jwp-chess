const startGame = document.getElementById("start-game");
const continueGame = document.getElementById("continue-game");
const startButton = document.getElementById("start-button");
const continueButton = document.getElementById("continue-button");
const blackName = document.getElementById("black-name");
const whiteName = document.getElementById("white-name");
const blackNameContinue = document.getElementById("black-name-continue");
const whiteNameContinue = document.getElementById("white-name-continue");
const roomButton = document.getElementById("room-button");

startButton.onclick = () => {
    if (checkNames()) {
        startGame.submit();
    }
};

roomButton.onclick = () => {
    location.href = '/'
};

continueButton.onclick = () => {
    if (checkNames()) {
        whiteNameContinue.value = whiteName.value;
        blackNameContinue.value = blackName.value;
        continueGame.submit();
    }
};

function checkNames() {
    if (blackName.value.toUpperCase() === "WHITE") {
        alert("Black팀의 이름은 WHITE로 지정할 수 없습니다.");
        return false;
    }
    if (whiteName.value.toUpperCase() === "BLACK") {
        alert("White팀의 이름은 BLACK으로 지정할 수 없습니다.");
        return false;
    }
    if ((blackName.value !== "" || whiteName.value !== "")
        && blackName.value === whiteName.value) {
        alert("Black팀과 White 팀의 이름은 같을 수 없습니다.");
        return false;
    }
    return true;
}

