package com.creditsuisse.task.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.creditsuisse.task.IntegrationTest;
import com.creditsuisse.task.domain.Patron;
import com.creditsuisse.task.repository.PatronRepository;
import com.creditsuisse.task.service.criteria.PatronCriteria;
import com.creditsuisse.task.service.dto.PatronDTO;
import com.creditsuisse.task.service.mapper.PatronMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatronResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatronResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patrons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PatronMapper patronMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatronMockMvc;

    private Patron patron;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patron createEntity(EntityManager em) {
        Patron patron = new Patron().name(DEFAULT_NAME).dateOfBirth(DEFAULT_DATE_OF_BIRTH).phoneNumber(DEFAULT_PHONE_NUMBER);
        return patron;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patron createUpdatedEntity(EntityManager em) {
        Patron patron = new Patron().name(UPDATED_NAME).dateOfBirth(UPDATED_DATE_OF_BIRTH).phoneNumber(UPDATED_PHONE_NUMBER);
        return patron;
    }

    @BeforeEach
    public void initTest() {
        patron = createEntity(em);
    }

    @Test
    @Transactional
    void createPatron() throws Exception {
        int databaseSizeBeforeCreate = patronRepository.findAll().size();
        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);
        restPatronMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patronDTO)))
            .andExpect(status().isCreated());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeCreate + 1);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatron.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testPatron.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createPatronWithExistingId() throws Exception {
        // Create the Patron with an existing ID
        patron.setId(1L);
        PatronDTO patronDTO = patronMapper.toDto(patron);

        int databaseSizeBeforeCreate = patronRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatronMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patronDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patronRepository.findAll().size();
        // set the field null
        patron.setName(null);

        // Create the Patron, which fails.
        PatronDTO patronDTO = patronMapper.toDto(patron);

        restPatronMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patronDTO)))
            .andExpect(status().isBadRequest());

        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = patronRepository.findAll().size();
        // set the field null
        patron.setDateOfBirth(null);

        // Create the Patron, which fails.
        PatronDTO patronDTO = patronMapper.toDto(patron);

        restPatronMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patronDTO)))
            .andExpect(status().isBadRequest());

        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = patronRepository.findAll().size();
        // set the field null
        patron.setPhoneNumber(null);

        // Create the Patron, which fails.
        PatronDTO patronDTO = patronMapper.toDto(patron);

        restPatronMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patronDTO)))
            .andExpect(status().isBadRequest());

        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatrons() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList
        restPatronMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patron.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getPatron() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get the patron
        restPatronMockMvc
            .perform(get(ENTITY_API_URL_ID, patron.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patron.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getPatronsByIdFiltering() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        Long id = patron.getId();

        defaultPatronShouldBeFound("id.equals=" + id);
        defaultPatronShouldNotBeFound("id.notEquals=" + id);

        defaultPatronShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatronShouldNotBeFound("id.greaterThan=" + id);

        defaultPatronShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatronShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatronsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where name equals to DEFAULT_NAME
        defaultPatronShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the patronList where name equals to UPDATED_NAME
        defaultPatronShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPatronsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where name not equals to DEFAULT_NAME
        defaultPatronShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the patronList where name not equals to UPDATED_NAME
        defaultPatronShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPatronsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPatronShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the patronList where name equals to UPDATED_NAME
        defaultPatronShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPatronsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where name is not null
        defaultPatronShouldBeFound("name.specified=true");

        // Get all the patronList where name is null
        defaultPatronShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPatronsByNameContainsSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where name contains DEFAULT_NAME
        defaultPatronShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the patronList where name contains UPDATED_NAME
        defaultPatronShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPatronsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where name does not contain DEFAULT_NAME
        defaultPatronShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the patronList where name does not contain UPDATED_NAME
        defaultPatronShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth is not null
        defaultPatronShouldBeFound("dateOfBirth.specified=true");

        // Get all the patronList where dateOfBirth is null
        defaultPatronShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultPatronShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the patronList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultPatronShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPatronsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultPatronShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the patronList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPatronShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPatronsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultPatronShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the patronList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultPatronShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPatronsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultPatronShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the patronList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPatronShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPatronsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where phoneNumber is not null
        defaultPatronShouldBeFound("phoneNumber.specified=true");

        // Get all the patronList where phoneNumber is null
        defaultPatronShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPatronsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultPatronShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the patronList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultPatronShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPatronsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultPatronShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the patronList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultPatronShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatronShouldBeFound(String filter) throws Exception {
        restPatronMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patron.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restPatronMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatronShouldNotBeFound(String filter) throws Exception {
        restPatronMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatronMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatron() throws Exception {
        // Get the patron
        restPatronMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatron() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeUpdate = patronRepository.findAll().size();

        // Update the patron
        Patron updatedPatron = patronRepository.findById(patron.getId()).get();
        // Disconnect from session so that the updates on updatedPatron are not directly saved in db
        em.detach(updatedPatron);
        updatedPatron.name(UPDATED_NAME).dateOfBirth(UPDATED_DATE_OF_BIRTH).phoneNumber(UPDATED_PHONE_NUMBER);
        PatronDTO patronDTO = patronMapper.toDto(updatedPatron);

        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patronDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronDTO))
            )
            .andExpect(status().isOk());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatron.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPatron.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patronDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patronDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatronWithPatch() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeUpdate = patronRepository.findAll().size();

        // Update the patron using partial update
        Patron partialUpdatedPatron = new Patron();
        partialUpdatedPatron.setId(patron.getId());

        partialUpdatedPatron.name(UPDATED_NAME);

        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatron.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatron))
            )
            .andExpect(status().isOk());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatron.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testPatron.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdatePatronWithPatch() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeUpdate = patronRepository.findAll().size();

        // Update the patron using partial update
        Patron partialUpdatedPatron = new Patron();
        partialUpdatedPatron.setId(patron.getId());

        partialUpdatedPatron.name(UPDATED_NAME).dateOfBirth(UPDATED_DATE_OF_BIRTH).phoneNumber(UPDATED_PHONE_NUMBER);

        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatron.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatron))
            )
            .andExpect(status().isOk());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatron.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPatron.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patronDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patronDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patronDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // Create the Patron
        PatronDTO patronDTO = patronMapper.toDto(patron);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patronDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatron() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeDelete = patronRepository.findAll().size();

        // Delete the patron
        restPatronMockMvc
            .perform(delete(ENTITY_API_URL_ID, patron.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
