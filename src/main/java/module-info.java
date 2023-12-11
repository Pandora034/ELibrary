/**
 * Модуль для работы с html-файлами российской электронной библиотеки elibrary.ru.
 */
module com.elibrary.elibrary {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.poi.ooxml;
    requires commons.math3;

    requires org.apache.logging.log4j;

    requires org.jsoup;

    requires org.controlsfx.controls;

    exports com.elibrary.elibrary to javafx.graphics;
    opens com.elibrary.elibrary to javafx.fxml;
}