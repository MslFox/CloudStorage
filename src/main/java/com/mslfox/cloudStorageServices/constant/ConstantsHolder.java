package com.mslfox.cloudStorageServices.constant;

public class ConstantsHolder {
    public static final String TOKEN_HEADER_NAME = "auth-token";
    public static final String USER_HEADER_NAME = "login";
    public static final String ERROR_WRONG_PASSWORD = "Wrong password";
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_GET_FILE_LIST = "Unable to get file list";
    public static final String ERROR_RENAME_FILE = "Unable to rename file";
    public static final String ERROR_UPLOAD_FILE = "Unable to upload file";
    public static final String ERROR_DELETE_FILE = "Unable to delete file";
    public static final String ERROR_GET_FILE = "Unable to get file";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String ERROR_JSON_PROCESSING = "Unable to create an instance for deserialization from a string value";
    public static final String ERROR_VALIDATION = "Unable to validate input data. ";
    public static final String ERROR_SECURITY_CONTEXT_INVALID_USERNAME = "Security context authentication: invalid username";
    public static final String SUCCESS_RENAME = "Success rename file";
    public static final String SUCCESS_DELETE = "Success delete file";
    public static final String SUCCESS_UPLOAD =
            """
 Мы рады сообщить Вам, что Ваш файл был успешно загружен на наш сервер.
Вы можете быть уверены, что мы сохраняем все Ваши файлы в безопасности и
доступности. Если у Вас возникнут какие-либо вопросы или замечания, пожалуйста,
не стесняйтесь связаться с нашей службой поддержки!
Мы всегда готовы помочь!
            
С уважением, MSL (Max Storage Level)  Group .
    """;
}