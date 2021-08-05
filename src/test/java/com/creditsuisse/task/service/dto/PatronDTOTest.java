package com.creditsuisse.task.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.creditsuisse.task.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatronDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatronDTO.class);
        PatronDTO patronDTO1 = new PatronDTO();
        patronDTO1.setId(1L);
        PatronDTO patronDTO2 = new PatronDTO();
        assertThat(patronDTO1).isNotEqualTo(patronDTO2);
        patronDTO2.setId(patronDTO1.getId());
        assertThat(patronDTO1).isEqualTo(patronDTO2);
        patronDTO2.setId(2L);
        assertThat(patronDTO1).isNotEqualTo(patronDTO2);
        patronDTO1.setId(null);
        assertThat(patronDTO1).isNotEqualTo(patronDTO2);
    }
}
