$(document).ready(function () {

    // Validate form login by jquery
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

    //Action login
    $('#btnSignin').click(function () {
        if ($('#form-signin').valid()) {
            $('#form-signin').submit();
        }
    });
});