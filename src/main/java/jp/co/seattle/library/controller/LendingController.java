package jp.co.seattle.library.controller;

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

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.LendingService;

/**
 * @author user
 *
 */
@Controller //APIの入り口
public class LendingController {
    final static Logger logger = LoggerFactory.getLogger(LendingController.class);
    @Autowired
    private BooksService booksService;
    @Autowired
    private LendingService lendingService;

    /**
     * 貸し出し機能
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/btn_rentBook", method = RequestMethod.POST)
    public String rentBook(Locale locale,
            @RequestParam("bookId") Integer bookId, Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        if (lendingService.lentCheck(bookId) == 0) {
            lendingService.lentSystem(bookId);
        }

        model.addAttribute("lending", "貸出中");

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        return "details";
    }

    /**
     * 返却機能
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/btn_returnBook", method = RequestMethod.POST)
    public String returnBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        if (lendingService.lentCheck(bookId) == 1) {
            lendingService.returnSystem(bookId);
        }
        model.addAttribute("lending", "貸出可");

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        return "details";
    }

}
