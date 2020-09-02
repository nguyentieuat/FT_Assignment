$(document).ready(function () {
    [].forEach.call(document.getElementsByName("messageUsername"), function (a) {
        a.addEventListener('click', getMessageByUsername, false);
    });

    [].forEach.call(document.getElementsByName("captureUsername"), function (a) {
        a.addEventListener('click', getCaptureByUsername, false);
    });

    $(document).on('submit',"#searchUserTabMessage",function(event){
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();


        $.ajax({
            url : post_url,
            type: request_method,
            data : form_data,
            processData: false,
            contentType: false,
            type: 'GET',
            success: function (data) {
                $("#chat-content-body").html("");
                document.getElementById("tab-content-message").innerHTML = data;
            },
            error: function (data) {
                console.log('An error occurred.');
                console.log(data);
            },
        }).done(function () {
            getMessageAccount();
        });
    });

    $(document).on('submit',"#searchUserTabCapture",function(event){
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();


        $.ajax({
            url : post_url,
            type: request_method,
            data : form_data,
            processData: false,
            contentType: false,
            type: 'GET',
            success: function (data) {
                $("#chat-content-body").html("");
                document.getElementById("tab-content-message").innerHTML = data;
            },
            error: function (data) {
                console.log('An error occurred.');
                console.log(data);
            },
        }).done(function () {
            getCaptureAccount();
        });
    });

    $(document).on('submit',"#formSearchMessage",function(event){
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();

        $.ajax({
            url : post_url,
            type: request_method,
            data : form_data,
            processData: false,
            contentType: false,
            type: 'GET',
            success: function (data) {
                $('#chat-content-body').html('').html(data)
                $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
            },
            error: function (data) {
                console.log('An error occurred.');
                console.log(data);
            },
        }).done(function () {
            getCaptureAccount();
        });
    });

    $(document).on('submit',"#formSearchCapture",function(event){
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();

        $.ajax({
            url : post_url,
            type: request_method,
            data : form_data,
            processData: false,
            contentType: false,
            type: 'GET',
            success: function (data) {
                $('#chat-content-body').html('').html(data)
                $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
            },
            error: function (data) {
                console.log('An error occurred.');
                console.log(data);
            },
        }).done(function () {
            getCaptureAccount();
        });
    });
});

window.getMessageAccount = function () {
    [].forEach.call(document.getElementsByName("messageUsername"), function (a) {
        a.addEventListener('click', getMessageByUsername, false);
    });
}

function getMessageByUsername() {
    [].forEach.call(document.getElementsByName("messageUsername"), function (a) {
        $(a).removeClass("active");
    });

    let username = $(this).attr("idAccount");
    $(this).addClass("active");

    $.ajax({
        url: "/admin/getAllMessage/" + username,
        processData: false,
        contentType: false,
        type: 'GET',
        success: function (response) {
            $('#chat-content-body').html(response)
            $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
        },
        error: function (response) {
            console.log('An error occurred.');
            console.log(response);
        },
    });
}

window.getCaptureAccount = function () {
    [].forEach.call(document.getElementsByName("captureUsername"), function (a) {
        a.addEventListener('click', getCaptureByUsername, false);
    });
}

function getCaptureByUsername() {
    [].forEach.call(document.getElementsByName("captureUsername"), function (a) {
        $(a).removeClass("active");
    });

    let username = $(this).attr("idAccount");
    $(this).addClass("active");

    $.ajax({
        url: "/admin/getAllCapture/" + username,
        processData: false,
        contentType: false,
        type: 'GET',
        success: function (response) {
            $('#chat-content-body').html(response)
            $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
        },
        error: function (response) {
            console.log('An error occurred.');
            console.log(response);
        },
    });
}