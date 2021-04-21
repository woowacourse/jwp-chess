function addSubmitButtonEvent() {
    const $submitButton = document.getElementById('submit-button');
    $submitButton.addEventListener('click', requestLogin);
}

const requestLogin = () => {
    const password = document.getElementById('room-player-password').value;
    const requestData = JSON.stringify({
        "roomId": roomId,
        "password": password
    });
    axios.post('/login', requestData);
};

addSubmitButtonEvent();
