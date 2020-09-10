package com.example.chatapplication.ultities;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;

import java.util.Objects;

public class Common {
    public static String convertNumberToString(int number) {
        return new StringBuilder().append(number).toString();
    }

    public boolean isImage(String fileName) {
        if (!Objects.isNull(fileName)) {
            String extension = FilenameUtils.getExtension(fileName);
            boolean validEnum = EnumUtils.isValidEnum(Constant.EXTENSION_IMAGE.class, extension.toUpperCase());
            return validEnum;
        }
        return false;
    }
}
