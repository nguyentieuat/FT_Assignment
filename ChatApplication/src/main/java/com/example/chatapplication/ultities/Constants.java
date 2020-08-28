package com.example.chatapplication.ultities;

public class Constants {
    public static final String REGEX_EMAIL = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";

    public static final String JWT_HEADER = "Authorization";
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    public static final String FORMAT_DATE_UNDERSCORE = "yyyy_MM_dd";
    public static final String BLANK = "";
    public static final String CHARACTER_PERCENT = "%";
    public static final String ATTACHMENT_FILENAME = "attachment; filename=";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CONTENT_DISPOSITION = "Content-disposition";
    public static final String CONTENT_TYPE = "application/octet-stream";
    public static final String COOKIE_NAME = "login_token";
    public static final String SLACK = "/";
    public static final int DEFAULT_SIZE_PAGE = 50;

    public enum EXTENSION_IMAGE {
        JPEG, PNG, GIF, BMP
    }

    public class Status {
        public static final int INACTIVE = 0;
        public static final int ACTIVE = 1;
    }

    public class CommonValue{
        public static final int CATEGORY_FILE = 1;
        public static final int CATEGORY_FILE_DOCUMENT = 1;
        public static final int CATEGORY_FILE_PICTURE = 2;
        public static final int CATEGORY_FILE_APPLICATION = 3;
        public static final int CATEGORY_FILE_MUSIC = 4;
        public static final int CATEGORY_FILE_OTHER = 5;


        public static final int RULE_ACCESS_FILE = 3;
        public static final int RULE_ACCESS_FILE_READ = 1;
        public static final int RULE_ACCESS_FILE_WRITE = 2;

    }

    public class Number{
        public static final int NEGATIVE_ONE = -1;
        public static final int ZERO = 0;
        public static final int ONE = 1;
    }

    public class Attribute {
        public static final String FILE_NAME = "fileName";
        public static final String CATEGORY = "category";
        public static final String CREATED_DATE = "createdDate";
        public static final String EMPLOYEE = "employee";
        public static final String STATUS = "status";
        public static final String ACCOUNT = "account";
        public static final String TYPE_ID = "typeId";
        public static final String ACCOUNT_EMPLOYEE = "account";
        public static final String EVERYONE = "everyOne";
        public static final String FILE_ID = "fileId";
    }

}
