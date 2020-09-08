package com.example.chatapplication.ultities;

public class Constants {

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    public static final String FORMAT_DATE_UNDERSCORE = "yyyy_MM_dd";
    public static final String COOKIE_NAME = "login_token";
    public static final String SLACK = "/";
    public static final int DEFAULT_SIZE_PAGE = 50;
    public static final int ID_CHAT_ROOM_ALL_USER = 1;
    public static final String KEY_SEARCH = "keySearch";
    public static final String REQUEST_CAPTURE_DESKTOP = "dataImg";
    public static final String PATH_CAPTURE_DESKTOP = "desktop_capture";
    public static final String PATH_ATTACH_MESSAGE = "attach_file_message";
    public static final String FILE_NAME_EXTENSION_PNG = ".png";
    public static final String FORMAT_DATE_SAVE_CAPTURE = "yyyy_MM_dd/HH_mm_ss";
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String PLUS = "+";
    public static final int RULE_ADMIN = 1;
    public static final String TYPE_IMAGE = "image/png";
    public static final String BLANK = "";


    public static class NameAttribute {

        public static final String CURRENT_USER = "currentUser";
        public static final String MESSAGE_DTO_LIST = "messageDtoList";
        public static final String CHAT_ROOM_DTO = "chatRoomDto";
        public static final String LIST_ACCOUNT = "accountDtos";
        public static final String CAPTURE_DTO_LIST = "captureScreenDtos";
    }

    public enum EXTENSION_IMAGE {
        JPEG, PNG, GIF, BMP
    }

    public class Status {
        public static final int INACTIVE = 0;
        public static final int ACTIVE = 1;
    }

    public class Number {
        public static final int NEGATIVE_ONE = -1;
        public static final int ZERO = 0;
        public static final int ONE = 1;
    }
}
