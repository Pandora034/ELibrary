package com.elibrary.elibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс Author представляет автора статей.
 */
public class Author {
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
        List<Integer> cits = new ArrayList<>();
        for (String cit : citations) {
            cits.add(Integer.parseInt(cit));
        }
        cits.sort(Collections.reverseOrder());
        for (int i = 0; i < cits.size(); i++) {
            if (i + 1 > cits.get(i)) {
                indexH = i;
                break;
            }
        }
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
