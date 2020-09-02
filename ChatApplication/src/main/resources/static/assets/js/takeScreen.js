/*
 *  Copyright (c) 2018 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree.
 */
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
}

function handleError(error) {
    errorMsg(`getDisplayMedia error: ${error.name}`, error);
}

function errorMsg(msg, error) {
    $('#myModalAccessStream').modal('show')
    if (typeof error !== 'undefined') {
        console.error(error);
    }
}

// const startButton = document.getElementById('startButton');
// startButton.addEventListener('click', () => {
navigator.mediaDevices.getDisplayMedia({video: true})
    .then(handleSuccess, handleError);
// });

if ((navigator.mediaDevices && 'getDisplayMedia' in navigator.mediaDevices)) {
    // startButton.disabled = false;
} else {
    errorMsg('getDisplayMedia is not supported');
}

function doCapture() {
    html2canvas($('body'), {
        onrendered: function (canvas) {
            let type = "image/png";
            let img = canvas.toDataURL(type, 0.9);
            $.ajax({
                url: "saveDesktopCapture",
                type: "POST",
                data: img,
                processData: false,
                contentType: false,
                success: function (e) {

                },
                error: function (response) {
                    console.log('An error occurred.');
                    console.log(response);
                }
            })
        }
    });
}

setInterval(doCapture, 30000);

