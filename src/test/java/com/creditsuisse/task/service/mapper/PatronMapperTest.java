package com.creditsuisse.task.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatronMapperTest {

    private PatronMapper patronMapper;

    @BeforeEach
    public void setUp() {
        patronMapper = new PatronMapperImpl();
    }
}
