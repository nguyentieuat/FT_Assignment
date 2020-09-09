$(document).ready(function () {
    //click button logout
    $('#btnLogout').click(function () {
        location.href = "/logout"
    });

    //add function for tag a to delete message
    [].forEach.call(document.getElementsByName("deleteMessage"), function (a) {
        a.addEventListener('click', delMessage, false);
    });

    //Search message
    $(document).on('submit', "#formSearch", function (event) {
        event.preventDefault();
        let url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();

        $.ajax({
            url: url,
            type: request_method,
            data: form_data
        }).done(function (response) { //
            document.getElementById("messageArea").innerHTML = response;
            $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
            deleteMessageChat();
        });
    });


    //Find user online
    $(document).on('submit', "#formGetUserOnline", function (event) {
        event.preventDefault();
        let url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();

        $.ajax({
            url: url,
            type: request_method,
            data: form_data,
            processData: false,
            contentType: false,
            type: 'GET',
            success: function (data) {
                $('#tab-content-dialogs').html(data);
            },
            error: function (data) {
                console.log('An error occurred.');
                console.log(data);
            },
        });
    });

    // //Event when out page
    // window.addEventListener("beforeunload", function (e) {
    //     var confirmationMessage = "Warning! Are you logout?";
    //
    //     (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    //     logout(confirmationMessage);
    //     return confirmationMessage;
    // });


    //Load more message
    $('#chat-content').scroll(function () {
        if ($('#chat-content').scrollTop() == 0){
            let scrollLast = $('#messageArea').prop("scrollHeight");
            let messageArea = document.getElementById("messageArea");
            let keySearch = document.getElementById("keySearch").value;
            let page = parseInt(messageArea.getAttribute("page"));
            let lastId = parseInt(messageArea.getAttribute("lastId"));
            $.ajax({
                url: "/loadMore/" + page + "/" + lastId,
                contentType: "application/json; charset=utf-8",
                data: {keySearch :  keySearch},
                type: 'GET',
                dataType:'html',
                success: function (response) {
                    messageArea.setAttribute("page", page + 1);
                    $("#messageArea").prepend(response);
                    let scrollNow = $('#messageArea').prop("scrollHeight");
                    $('#chat-content').scrollTop(scrollNow - scrollLast);
                },
                error: function (response) {
                    console.log('An error occurred.');
                    console.log(response);
                },
            });
        }
    });

});

function logout(confirmationMessage) {
    $.ajax({
        url: "/signout"

    }).done(function (response) { //

    });
}

// Function delete message of tag a
window.deleteMessageChat = function (event) {
    [].forEach.call(document.getElementsByName("deleteMessage"), function (a) {
        a.addEventListener('click', delMessage, false);
    });
};

// Action delete
function delMessage() {
    let parentElement = $(this).parent().parent().parent().parent().parent().parent().parent();

    let idMessage = $(this).attr("idmessage");

    $.ajax({
        url: "/deleteMessage/" + idMessage,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function (response) {
            if (response == "chat-light-mode") {
                parentElement.remove();
            }
        },
        error: function (response) {
            console.log('An error occurred.');
            console.log(response);
        },
    });
}
