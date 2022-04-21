window.addEventListener("load", function(){

    var section = this.document.querySelector(".chess-ui");
    var pngs = section.querySelectorAll(".img");
    var positions = "";

    for (var i = 0; i < pngs.length; i++) {
        pngs[i].onclick = function(event) {
            var parentTarget = event.target.parentElement;
            positions += parentTarget.id;

            if (positions.length == 4) {
                var source = positions.substring(0, 2);
                var target = positions.substring(2, 4);
                JsonSender.sendSourceTarget(source, target);
                positions = "";
            }
        }
    }
});

const JsonSender = {
    sendSourceTarget: function(source, target) {
        fetch('/move', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                source: source,
                target: target
            })
        })
        .then((response) => {
            return response.json().then((data) => {
                console.log(data);
                if (data.isGameOver === true) {
                    alert(data.winner + "가 승리하였습니다!!!");
                    window.location.replace("end");
                } else if (data.isMovable === false) {
                    alert('이동할 수 없습니다.');
                } else {
                    window.location.reload();
                }
            });
        });
    }
}