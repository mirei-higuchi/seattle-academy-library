package jp.co.seattle.library.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class EditBooksController {
    final static Logger logger = LoggerFactory.getLogger(EditBooksController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/btn_editBook", method = RequestMethod.POST)
    public String login(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {

        BookDetailsInfo newIdInfo = booksService.getBookInfo(bookId);
        model.addAttribute("bookDetailsInfo", newIdInfo);

        return "edit";
    }

    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateBooks(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("isbn") String isbn,
            @RequestParam("descripton") String descripton,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("bookId") Integer bookId,

            Model model) {
        logger.info("Welcome editBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);
        bookInfo.setDescripton(descripton);
        bookInfo.setBookId(bookId);

        boolean isIsbn = isbn.matches("(^\\d{10,13}$)?");
        boolean check = false;

        if (!isIsbn) {
            model.addAttribute("error1", "半角数字10文字以上13文字以内");
            check = true;
        }

        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(publishDate);
        } catch (ParseException p) {
            model.addAttribute("error2", "年月日を入力してください");
            check = true;
        }

        if (check) {
            return "edit";
        }
        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "editBook";
            }
        }
        booksService.updateBooks(bookInfo);
        model.addAttribute("resultMessage", "編集完了");

        booksService.getBookInfo(bookId);
        {
            BookDetailsInfo newIdInfo = booksService.getBookInfo(bookId);
            model.addAttribute("bookDetailsInfo", newIdInfo);
            return "details";

        }
    }

}
