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

    public int lentCheck(int bookId) {
        String sql = "SELECT COUNT( * ) FROM lending where id=" + bookId + ";";
        int lentCheck = jdbcTemplate.queryForObject(sql, Integer.class);

        return lentCheck;
    }

    public void lentSystem(int bookId) {
        String sql = "INSERT INTO lending (id) VALUES (" + bookId + ");";
        jdbcTemplate.update(sql);
    }

    public void returnSystem(int bookId) {
        String sql = "DELETE FROM lending where id=" + bookId + ";";
        jdbcTemplate.update(sql);
    }

}
