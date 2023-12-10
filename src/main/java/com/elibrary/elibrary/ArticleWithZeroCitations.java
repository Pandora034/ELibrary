package com.elibrary.elibrary;

/**
 * Представляет статью с нулевым количеством цитирований.
 */
public record ArticleWithZeroCitations(String title, String jointAuthors, String placeOfPublication) {
}
