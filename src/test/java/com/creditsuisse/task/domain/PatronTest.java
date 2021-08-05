package com.creditsuisse.task.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.creditsuisse.task.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatronTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patron.class);
        Patron patron1 = new Patron();
        patron1.setId(1L);
        Patron patron2 = new Patron();
        patron2.setId(patron1.getId());
        assertThat(patron1).isEqualTo(patron2);
        patron2.setId(2L);
        assertThat(patron1).isNotEqualTo(patron2);
        patron1.setId(null);
        assertThat(patron1).isNotEqualTo(patron2);
    }
}
