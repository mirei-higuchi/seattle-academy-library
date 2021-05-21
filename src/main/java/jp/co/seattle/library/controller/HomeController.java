package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HomeController {
    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * Homeボタンからホーム画面に戻るページ
     * @param model
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String transitionHome(Model model) {
        model.addAttribute("bookList", booksService.getBookList());
        return "home";
    }

    /**
     * 貸出可能書籍リスト表示
     * @param model
     * @return
     */
    @RequestMapping(value = "/lendingBook", method = RequestMethod.GET)
    public String lendingBooklist(Model model) {

        if (CollectionUtils.isEmpty(booksService.getLendingBooklist())) {
            model.addAttribute("noLendingMessage", "全ての書籍が貸出中です");
            return "home";
        }
        model.addAttribute("bookList", booksService.getLendingBooklist());
        return "home";
    }

}
