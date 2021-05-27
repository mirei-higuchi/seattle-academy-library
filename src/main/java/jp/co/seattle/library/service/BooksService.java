package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "SELECT id,title,author,publisher,publish_date,thumbnail_url from books order by title asc ",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }
    
    

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title, author,publisher,publish_date,thumbnail_url,thumbnail_name,reg_date,upd_date,isbn,descripton) VALUES ('"
                + bookInfo.getTitle() + "','"
                + bookInfo.getAuthor() + "','"
                + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getThumbnailName() + "',"
                + "sysdate(),"
                + "sysdate()," + "'"
                + bookInfo.getIsbn() + "','"
                + bookInfo.getDescripton() + "');";
        jdbcTemplate.update(sql);
    }

    public int getNewId() {
        String sql = "select MAX(id) from books";
        int newId = jdbcTemplate.queryForObject(sql, Integer.class);
        return newId;
    }

    /**
     * 削除機能
     * @param bookId
     */
    public void deletingSystem(int bookId) {
        String sql = "DELETE FROM books where id=" + bookId + ";";
        jdbcTemplate.update(sql);
    }

    /**
     * 登録書籍情報を更新する
     * @param bookInfo
     */
    public void updateBooks(BookDetailsInfo bookInfo) {
        String sql = "update books set title='" + bookInfo.getTitle()
                + "',author='" + bookInfo.getAuthor()
                + "',publisher='" + bookInfo.getPublisher()
                + "',publish_date='" + bookInfo.getPublishDate()
                + "',thumbnail_url='" + bookInfo.getThumbnailUrl()
                + "',thumbnail_name='" + bookInfo.getThumbnailName()
                + "',upd_date=sysdate()"
                + ",isbn='" + bookInfo.getIsbn()
                + "',descripton='" + bookInfo.getDescripton()
                + "'where id=" + bookInfo.getBookId() + ";";
        jdbcTemplate.update(sql);
    }
    
    /**
     * サムネイルがない場合の書籍更新
     * @param bookInfo
     */
    public void nullThumbnail(BookDetailsInfo bookInfo) {
        String sql = "update books set title='" + bookInfo.getTitle()
                + "',author='" + bookInfo.getAuthor()
                + "',publisher='" + bookInfo.getPublisher()
                + "',publish_date='" + bookInfo.getPublishDate()
                + "',upd_date=sysdate()"
                + ",isbn='" + bookInfo.getIsbn()
                + "',descripton='" + bookInfo.getDescripton()
                + "'where id=" + bookInfo.getBookId() + ";";
        jdbcTemplate.update(sql);
    }


    /**
     * 検索された文字を含むタイトルの取得
     * @param searchBook
     * @return
     */
    public List<BookInfo> getSearchBooklist(String searchBook) {
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "SELECT id,title,author,publisher,publish_date,thumbnail_url from books where title like '%"
                        + searchBook + "%' ORDER BY title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 貸出可能書籍取得
     * @param searchBook
     * @return
     */
    public List<BookInfo> getLendingBooklist() {
        List<BookInfo> getedLendingBookList = jdbcTemplate.query(
                "SELECT *FROM books LEFT OUTER JOIN lending ON (books.id = lending.id) WHERE lending.id IS NULL",
                new BookInfoRowMapper());
        return getedLendingBookList;
    }

}