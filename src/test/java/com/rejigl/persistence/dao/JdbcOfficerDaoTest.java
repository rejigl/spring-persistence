package com.rejigl.persistence.dao;

import com.rejigl.persistence.entities.Officer;
import com.rejigl.persistence.entities.Rank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcOfficerDaoTest {

    @Autowired
    private OfficerDao dao;

    @Test
    void save() {
        Officer officer = new Officer(Rank.ENSIGN, "Pavel", "Chekov");
        officer = dao.save(officer);
        assertNotNull(officer.getId());
    }

    @Test
    void findByIdThatExists() {
        Optional<Officer> officer = dao.findById(1);
        assertTrue(officer.isPresent());
        assertEquals(1, officer.get().getId().intValue());
    }

    @Test
    void findByIdThatDoesNotExists() {
        Optional<Officer> officer = dao.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    void findAll() {
        List<String> dbNames = dao.findAll().stream()
                .map(Officer::getLast)
                .collect(Collectors.toList());
        assertThat(dbNames, containsInAnyOrder("Kirk", "Sisko", "Janeway", "Archer", "Picard"));
    }

    @Test
    void count() {
        assertEquals(5, dao.count());
    }

    @Test
    void delete() {
        IntStream.rangeClosed(1,5)
                .forEach(id -> {
                    Optional<Officer> officer = dao.findById(id);
                    assertTrue(officer.isPresent());
                    dao.delete(officer.get());
                });
        assertEquals(0, dao.count());
    }

    @Test
    void existsById() {
        IntStream.rangeClosed(1,5)
                .forEach(id -> assertTrue(dao.existsById(id)));
    }
}