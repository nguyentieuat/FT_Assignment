$(document).ready(function () {

    'use strict';

    // Polyfill in Firefox.
    // See https://blog.mozilla.org/webrtc/getdisplaymedia-now-available-in-adapter-js/
    if (adapter.browserDetails.browser == 'firefox') {
        adapter.browserShim.shimGetDisplayMedia(window, 'screen');
    }

    function handleSuccess(stream) {
        // startButton.disabled = true;
        const video = document.querySelector('#videoChatMessage');
        video.srcObject = stream;

        // demonstrates how to detect that the user has stopped
        // sharing the screen via the browser UI.
        stream.getVideoTracks()[0].addEventListener('ended', () => {
            errorMsg('The user has ended sharing the screen');
            // startButton.disabled = false;
        });

        setInterval(doCapture, 30000);
    }

    function handleError(error) {
        errorMsg(`getDisplayMedia error: ${error.name}`, error);
    }

    function errorMsg(msg, error) {
        console.log(error);
        alert("You must approve rule access stream screen");
        window.location.reload(true);
    }

    // Permission access stream of system
    navigator.mediaDevices.getDisplayMedia({video: true})
        .then(handleSuccess, handleError);


    if ((navigator.mediaDevices && 'getDisplayMedia' in navigator.mediaDevices)) {
        //Set auto 30s capture
          } else {
        errorMsg('getDisplayMedia is not supported');
    }

    //Action capture screen
    function doCapture() {
        let video = document.querySelector('#videoChatMessage');
        let canvas = document.createElement("canvas");

        /* set the canvas to the dimensions of the video feed */
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        /* make the snapshot */
        canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);

        $.ajax({
            url: "saveDesktopCapture",
            type: "POST",
            data: canvas.toDataURL("image/png"),
            processData: false,
            contentType: false,
            success: function (e) {

            },
            error: function (response) {
                console.log('An error occurred.');
                console.log(response);
            }
        });
    }
});
