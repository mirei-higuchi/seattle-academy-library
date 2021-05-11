package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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
public class BulkRegistController {
    final static Logger logger = LoggerFactory.getLogger(BulkRegistController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 一括追加画面に偏移
     * @param model
     * @return
     */
    @RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "bulkRegist";
    }

    /**
     * CSVファイルを読み込む
     * @param locale
     * @param uploadFile
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/bulkRegistSystem", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String bulkRegistSystem(Locale locale,
            @RequestParam("csvFile") MultipartFile uploadFile, Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        try (InputStream stream = uploadFile.getInputStream();
                Reader reader = new InputStreamReader(stream);
                BufferedReader buf = new BufferedReader(reader);) {

            ArrayList<BookDetailsInfo> bookList = new ArrayList<BookDetailsInfo>();

            ArrayList<String> errorMsg = new ArrayList<String>();
            int count = 1;

            String line;

            while ((line = buf.readLine()) != null) {
                String[] data = new String[6];
                data = line.split(",", -1);

                BookDetailsInfo bookInfo = new BookDetailsInfo();

                bookInfo.setDescripton(data[5]);
                bookInfo.setIsbn(data[4]);

                boolean check = false;

                if (StringUtils.isEmpty(data[0]) ||
                        StringUtils.isEmpty(data[1]) ||
                        StringUtils.isEmpty(data[2]) ||
                        StringUtils.isEmpty(data[3])) {
                    check = true;
                }
                bookInfo.setTitle(data[0]);
                bookInfo.setAuthor(data[1]);
                bookInfo.setPublisher(data[2]);
                bookInfo.setPublishDate(data[3]);

                if (check) {
                    errorMsg.add(count + "行目の書籍情報登録でバリデーションエラー");
                }
                bookList.add(bookInfo);

                count++;

            }

            if (!CollectionUtils.isEmpty(errorMsg)) {
                model.addAttribute("errorMessage", errorMsg);
                return "bulkRegist";
            }

            for (BookDetailsInfo list : bookList) {
                booksService.registBook(list);
            }

            model.addAttribute("resultMessage", "登録完了");
            return "bulkRegist";

        } catch (Exception e) {
            return "bulkRegist";

        }
    }
}
