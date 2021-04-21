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
    axios.post('/login', requestData, {headers: {'Content-Type': 'application/json'}})
        .then(response => window.location.replace(response.data))
        .catch(error => alert(error.response.data));
};

addSubmitButtonEvent();
