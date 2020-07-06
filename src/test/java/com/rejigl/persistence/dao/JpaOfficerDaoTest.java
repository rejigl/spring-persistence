package com.rejigl.persistence.dao;

import com.rejigl.persistence.entities.Officer;
import com.rejigl.persistence.entities.Rank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class JpaOfficerDaoTest {

    @Autowired @Qualifier("jpaOfficerDao")
    private OfficerDao dao;

    @Autowired
    private JdbcTemplate jdbctemplate;

    private RowMapper<Integer> idMapper = (rs, rowNum) -> rs.getInt("id");

    @Test
    void save() {
        Officer officer = new Officer(Rank.ADMIRAL, "Nyota", "Uhuru");
        officer = dao.save(officer);
        assertNotNull(officer.getId());
    }

    @Test
    void findByIdThatExists() {
        jdbctemplate.query("SELECT id from officers", idMapper)
                .forEach(id -> {
                    Optional<Officer> officer = dao.findById(id);
                    assertTrue(officer.isPresent());
                    assertEquals(id, officer.get().getId());
                });
    }

    @Test
    public void findOneThatDoesNotExists(){
        Optional<Officer> officer = dao.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    void findAll() {
        List<String> lastNames = dao.findAll().stream()
                .map(Officer::getLast)
                .collect(Collectors.toList());
        assertThat(lastNames, containsInAnyOrder("Archer", "Janeway", "Kirk", "Picard", "Sisko"));
    }

    @Test
    void count() {
        assertEquals(5,dao.count());
    }

    @Test
    void delete() {
        jdbctemplate.query("select id from officers", idMapper)
                .forEach(id -> {
                    Optional<Officer> officer = dao.findById(id);
                    assertTrue(officer.isPresent());
                    dao.delete(officer.get());
                });
        assertEquals(0, dao.count());
    }

    @Test
    void existsById() {
        jdbctemplate.query("select id from officers", idMapper)
                .forEach(id -> assertTrue(dao.existsById(id)));
    }
}