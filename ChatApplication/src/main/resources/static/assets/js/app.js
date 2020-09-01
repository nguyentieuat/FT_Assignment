$(document).ready(function () {
    $('#btnLogout').click(function () {
        location.href = "/logout"
    });

    [].forEach.call(document.getElementsByName("deleteMessage"), function (a) {
        a.addEventListener('click', myFunction, false);
    });

    function myFunction() {
       let parentElement =  $(this).parent().parent().parent().parent().parent().parent().parent();

        let idMessage = $(this).attr("idmessage");
        $.ajax({
            url: "/deleteMessage/" + idMessage,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (response) {
                console.log(response == "chat-light-mode")
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


    $("#formSearch").submit(function(event){
        event.preventDefault();
        var post_url = $(this).attr("action");
        var request_method = $(this).attr("method");
        var form_data = $(this).serialize();

        $.ajax({
            url : post_url,
            type: request_method,
            data : form_data
        }).done(function(response){ //
            $("#messageArea").html(response);
            $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
        });
    });

    // window.addEventListener("beforeunload", function (e) {
    //     var confirmationMessage = "Warning! Are you logout?";
    //
    //     (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    //     logout(confirmationMessage);
    //     return confirmationMessage;
    // });
    //
    // function logout(confirmationMessage) {
    //     $.ajax({
    //         url: "/signout"
    //
    //     }).done(function(response){ //
    //
    //     });
    //
    //
    // }
});