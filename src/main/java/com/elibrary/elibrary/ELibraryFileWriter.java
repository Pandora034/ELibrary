package com.elibrary.elibrary;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Класс ELibraryFileWriter управляет записью проанализированных данных электронной библиотеки в документ Word.
 * Он предоставляет функциональность для создания файла .docx, содержащего информацию об авторах, их статьях,
 * и связанных с ними деталях.
 */
public class ELibraryFileWriter {
    private final ELibraryParser libraryParser;
    private String outputFileName;
    private final XWPFDocument document = new XWPFDocument();
    private XWPFParagraph paragraph;
    private XWPFRun run;


    /**
     * Создает ELibraryFileWriter со ссылкой на ELibraryParser.
     *
     * @param libraryParser Экземпляр ELibraryParser, который будет использоваться для записи данных в файл.
     */
    public ELibraryFileWriter(ELibraryParser libraryParser) {
        this.libraryParser = libraryParser;
    }

    /**
     * Записывает проанализированные данные в файл в указанном выходном каталоге.
     *
     * @param outputPath - Путь, по которому будет записан выходной файл.
     * @throws IOException - если записываемый файл пустой или при ошибке открытия (создания) файла
     */
    public void write(String outputPath) throws IOException {
        if (outputFileName == null || outputFileName.isEmpty() || outputFileName.startsWith(" ")) {
            outputFileName = "Output";
            ELibLogger.addDebug("Output file name out of format. (" + outputFileName + ") - auto create)");
        }
        writeAllInformation();
        writeToFile(outputPath);
    }


    /**
     * Задает имя выходного файла.
     *
     * @param fileName Имя выходного файла.
     */
    public void setOutputFileName(String fileName) {
        this.outputFileName = fileName;
    }

    /**
     * Создает новый абзац и инициализирует новый текстовый блок с настройками шрифта по умолчанию.
     * Шрифт по умолчанию установлен как "Times New Roman", размер шрифта - 14.
     */
    private void createNewParagraph() {
        paragraph = document.createParagraph();
        run = paragraph.createRun();
        run.setFontFamily("TimesNewRoman");
        run.setFontSize(14);
    }

    /**
     * Записывает имя автора в документ, выделяя его жирным шрифтом и устанавливая центрирование.
     *
     * @param author Автор, чье имя будет записано в документ.
     */
    private void writeAuthorName(Author author) {
        createNewParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setBold(true);
        run.setFontSize(18);
        run.setText(author.getName());
    }

    /**
     * Записывает заголовок статьи в документ, добавляя нижнюю границу, делая текст жирным и устанавливая размер шрифта.
     *
     * @param article Статья, заголовок которой будет записан в документ.
     */
    private void writeArticleTitle(ArticleWithZeroCitations article) {
        createNewParagraph();
        paragraph.setBorderBottom(Borders.APPLES);
        run.setBold(true);
        run.setFontSize(14);
        run.setText(article.title());
    }

    /**
     * Записывает совместных авторов статьи в документ, устанавливая размер шрифта для текста.
     *
     * @param article Статья, совместные авторы которой будут записаны в документ.
     */
    private void writeJointsAuthors(ArticleWithZeroCitations article) {
        createNewParagraph();
        run.setFontSize(12);
        run.setText(article.jointAuthors());
    }


    /**
     * Записывает место публикации статьи в документ, устанавливая размер шрифта для текста.
     *
     * @param article Статья, место публикации которой будет записано в документ.
     */
    private void writePlaceOfPublication(ArticleWithZeroCitations article) {
        createNewParagraph();
        run.setFontSize(12);
        run.setText(article.placeOfPublication());
    }

    /**
     * Записывает место работы автора в документ, устанавливая выравнивание по центру и размер шрифта для текста.
     * Также добавляет разрыв после записи.
     *
     * @param author Автор, место работы которого будет записано в документ.
     */
    private void writePlaceOfWork(Author author) {
        createNewParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(12);
        run.setText(author.getPlaceOfWork());
        run.addBreak();
    }

    /**
     * Записывает индекс Хирша автора в документ, делая текст жирным.
     *
     * @param author Автор, индекс Хирша которого будет записан в документ.
     */
    private void writeIndexH(Author author) {
        createNewParagraph();
        run.setBold(true);
        run.setText("Индекс Хирша: " + author.getIndexH());
    }

    private void writeNumberOfPublications(Author author) {
        createNewParagraph();
        run.setBold(true);
        run.setText("Количество статей: " + author.getNumberOfPublications());
    }

    /**
     * Записывает количество статей с нулевым цитированием автора в документ, делая текст жирным.
     *
     * @param author Автор, для которого будет записано количество статей с нулевым цитированием.
     */
    private void writeNumberOfZeroCitations(Author author) {
        createNewParagraph();
        run.setBold(true);
        run.setText("Количество статей с нулевым цитированием: " + author.getNumberOfZeroCitations());
    }

    /**
     * Записывает информацию об авторах и их статьях в документ.
     * Использует {@link #writeAuthorInformation(Author)}
     */
    private void writeAllInformation() {
        for (Author author : libraryParser.getArrayAuthors()) {
            writeAuthorInformation(author);
            writeArticlesInformation(author);
            run.addBreak(BreakType.PAGE);
        }
    }

    /**
     * Записывает информацию о конкретном авторе в документ.
     *
     * @param author Автор, информацию о котором необходимо записать.
     */
    private void writeAuthorInformation(Author author) {
        writeAuthorName(author);
        writePlaceOfWork(author);
        writeIndexH(author);
        writeNumberOfPublications(author);
        writeNumberOfZeroCitations(author);
        run.addBreak(BreakType.TEXT_WRAPPING);
    }

    /**
     * Записывает информацию о статьях автора в документ.
     *
     * @param author Автор, статьи которого необходимо записать.
     */
    private void writeArticlesInformation(Author author) {
        for (ArticleWithZeroCitations article : author.getArticles()) {
            writeArticleTitle(article);
            writeJointsAuthors(article);
            writePlaceOfPublication(article);
            run.addBreak(BreakType.TEXT_WRAPPING);
        }
    }

    /**
     * Записывает содержимое документа в файл.
     *
     * @param outputPath Путь, по которому необходимо сохранить файл.
     * @throws IOException Если возникают проблемы при записи в файл.
     */
    private void writeToFile(String outputPath) throws  IOException {
        try (OutputStream fileOut = new FileOutputStream(outputPath + "\\" + outputFileName + ".docx")) {
            if (!documentIsEmpty()) {
                document.write(fileOut);
                ELibLogger.addInfo("The file was successfully written: " + outputPath + "\\" + outputFileName + ".docx");
            } else {
                ELibLogger.addError("The file could not be written. There is a problem with the input files.");
                throw new IOException("File is empty");
            }
        }
    }

    /**
     * Проверяет, пуст ли документ.
     *
     * @return true, если документ пуст, в противном случае - false.
     */
    private boolean documentIsEmpty() {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                if (run.getText(run.getTextPosition()) == null && run.getText(run.getTextPosition()).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Выводит информацию об авторах и их статьях в консоль.
     * Cлужит для отладки
     */
    public void printAuthor() {
        List<Author> authors = libraryParser.getArrayAuthors();
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                System.out.println(author.getName());
                System.out.println(author.getPlaceOfWork());
                System.out.println("Индекс Хирша: " + author.getIndexH());
                for (ArticleWithZeroCitations article : author.getArticles()) {
                    System.out.println(article.title());
                    System.out.println(article.jointAuthors());
                    System.out.println(article.placeOfPublication());
                }
                System.out.println("--------------------------------");
            }
        } else System.out.println("Не было добавлено ни одного автора");
    }
}