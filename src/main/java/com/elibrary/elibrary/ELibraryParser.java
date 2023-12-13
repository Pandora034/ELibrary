package com.elibrary.elibrary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Класс ELibraryParser предназначен для парсинга информации из HTML-файлов электронной библиотеки ELibrary.
 */
public class ELibraryParser {
    private static final Logger LOGGER = LogManager.getLogger(ELibraryParser.class);
    /**
     * Массив для хранения путей до входных файлов
     */
    private final List<String> arrayInputPath = new ArrayList<>();
    /**
     * Ссылка на объект класса {@link Author}
     */
    private Author author;

    private Elements trElements = null;

    /**
     * Массив для хранения объектов класса {@link Author}
     */
    private final List<Author> arrayAuthors = new ArrayList<>();
    private Document doc;

    /**
     * Метод добавляет в {@link #arrayInputPath} путь к файлу с HTML для последующего парсинга.
     * Записывает debug в лог c путём до файла
     *
     * @param inputPath Путь к файлу с HTML.
     */
    public void addInputPath(String inputPath) {
        arrayInputPath.add(inputPath);
        LOGGER.debug("Added the path to the input file: " + inputPath);
    }

    /**
     * Метод добавляет несколько путей к файлам с HTML для последующего парсинга.
     * Использует {@link #addInputPath(String)}
     *
     * @param inputPaths Список путей к файлам с HTML.
     */
    @Deprecated
    public void addInputPath(String... inputPaths) {
        for (String path : inputPaths) {
            addInputPath(path);
        }
    }

    /**
     * Парсит информацию из нескольких HTML-файлов электронной библиотеки.
     * Если пути к файлам повторяются, метод парсит каждый файл только один раз.
     * Использует {@link #parseELibrary(String)}.
     */
    public void parse() {
        Set<String> setInputPath = new HashSet<>(arrayInputPath);
        arrayInputPath.clear();
        arrayInputPath.addAll(setInputPath);
        for (String path : arrayInputPath) {
            parseELibrary(path);
        }
    }

    /**
     * Инициализирует новый HTML-файл для дальнейшего парсинга.
     * При ошибке открытия файла записывает error в лог.
     *
     * @param fileInputPath Путь к HTML-файлу для инициализации
     */
    private void initNewFile(String fileInputPath) {
        try {
            doc = Jsoup.parse(new File(fileInputPath), "UTF-8");
        } catch (IOException e) {
            LOGGER.error("Could not read the file");
        }
    }

    /**
     * Парсит информацию из HTML-файла электронной библиотеки.
     * Создаёт внутри себя объекты класса {@link Author},
     * Записывает в {@link #arrayAuthors} авторов.
     * Использует {@link #initNewFile(String)},
     * {@link #initTrElements()},
     * {@link #parseCitations()}
     * {@link #parseAuthorName()},
     * {@link #parsePlaceOfWork()},
     * {@link #addArticles()}
     * Пишет лог о начале парсинга файла
     *
     * @param inputPath Путь к файлу с HTML для парсинга
     */
    public void parseELibrary(String inputPath) {
        LOGGER.info("Starting to parse the file: " + inputPath);
        initNewFile(inputPath);
        initTrElements();
        author = new Author(parseCitations());
        author.setName(parseAuthorName());
        author.setPlaceOfWork(parsePlaceOfWork());
        addArticles();
        arrayAuthors.add(author);
    }


    /**
     * Инициализирует элементы таблицы для последующего парсинга.
     * Если не удалось распарсить таблицу, будет создан пустой набор элементов.
     */
    private void initTrElements() {
        try {
            trElements = doc.select("tr");
            for (Element tr : trElements){
                Elements tables = tr.select("table");
                tables.remove();
            }
            trElements = doc.select("tr");
        } catch (NullPointerException e) {
            LOGGER.error("Failed to parse the table");
            trElements = new Elements();
        }
    }

    /**
     * Парсит названия статей из элементов таблицы.
     *
     * @return Список названий статей
     */
    private List<String> parseNameOfArticles() {
        List<String> nameOfArticles = new ArrayList<>();
        for (Element article : trElements.select("b")) {
            if (article.text().contains("table"))
                nameOfArticles.add(article.select("span").text());
            else nameOfArticles.add(article.text());
        }
        return nameOfArticles;
    }

    /**
     * Парсит место публикаций статей из элементов таблицы.
     *
     * @return Список мест публикаций
     */
    private List<String> parsePlacesOfPublication() {
        List<String> placesOfPublication = new ArrayList<>();
        Element[] tdBufferElements;
        tdBufferElements = trElements.select("td").toArray(new Element[0]);
        for (Element element : tdBufferElements) {
            String line = element.text();

            if (!isDigit(line) && !line.equals("№") && !line.equals("ПубликацияЦитирований")) {
                int index = line.indexOf(". ");
                if (index != -1) {
                    placesOfPublication.add(line.substring(index + 2));
                } else placesOfPublication.add(line);
            }
        }
        return placesOfPublication;
    }

    /**
     * Метод для извлечения ФИО автора из HTML-документа.
     *
     * @return ФИО автора
     */
    private String parseAuthorName() {
        Element element;
        try {
            element = doc.select("span").first();
            return Objects.requireNonNull(element).text();
        } catch (NullPointerException e) {
            LOGGER.error("Failed to parse the author name");
            return "Не удалось найти имя автора";
        }
    }

    /**
     * Метод для извлечения места работы из HTML-документа.
     *
     * @return Место работы автора
     */
    private String parsePlaceOfWork() {
        Element element;
        try {
            element = doc.select("i").first();
            return Objects.requireNonNull(element).text();
        } catch (NullPointerException e) {
            LOGGER.error("Failed to parse the place of work");
            return "Место работы не найдено";
        }
    }

    /**
     * Метод для парсинга совместных авторов.
     *
     * @return Список совместных авторов
     */
    private List<String> parseJointAuthors() {
        List<String> jointAuthors = new ArrayList<>();
        for (Element element : trElements.select("i")) {
            jointAuthors.add(element.text());
        }
        return jointAuthors;
    }

    /**
     * Метод для парсинга количества цитирований.
     *
     * @return Список цитат
     */
    private List<String> parseCitations() {
        List<String> citations = new ArrayList<>();
        for (Element cit : trElements.select("td")) {
            String bufferCit = cit.select("td:nth-child(3)").text();
            if (!bufferCit.isBlank() && !bufferCit.equals(" ")) {
                citations.add(bufferCit);
            }
        }
        return citations;
    }

    /**
     * Метод для добавления статей в класс
     *{@link Author#addArticle(String, String, String)}.
     */
    private void addArticles() {
        List<String> nameOfArticles = parseNameOfArticles();
        List<String> jointAuthors = parseJointAuthors();
        List<String> placeOfPublication = parsePlacesOfPublication();
        List<String> citations = parseCitations();

        int sizeOfFirstList = nameOfArticles.size();
        if (jointAuthors.size() == sizeOfFirstList &&
                placeOfPublication.size() == sizeOfFirstList &&
                citations.size() == sizeOfFirstList) {
            for (int i = 0; i < nameOfArticles.size(); i++) {
                if (citations.get(i).equals("0"))
                    author.addArticle(nameOfArticles.get(i), jointAuthors.get(i), placeOfPublication.get(i));
            }
        }
        else LOGGER.error("Failed to parse articles for author");
    }

    /**
     * Метод для получения массива авторов.
     *
     * @return Массив авторов
     */
    public List<Author> getArrayAuthors() {
        return arrayAuthors;
    }

    /**
     * Метод для определения, является ли строка числом.
     *
     * @param line Проверяемая строка
     * @return true, если строка состоит только из цифр, в противном случае - false
     */
    private boolean isDigit(String line) {
        return line.chars().allMatch(Character::isDigit);
    }
}
