package com.elibrary.elibrary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Класс запускает графический интерфейс для выбора входных файлов и места сохранения выходного файла
 * Так же можно указать имя выходного файла.
 */
public class ELibraryGUI extends Application {
    /**
     * Список выбранных файлов.
     */
    private static List<File> selectedFiles;
    /**
     * Выбранная директория.
     */
    private static File selectedDirectory;
    /**
     * Название выходного файла.
     */
    private static String fileName;
    private static Label selectedFilesLabel;
    private static Label selectedDirectoryLabel;

    /**
     * Текст из формы для имени выходного файла.
     */
    private static TextField inputTextField;

    /**
     * Входная точка в класс. Пишется лог о старте программы {@link ELibLogger#addInfo(String)}
     * @param args - аргументы из командной строки (не используются)
     */
    public static void main(String[] args) {
        ELibLogger.addInfo("Starting program");
        launch(args);

    }

    /**
     * Метод start инициализирует и отображает графический интерфейс приложения.
     * Здесь происходит настройка окна, добавление компонентов (кнопок, текстовых полей и меток),
     * установка обработчиков событий для кнопок и настройка стилей окна.
     * При нажатии на кнопку запускает парсер {@link #startELibrary()}
     *
     * @param primaryStage Сцена (окно), в котором будет отображаться графический интерфейс.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Парсер электронной библиотеки");
        Button selectFilesButton = createSelectFilesButton(primaryStage);
        Button selectDirectoryButton = createSelectDirectoryButton(primaryStage);
        createInputTextField();
        Button startButton = createStartButton();
        createSelectedFilesLabel();
        createSelectedDirectoryLabel();
        VBox layout = createLayout();

        layout.getChildren().addAll(selectFilesButton,
                selectDirectoryButton,
                inputTextField,
                startButton, selectedFilesLabel,
                selectedDirectoryLabel);

        initScene(primaryStage, layout);
        primaryStage.show();
    }

    /**
     * Метод createStartButton используется для создания кнопки "Старт".
     * @return объект Button - кнопка "Старт"
     */
    private Button createStartButton() {
        Button startButton = new Button("Старт");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> startButtonHandler());
        return startButton;
    }

    /**
     * Метод startButtonHandler представляет обработчик события для кнопки "Старт".
     * Извлекает текст из текстового поля inputTextField и инициирует процесс парсинга,
     * если введенные данные прошли валидацию, иначе отображает предупреждение.
     */
    private void startButtonHandler() {
        fileName = inputTextField.getText();
        if (validateInputs()) {
            startELibrary();
        } else showWarning();
    }

    /**
     * Создает кнопку для выбора директории.
     * @param stage ссылка на объект Stage для использования в обработчике события
     * @return объект Button - кнопка "Выбрать директорию"
     */
    private Button createSelectDirectoryButton(Stage stage) {
        Button selectDirectoryButton = new Button("Выбрать директорию");
        selectDirectoryButton.setOnAction(e -> selectDirectory(stage));
        return selectDirectoryButton;
    }

    /**
     * Создает кнопку для выбора файлов.
     * @param stage ссылка на объект Stage для использования в обработчике события
     * @return объект Button - кнопка "Выбрать файлы"
     */
    private Button createSelectFilesButton(Stage stage) {
        Button selectFilesButton = new Button("Выбрать файлы");
        selectFilesButton.setOnAction(e -> selectFiles(stage));
        return selectFilesButton;
    }

    /**
     * Создает текстовое поле для ввода имени выходного файла.
     * Устанавливает текст подсказки и предпочтительное количество столбцов для отображения.
     */
    private void createInputTextField() {
        inputTextField = new TextField();
        inputTextField.setPromptText("Имя выходного файла");
        inputTextField.setPrefColumnCount(20);
    }

    /**
     * Создает вертикальный контейнер для размещения элементов интерфейса.
     * @return объект VBox - вертикальный контейнер layout
     */
    private VBox createLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        return layout;
    }

    /**
     * Создает метку для отображения информации о выбранных файлах.
     */
    private void createSelectedFilesLabel() {
        selectedFilesLabel = new Label("Выбранные файлы:");
    }

    /**
     * Создает метку для отображения информации о выбранной директории.
     */
    private void createSelectedDirectoryLabel() {
        selectedDirectoryLabel = new Label("Выбранная директория:");
    }

    /**
     * Инициализирует сцену и настраивает основное окно приложения.
     * @param primaryStage главное окно приложения
     * @param layout объект VBox, представляющий макет интерфейса
     */
    private void initScene(Stage primaryStage, VBox layout) {
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("C:\\Users\\fyodo\\IdeaProjects\\ELibraryGradle\\out\\artifacts\\ELibrary_main_jar\\styles.css");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(260);  // Устанавливаем минимальную ширину окна
        primaryStage.setMinHeight(350); // Устанавливаем минимальную высоту окна
        primaryStage.setWidth(500);      // Устанавливаем начальную ширину окна
        primaryStage.setHeight(400);     // Устанавливаем начальную высоту окна
        primaryStage.setResizable(true); // Разрешаем изменение размеров окна
    }

    /**
     * Метод stop вызывается при завершении работы графического интерфейса приложения.
     * Использует {@link  ELibLogger#addInfo(String)}
     */
    @Override
    public void stop() {
        ELibLogger.addInfo("Closing program");
    }

    /**
     * Метод selectFiles вызывает диалоговое окно для выбора файлов.
     * После выбора файлов происходит обновление {@link #selectedFiles} с информацией о выбранных файлах.
     *
     * @param stage Сцена (окно), используемая для отображения диалогового окна выбора файлов.
     */
    private void selectFiles(Stage stage) {
        ELibLogger.addInfo("The file explorer is open to select a files");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файлы");
        selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        updateSelectedFilesLabel();
    }

    /**
     * Метод selectDirectory вызывает диалоговое окно для выбора директории.
     * После выбора директории происходит обновление метки с информацией о выбранной директории.
     *
     * @param stage Сцена (окно), используемая для отображения диалогового окна выбора директории.
     */
    private void selectDirectory(Stage stage) {
        ELibLogger.addInfo("The file explorer is open to select a directory");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите директорию");
        selectedDirectory = directoryChooser.showDialog(stage);
        updateSelectedDirectoryLabel();
    }


    /**
     * Метод updateSelectedFilesLabel обновляет метку с информацией о выбранных файлах.
     * Если выбрано хотя бы один файл, метод формирует строку с абсолютными путями выбранных файлов
     * и устанавливает эту строку в метку. Если файлы не выбраны, метка устанавливается в значение "Выбранные файлы: нет".
     */
    private void updateSelectedFilesLabel() {
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            StringBuilder filesText = new StringBuilder();
            for (File file : selectedFiles) {
                ELibLogger.addInfo("A new file has been selected: " + file.getAbsolutePath());
                filesText.append(file.getAbsolutePath()).append(System.getProperty("line.separator"));
            }
            filesText.insert(0, "Выбранные файлы:" + System.getProperty("line.separator"));
            selectedFilesLabel.setText(filesText.toString());
        } else {
            selectedFilesLabel.setText("Выбранные файлы: нет");
        }
    }

    /**
     * Метод updateSelectedDirectoryLabel обновляет метку с информацией о выбранной директории.
     * Если директория выбрана, метод устанавливает метку с абсолютным путем выбранной директории.
     * В случае, если директория не выбрана, метка устанавливается в значение "Выбранная директория:".
     */
    private void updateSelectedDirectoryLabel() {
        if (selectedDirectory != null) {
            selectedDirectoryLabel.setText("Выбранная директория: " + selectedDirectory.getAbsolutePath());
            ELibLogger.addInfo("A new directory has been selected: " + selectedDirectory.getAbsolutePath());
        } else {
            selectedDirectoryLabel.setText("Выбранная директория:");
        }
    }

    /**
     * Метод clearAllInputs очищает все входные поля и метки выбранных файлов и директории.
     * Также он обнуляет переменные selectedFiles и selectedDirectory.
     */
    private void clearAllInputs() {
        inputTextField.clear();
        selectedFilesLabel.setText("Выбранные файлы: нет");
        selectedDirectoryLabel.setText("Выбранная директория:");
        selectedFiles = null;
        selectedDirectory = null;
    }


    /**
     * Метод validateInputs проверяет валидность введенных данных.
     * Возвращает true, если выбран хотя бы один файл, выбрана директория и введено имя файла.
     * В противном случае возвращает false.
     *
     * @return Результат валидации- true, если данные валидны, иначе false.
     */
    private boolean validateInputs() {
        if (selectedFiles == null || selectedFiles.isEmpty()) return false;
        else if (selectedDirectory == null) return false;
        else return fileName != null && !fileName.isEmpty();
    }

    /**
     * Метод showWarning отображает предупреждение в виде диалогового окна.
     * Предупреждение информирует пользователя о необходимости выбрать файлы, директорию и ввести имя файла.
     */
    private void showWarning() {
        ELibLogger.addWarn("Incorrect or empty input");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText("Пожалуйста, выберите файлы, директорию и введите имя файла!");

        alert.showAndWait();
    }

    /**
     * Метод showError отображает сообщение об ошибке в виде диалогового окна.
     *
     * @param message Сообщение об ошибке, которое следует отобразить в диалоговом окне.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Метод showInformation отображает информационное сообщение в виде диалогового окна.
     * Сообщение включает информацию об успешной записи файла в указанную директорию.
     */
    private void showInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Файл успешно записан в директорию:\n" + selectedDirectory.getAbsolutePath() +
                "\nс именем файла: " + fileName + ".docx");

        alert.showAndWait();
    }

    /**
     * Метод startELibrary отвечает за запуск процесса парсинга данных электронной библиотеки и запись результатов в файл.
     * Для этого метод создает экземпляр парсера электронной библиотеки {@link ELibraryParser},
     * добавляет выбранные файлы в качестве входных,
     * выполняет парсинг, создает объект для записи файлов, устанавливает имя выходного файла, записывает результаты
     * и отображает информационное сообщение об успешной записи, затем очищает входные данные.
     */
    private void startELibrary() {
        ELibLogger.addInfo("Starting to parse");

        ELibraryParser parser = new ELibraryParser();

        addPaths(parser);
        parser.parse();
        writeToFile(parser);
    }

    /**
     * Добавляет пути до входных файлов в класс парсера
     *
     * @param parser - объект класса {@link ELibraryParser}
     */
    private void addPaths(ELibraryParser parser){
        for (File file : selectedFiles) {
            parser.addInputPath(file.getPath());
        }
    }

    /**
     * Запускает процесс записи информации из парсера в файл.
     * При возникновении ошибки записи выскакивает предупреждение.
     *
     * @param parser - объект класса {@link ELibraryParser}
     */
    private void writeToFile(ELibraryParser parser){
        ELibLogger.addInfo("The beginning of the report recording procedure");
        try {
            ELibraryFileWriter eLibraryFileWriter = new ELibraryFileWriter(parser);
            eLibraryFileWriter.setOutputFileName(fileName);
            eLibraryFileWriter.write(selectedDirectory.getPath());
            showInformation();
            clearAllInputs();

        } catch (IOException e) {
            ELibLogger.addError("The file could not be written", e);
            showError("Не удалось записать файл");
        }
    }
}


