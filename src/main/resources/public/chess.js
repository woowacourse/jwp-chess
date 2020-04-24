var click_flag = false;
var source = "";
var target = "";

document.addEventListener("DOMContentLoaded", function () {
    $('.piece-image').click(function () {
        if (!click_flag) {
            console.log("click");
            source = $(this);
        }
        if (click_flag) {
            console.log("next click");
            console.log(source);
            console.log($(this).attr('id'));
            target = $(this);
            console.log(document.getElementById('room-name').innerText);
            $.ajax({
                url: '/chess/move',
                type: 'POST',
                data: {
                    roomName: document.getElementById('room-name').innerText,
                    source: source.attr('id'),
                    target: target.attr('id')
                },
                dataType: 'text',
                success: function (data) {
                    console.log(data);
                    target.attr('src', source.attr('src'));
                    source.attr('src', `/img/blank.png`);
                },
                error: function (e) {
                    alert(e.message);
                }
            })
        }
        click_flag = !click_flag;

    });
});

function exitRoom() {
    alert("방에서 나왔습니다.");
}