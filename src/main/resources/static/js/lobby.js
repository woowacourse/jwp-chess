
function func(id) {
    let password = prompt("비밀번호 입력", "");
    $.ajax({
        type: "DELETE",
        url: "/chess/" + id,
        data: {
            "password": password,
        },
        dataType: "json",
        success: redirectIndex,
        error: alertError
    })
}
function redirectIndex() {
    window.location.href = "/";
}
function alertError(response) {
    alert(response.responseText);
}