$(document).ready(function () {
    'use strict';
    var messageArea = document.querySelector('#messageArea');

    var stompClient = null;
    var username = null;


    function connect() {
        username = $('#username').text().trim();

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }

    // Connect to WebSocket Server.
    connect();

    function onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe('/topic/publicChatRoom', onMessageReceived);

        // Tell your username to the server
        stompClient.send("/app/chat.addUser",
            {},
            JSON.stringify({createdBy: username, type: 'JOIN'})
        )
    }

    function onError(error) {
        var connectingElement = document.createElement("div");
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
        $('#chat-content').append(connectingElement);
    }

    //Action send message
    function sendMessage(event) {
        let messageContent = $('#chat-id-1-input').val().trim();

        let files = $('#chat-1')[0].dropzone.files;

        if (files.length > 0) {
            let formData = new FormData();
            for (let i in files) {
                formData.append('files', files[i]);
            }
            formData.append("createdBy", username);
            formData.append("content", messageContent);
            formData.append("type", 'CHAT');

            $.ajax({
                url: "/app/chat.uploadFile",
                data: formData,
                processData: false,
                contentType: false,
                type: 'POST',
                success: function (response) {
                    $('#previewFile').empty();
                    ;
                    $('#chat-1')[0].dropzone.removeAllFiles(true);
                    $("#messageArea").append(response);
                    $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);
                }
            });
        } else {
            if (messageContent && stompClient) {
                var chatMessage = {
                    createdBy: username,
                    content: messageContent,
                    type: 'CHAT'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                $('#chat-id-1-input').val('');
                $('#chat-id-1-input').css('height', '46px');
            }
        }

        event.preventDefault();
    }

    // Change key up shift+enter to line break, enter to sent
    $('#chat-id-1-input').keyup(function (event) {
        if (event.keyCode == 13 && event.shiftKey) {
            event.stopPropagation();

        } else if (event.keyCode == 13) {
            event.stopPropagation();
            sendMessage(event);
        }
    });


    //Action when receive a message
    function onMessageReceived(payload) {

        $.ajax({
            url: "/getUserOnline",
            processData: false,
            contentType: false,
            type: 'GET',
            success: function (response) {
               $('#tab-content-dialogs').html(response);
            }
        });


        let message = JSON.parse(payload.body);

        let messageElement = document.createElement('li');
        let textElement = document.createElement('span');
        let messageText;
        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.createdBy + ' joined!';

            messageText = document.createTextNode(message.content)
            textElement.appendChild(messageText);
            messageArea.appendChild(messageElement);
            messageElement.appendChild(textElement);

        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.createdBy + ' left!';

            messageText = document.createTextNode(message.content)
            textElement.appendChild(messageText);
            messageArea.appendChild(messageElement);
            messageElement.appendChild(textElement);
        } else {
            $('#messageArea').append(addHtmlMessage(message));
        }

        $('#chat-content').animate({scrollTop: $('#messageArea').prop("scrollHeight")}, 333);

    }

    //Show message on screen
    function addHtmlMessage(message) {
        let idMessage = message.id;
        let isOwner = username == message.usernameSender;
        let divClassMain = 'message';
        let divClassMessageRow = 'd-flex align-items-center';
        let tagHtmlUserName = '<h6 class="mb-2" text="' + username + '"></h6>';
        let tagHtmlDropdownBefore = '<div class="dropdown dropleft">' +
            '                                <a class="text-muted opacity-60 ml-3" href="#" data-toggle="dropdown"' +
            '                                   aria-haspopup="true" aria-expanded="false">' +
            '                                    <i class="fe-more-vertical"></i>' +
            '                                </a>' +
            '                                <div class="dropdown-menu">' +
            '                                    <a class="dropdown-item d-flex align-items-center" href="#" idMessage="'+ idMessage + '"  id="editMessage" >' +
            '                                        Edit <span class="ml-auto fe-edit-3"></span>' +
            '                                    </a>' +
            // '                                    <a class="dropdown-item d-flex align-items-center" href="#">' +
            // '                                        Share <span class="ml-auto fe-share-2"></span>' +
            // '                                    </a>' +
            '                                    <a class="dropdown-item d-flex align-items-center" href="#"  idMessage="'+ idMessage + '" id="deleteMessage" onclick="deleteMessageChat()">' +
            '                                        Delete <span class="ml-auto fe-trash-2"></span>' +
            '                                    </a>' +
            '                                </div>' +
            '                            </div>';

        let tagHtmlDropdownAfter = '<div class="dropdown dropright">' +
            '                                <a class="text-muted opacity-60 ml-3" href="#" data-toggle="dropdown"' +
            '                                   aria-haspopup="true" aria-expanded="false">' +
            '                                    <i class="fe-more-vertical"></i>' +
            '                                </a>' +
            '                                <div class="dropdown-menu">' +
            '                                    <a class="dropdown-item d-flex align-items-center" href="#" idMessage="'+ idMessage + '"  id="editMessage">' +
            '                                        Edit <span class="ml-auto fe-edit-3"></span>' +
            '                                    </a>' +
            // '                                    <a class="dropdown-item d-flex align-items-center" href="#">' +
            // '                                        Share <span class="ml-auto fe-share-2"></span>' +
            // '                                    </a>' +
            '                                </div>' +
            '                            </div>';
        ;
        if (isOwner) {
            divClassMain = 'message message-right';
            divClassMessageRow = 'd-flex align-items-center justify-content-end';
            tagHtmlUserName = '';
            tagHtmlDropdownAfter = '';
        } else {
            tagHtmlDropdownBefore = '';
        }

        let messageContent = message.content;

        let infoAttachment = message.infoAttachment;
        let tagHtmlAttachment = '<div class="form-row py-3">';

        if (infoAttachment != null) {
            for (let key in infoAttachment) {
                if (infoAttachment.hasOwnProperty(key)) {
                    let fileName = infoAttachment[key].toLowerCase();
                    if (fileName.includes('jpg') || fileName.includes('png')
                        || fileName.includes('gif') || fileName.includes('bmp')) {

                        let tagHtmlAttachImage = '<div class="col">' +
                            '                         <img class="getAttachmentForMessage(messages);"' +
                            '                              src="/loadImage/' + key + '" data-action="zoom"' + 'alt="" style="">' +
                            '                     </div>';

                        tagHtmlAttachment += tagHtmlAttachImage;
                    }
                }
            }
            tagHtmlAttachment += '</div><div class="media">';


            for (let key in infoAttachment) {
                if (infoAttachment.hasOwnProperty(key)) {
                    let fileName = infoAttachment[key].toLowerCase();
                    if (!(fileName.includes('jpg') || fileName.includes('png') || fileName.includes('gif') || fileName.includes('bmp'))) {
                        let tagHtmlAttachOther = '<a href="#" class="icon-shape mr-5">' +
                            '                                <i class="fe-paperclip"></i>' +
                            '                           </a>' +
                            '                           <div class="media-body overflow-hidden flex-fill">' +
                            '                                 <a href="/download/' + key + '" class="d-block text-truncate font-medium text-reset">' + fileName + '</a>' +
                            '                           </div>';
                        tagHtmlAttachment += tagHtmlAttachOther;
                    }
                }
            }
            tagHtmlAttachment += '</div>';
        } else {
            tagHtmlAttachment += '</div><div class="media">';
            tagHtmlAttachment += '</div>';
        }

        let date = new Date(message.createdDate);
        let dateString = moment(date).format('YYYY MMM DD HH:mm');

        let tagHtml = '<div class="container-xxl py-6 py-lg-10">' +
            '            <!-- Message -->' +
            '            <div class="' + divClassMain + '">' +
            '                <!-- Avatar -->' +
            '                <a class="avatar avatar-sm mr-4 mr-lg-5" href="#"' +
            '                   data-chat-sidebar-toggle="#chat-1-user-profile">' +
            '                    <img class="avatar-img" src="/loadImage/' + message.idAvatar + '" alt="">' +
            '                </a>' +
            '                <!-- Message: body -->' +
            '                <div class="message-body">' +
            '                    <!-- Message: row -->' +
            '                    <div class="message-row">' +
            '                        <div class="' + divClassMessageRow + '">' +
            tagHtmlDropdownBefore +
            '                            <!-- Message: content -->' +
            '                            <div class="message-content bg-light">' + tagHtmlUserName +
            '                                <div style="white-space: pre-wrap;">' + messageContent + '</div>' +
            tagHtmlAttachment +
            '                                <div class="mt-1">' +
            '                                    <small class="opacity-65" >' + dateString + '</small>' +
            '                                </div>' +
            '                            </div>' +
            '                       </div>' +
            '                   </div>' +
            '               </div>' +
            '           </div>' +
            tagHtmlDropdownAfter +
            '       </div>';

        return tagHtml;
    }


    $('#chat-id-1-form').submit(sendMessage);
});