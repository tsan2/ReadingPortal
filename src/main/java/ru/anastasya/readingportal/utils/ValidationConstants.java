package ru.anastasya.readingportal.utils;

public final class ValidationConstants {

    private ValidationConstants(){

    }

    public static final int USER_NICKNAME_MIN_SIZE = 3;
    public static final int USER_NICKNAME_MAX_SIZE = 30;
    public static final int USER_PASSWORD_MIN_SIZE = 4;

    public static final int CONTENT_MAX_SIZE = 2_000_000;

    public static final int TITLE_MIN_SIZE = 2;
    public static final int TITLE_MAX_SIZE = 250;

    public static final int GENRE_NAME_MIN_SIZE = 2;
    public static final int GENRE_NAME_MAX_SIZE = 100;

}
