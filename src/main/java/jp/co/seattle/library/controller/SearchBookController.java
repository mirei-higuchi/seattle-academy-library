package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class SearchBookController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService booksService;

    /**
     * 書籍タイトル検索機能
     * @param locale
     * @param searchBook
     * @param model
     * @return
     */
    @RequestMapping(value = "/searchBook", method = RequestMethod.POST)
    public String serchBook(Locale locale,
            @RequestParam("search") String searchBook,
            Model model) {

        if (CollectionUtils.isEmpty(booksService.getSearchBooklist(searchBook))) {
            model.addAttribute("errorMessage", "該当する書籍名がありません");
            return "home";
        }

        model.addAttribute("bookList", booksService.getSearchBooklist(searchBook));

        return "home";
    }

}
