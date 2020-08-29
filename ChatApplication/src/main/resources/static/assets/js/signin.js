$(document).ready(function () {

    $('#form-signin').validate({
        rules: {
            username: "required",
            password: "required"
        },

        messages: {
            username: "Please enter your username",
            password: "Please enter your password",
        }
    });

    $('#btnSignin').click(function () {
        if ($('#form-signin').valid()) {
            $('#form-signin').submit();
        }
    });
});