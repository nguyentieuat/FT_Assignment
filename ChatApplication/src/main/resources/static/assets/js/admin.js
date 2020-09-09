$(document).ready(function () {

    //click button logout
    $('#btnLogout').click(function () {
        location.href = "/logout"
    });

    //Add function for elements
    [].forEach.call(document.getElementsByName("messageUsername"), function (a) {
        a.addEventListener('click', getMessageByUsername, false);
    });

    [].forEach.call(document.getElementsByName("captureUsername"), function (a) {
        a.addEventListener('click', getCaptureByUsername, false);
    });

    //Search user on screen message
    $(document).on('submit', "#searchUserTabMessage", function (event) {
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();


        $.ajax({
            url: post_url,
            type: request_method,
            data: form_data,
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

    //Search user on screen capture
    $(document).on('submit', "#searchUserTabCapture", function (event) {
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();


        $.ajax({
            url: post_url,
            type: request_method,
            data: form_data,
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


    //Search message on screen message
    $(document).on('submit', "#formSearchMessage", function (event) {
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();

        $.ajax({
            url: post_url,
            type: request_method,
            data: form_data,
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

    //Search capture on screen capture
    $(document).on('submit', "#formSearchCapture", function (event) {
        event.preventDefault();
        let post_url = $(this).attr("action");
        let request_method = $(this).attr("method");
        let form_data = $(this).serialize();

        $.ajax({
            url: post_url,
            type: request_method,
            data: form_data,
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

    $(document).on('scroll', '#chat-content', function () {
        alert("Hello")
        if ($('#chat-content').scrollTop() == 0) {
            alert("Hello")
        }
    });
});

//Add function for elements
window.getMessageAccount = function () {
    [].forEach.call(document.getElementsByName("messageUsername"), function (a) {
        a.addEventListener('click', getMessageByUsername, false);
    });
}

//Declare function
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
            let height = document.querySelector('#messageArea').scrollHeight;
            $('#chat-content').scrollTop(height);
        },
        error: function (response) {
            console.log('An error occurred.');
            console.log(response);
        },
    });
}


// document.querySelector('#chat-content').scroll(function () {
//     if ($('#chat-content').scrollTop() == 0) {
//         alert("Hello")
//     }
// });

function loadMoreCapture() {
    if ($('#chat-content').scrollTop() == 0) {
        let scrollLast = $('#messageArea').prop("scrollHeight");
        let messageArea = document.getElementById("messageArea");
        let keySearch = $('#keySearch').val();
        let page = parseInt(messageArea.getAttribute("page"));
        let username = messageArea.getAttribute("username");
        let lastId = parseInt(messageArea.getAttribute("lastId"));
        $.ajax({
            url: "/admin/loadMoreCapture/" + username + "/" + lastId + "/" + page,
            contentType: "application/json; charset=utf-8",
            data: {keySearch: keySearch},
            type: 'GET',
            dataType: 'html',
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
}