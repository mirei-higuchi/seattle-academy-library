package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LendingService {

    final static Logger logger = LoggerFactory.getLogger(LendingService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * lendingテーブルに本が追加されているか確認する
     * @param bookId
     * @return
     */
    public int lentCheck(int bookId) {
        String sql = "SELECT COUNT( * ) FROM lending where id=" + bookId + ";";
        int lentCheck = jdbcTemplate.queryForObject(sql, Integer.class);

        return lentCheck;
    }

    /**
     * データベースに貸し出す本の追加
     * @param bookId
     */
    public void lentSystem(int bookId) {
        String sql = "INSERT INTO lending (id) VALUES (" + bookId + ");";
        jdbcTemplate.update(sql);
    }

    /**
     * データベースの返却する本の削除
     * @param bookId
     */
    public void returnSystem(int bookId) {
        String sql = "DELETE FROM lending where id=" + bookId + ";";
        jdbcTemplate.update(sql);
    }

}
