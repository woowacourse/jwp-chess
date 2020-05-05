$('.create').hide();

$('.new_room').click(function () {
    $('.create').show();
});

$('.complete').click(function () {
    let title = $('#room_name').val();
    console.log(title);
    $.ajax({
        type: 'get',
        url: '/create',
        data: {'title': title},
        dataType: 'text',
        success: function (roomId) {
            location.href = "open?roomId=" + roomId
        }
    });
});

$('.list').click(function () {
    let roomId = $(this).find('td:eq(0)').text();
    location.href = "open?roomId=" + roomId

});