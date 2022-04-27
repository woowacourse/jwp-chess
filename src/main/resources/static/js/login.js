window.onload = function () {
    let modalButton = document.getElementById('room-modal-open');
    modalButton.onclick = function () {
        document.getElementById('room-modal').classList.add('show-modal');
    }

    let modal = document.getElementById('room-modal');
    Array.from(document.getElementsByClassName('modal-align')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? modal.classList.remove('show-modal') : false;
        })
    })
}

function showErrorMessage(message) {
    document.getElementById('error-message').innerHTML = message;
}

function fetchNewRoom() {
    let form = document.getElementById('room-form');
    fetch("http://localhost:8080/rooms" , {
        method: 'POST',
        body: new FormData(form)
    })
        .then(res => res.json())
        .then(res => {
            if (res.message) {
                throw new Error(res.message);
            }
            window.location.href = res.url;
        })
        .catch(err => {
            showErrorMessage(err.message);
        })
}
