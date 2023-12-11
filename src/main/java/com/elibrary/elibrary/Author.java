package com.elibrary.elibrary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Класс Author представляет автора статей.
 */
public class Author {
    private static final Logger LOGGER = LogManager.getLogger(Author.class);
    /**
     * Имя автора.
     */
    private String name;
    /**
     * Место работы автора.
     */
    private String placeOfWork;

    /**
     * Индекс Hirsch.
     */
    private int indexH;

    private int numberOfPublications;

    private int numberOfZeroCitations;

    private final List<ArticleWithZeroCitations> articles = new ArrayList<>();
    private final List<String> citations = new ArrayList<>();

    /**
     * Конструктор класса Author. Вычисляет индекс Hirsch для автора на основе списка цитат.
     * Внутри себя вызывает {@link #calculateIndexH()}
     *
     * @param citations Список цитат для вычисления индекса Hirsch.
     */
    public Author(List<String> citations) {
        this.citations.addAll(citations);
        calculateIndexH();
        calculateNumberOfPublication();
        calculateNumberOfZeroCitations();
        LOGGER.debug("Create new author: all- " + numberOfPublications + " zero- " +
                numberOfZeroCitations + " HIndex- " + indexH);
    }

    /**
     * Устанавливает имя автора.
     *
     * @param name Имя автора.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает место работы автора.
     *
     * @param placeOfWork Место работы автора.
     */
    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    /**
     * Добавляет статью с нулевым цитированием для автора.
     *
     * @param title              Название статьи.
     * @param jointAuthors       Список совместных авторов.
     * @param placeOfPublication Место публикации статьи.
     */
    public void addArticle(String title, String jointAuthors, String placeOfPublication) {
        articles.add(new ArticleWithZeroCitations(title, jointAuthors, placeOfPublication));
    }

    /**
     * Вычисляет индекс Hirsch на основе списка цитат.
     */
    private void calculateIndexH() {
        List<Integer> cits = citations.stream()
                .map(Integer::parseInt)
                .sorted(Collections.reverseOrder())
                .toList();

        indexH = IntStream.range(0, cits.size())
                .filter(i -> i + 1 > cits.get(i))
                .findFirst()
                .orElse(cits.size());
    }

    private void calculateNumberOfPublication(){
        numberOfPublications = citations.size();
    }

    private void calculateNumberOfZeroCitations(){
        this.numberOfZeroCitations = (int) citations.stream()
                .filter(number -> Integer.parseInt(number) == 0)
                .count();
    }
    /**
     * Возвращает индекс Hirsch автора.
     *
     * @return Индекс Hirsch автора.
     */
    public int getIndexH() {
        return indexH;
    }

    /**
     * Возвращает имя автора.
     *
     * @return Имя автора.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает место работы автора.
     *
     * @return Место работы автора.
     */
    public String getPlaceOfWork() {
        return placeOfWork;
    }

    /**
     * Возвращает список статей с нулевым цитированием автора.
     *
     * @return Список статей с нулевым цитированием автора.
     */
    public List<ArticleWithZeroCitations> getArticles() {
        return articles;
    }

    public int getNumberOfPublications(){return numberOfPublications;}

    public int getNumberOfZeroCitations() {
        return numberOfZeroCitations;
    }
}
