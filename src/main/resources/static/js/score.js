function getScore() {
    $.ajax({
        url: "/status",
        type: "GET",
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            document.getElementById("whiteScore").innerText = "White팀 점수: " + data["whiteScore"] + "점";
            document.getElementById("blackScore").innerText = "Black팀 점수: " + data["blackScore"] + "점";
        },
        error: function (data){
            alert(data);
        }
    })
}