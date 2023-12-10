package com.elibrary.elibrary;

/**
 * Класс ELibraryLauncher предназначен для запуска приложения электронной библиотеки.
 * Он содержит метод main, который является входной точкой приложения.
 */
public class ELibraryLauncher {

    /**
     * Метод main представляет точку входа для приложения. Использует {@link ELibraryGUI#main(String[])}
     *
     * @param args Массив строковых аргументов, передаваемых при запуске приложения.
     */
    public static void main(String[] args) {
        ELibraryGUI.main(args);
    }
}
