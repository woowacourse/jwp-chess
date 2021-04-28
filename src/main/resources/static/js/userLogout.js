export let logoutBtn = document.getElementById("logout");

logoutBtn.addEventListener("click", askLogout);

function askLogout() {
    if (confirm("로그아웃 하시겠습니까?")) {
        logout();
    }
}

function logout() {
    const postOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }

    fetch("/logout", postOption)
        .then(response => {
            if (!response.ok) {
                throw new Error(response.status);
            }
            return response.text();
        })
        .then(data => {
            location.reload();
        })
        .catch(error => {
            alert("로그아웃에 실패했습니다.");
        });
}
