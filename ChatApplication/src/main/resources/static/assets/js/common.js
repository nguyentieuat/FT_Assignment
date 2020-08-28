$(document).ready(function () {

    $('.createChat').click(function () {

        $('#myModal').modal('show')
    });

    $('#chat-id-1-input').keyup(function (event) {
        if (event.keyCode == 13 && event.shiftKey) {
            event.stopPropagation();

        } else if (event.keyCode == 13) {
            event.stopPropagation();
            sendMessage(event);
        }
    });

});