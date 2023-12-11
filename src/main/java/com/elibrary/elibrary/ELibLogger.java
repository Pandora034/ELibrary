package com.elibrary.elibrary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @deprecated Теперь логгер встроен отдельно в каждый класс модуля.
 * <p>
 * ELibLogger представляет собой класс, отвечающий за логирование различных уровней сообщений.
 */
@Deprecated
public class ELibLogger {
    private static final Logger logger = LogManager.getLogger(ELibLogger.class);

    /**
     * Добавляет информационное сообщение в лог.
     *
     * @param message Сообщение для логирования.
     */
    public static void addInfo(String message) {
        logger.info(message);
    }

    /**
     * Добавляет сообщение об ошибке в лог.
     *
     * @param message   Сообщение об ошибке.
     * @param throwable Исключение, связанное с ошибкой.
     */
    public static void addError(String message, Throwable throwable) {
        logger.error(message + " " + throwable.getMessage());
    }

    /**
     * Добавляет сообщение об ошибке в лог.
     *
     * @param message Сообщение об ошибке.
     */
    public static void addError(String message) {
        logger.error(message);
    }

    /**
     * Добавляет отладочное сообщение в лог.
     *
     * @param message Сообщение для отладки.
     */
    public static void addDebug(String message) {
        logger.debug(message);
    }

    public static void addWarn(String message){logger.warn(message);}
}