package com.creditsuisse.task.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.creditsuisse.task.IntegrationTest;
import com.creditsuisse.task.domain.Inventory;
import com.creditsuisse.task.domain.Patron;
import com.creditsuisse.task.domain.Rental;
import com.creditsuisse.task.repository.RentalRepository;
import com.creditsuisse.task.service.criteria.RentalCriteria;
import com.creditsuisse.task.service.dto.RentalDTO;
import com.creditsuisse.task.service.mapper.RentalMapper;
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
 * Integration tests for the {@link RentalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RentalResourceIT {

    private static final LocalDate DEFAULT_RENTAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RENTAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RENTAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RETURN_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/rentals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalMockMvc;

    private Rental rental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createEntity(EntityManager em) {
        Rental rental = new Rental().rentalDate(DEFAULT_RENTAL_DATE).returnDate(DEFAULT_RETURN_DATE);
        return rental;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createUpdatedEntity(EntityManager em) {
        Rental rental = new Rental().rentalDate(UPDATED_RENTAL_DATE).returnDate(UPDATED_RETURN_DATE);
        return rental;
    }

    @BeforeEach
    public void initTest() {
        rental = createEntity(em);
    }

    @Test
    @Transactional
    void createRental() throws Exception {
        int databaseSizeBeforeCreate = rentalRepository.findAll().size();
        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);
        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isCreated());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate + 1);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getRentalDate()).isEqualTo(DEFAULT_RENTAL_DATE);
        assertThat(testRental.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
    }

    @Test
    @Transactional
    void createRentalWithExistingId() throws Exception {
        // Create the Rental with an existing ID
        rental.setId(1L);
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        int databaseSizeBeforeCreate = rentalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRentalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalRepository.findAll().size();
        // set the field null
        rental.setRentalDate(null);

        // Create the Rental, which fails.
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isBadRequest());

        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRentals() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].rentalDate").value(hasItem(DEFAULT_RENTAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())));
    }

    @Test
    @Transactional
    void getRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get the rental
        restRentalMockMvc
            .perform(get(ENTITY_API_URL_ID, rental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rental.getId().intValue()))
            .andExpect(jsonPath("$.rentalDate").value(DEFAULT_RENTAL_DATE.toString()))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE.toString()));
    }

    @Test
    @Transactional
    void getRentalsByIdFiltering() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        Long id = rental.getId();

        defaultRentalShouldBeFound("id.equals=" + id);
        defaultRentalShouldNotBeFound("id.notEquals=" + id);

        defaultRentalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRentalShouldNotBeFound("id.greaterThan=" + id);

        defaultRentalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRentalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate equals to DEFAULT_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.equals=" + DEFAULT_RENTAL_DATE);

        // Get all the rentalList where rentalDate equals to UPDATED_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.equals=" + UPDATED_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate not equals to DEFAULT_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.notEquals=" + DEFAULT_RENTAL_DATE);

        // Get all the rentalList where rentalDate not equals to UPDATED_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.notEquals=" + UPDATED_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsInShouldWork() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate in DEFAULT_RENTAL_DATE or UPDATED_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.in=" + DEFAULT_RENTAL_DATE + "," + UPDATED_RENTAL_DATE);

        // Get all the rentalList where rentalDate equals to UPDATED_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.in=" + UPDATED_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate is not null
        defaultRentalShouldBeFound("rentalDate.specified=true");

        // Get all the rentalList where rentalDate is null
        defaultRentalShouldNotBeFound("rentalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate is greater than or equal to DEFAULT_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.greaterThanOrEqual=" + DEFAULT_RENTAL_DATE);

        // Get all the rentalList where rentalDate is greater than or equal to UPDATED_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.greaterThanOrEqual=" + UPDATED_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate is less than or equal to DEFAULT_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.lessThanOrEqual=" + DEFAULT_RENTAL_DATE);

        // Get all the rentalList where rentalDate is less than or equal to SMALLER_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.lessThanOrEqual=" + SMALLER_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate is less than DEFAULT_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.lessThan=" + DEFAULT_RENTAL_DATE);

        // Get all the rentalList where rentalDate is less than UPDATED_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.lessThan=" + UPDATED_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByRentalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where rentalDate is greater than DEFAULT_RENTAL_DATE
        defaultRentalShouldNotBeFound("rentalDate.greaterThan=" + DEFAULT_RENTAL_DATE);

        // Get all the rentalList where rentalDate is greater than SMALLER_RENTAL_DATE
        defaultRentalShouldBeFound("rentalDate.greaterThan=" + SMALLER_RENTAL_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate equals to DEFAULT_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.equals=" + DEFAULT_RETURN_DATE);

        // Get all the rentalList where returnDate equals to UPDATED_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.equals=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate not equals to DEFAULT_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.notEquals=" + DEFAULT_RETURN_DATE);

        // Get all the rentalList where returnDate not equals to UPDATED_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.notEquals=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsInShouldWork() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate in DEFAULT_RETURN_DATE or UPDATED_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.in=" + DEFAULT_RETURN_DATE + "," + UPDATED_RETURN_DATE);

        // Get all the rentalList where returnDate equals to UPDATED_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.in=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate is not null
        defaultRentalShouldBeFound("returnDate.specified=true");

        // Get all the rentalList where returnDate is null
        defaultRentalShouldNotBeFound("returnDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate is greater than or equal to DEFAULT_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.greaterThanOrEqual=" + DEFAULT_RETURN_DATE);

        // Get all the rentalList where returnDate is greater than or equal to UPDATED_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.greaterThanOrEqual=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate is less than or equal to DEFAULT_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.lessThanOrEqual=" + DEFAULT_RETURN_DATE);

        // Get all the rentalList where returnDate is less than or equal to SMALLER_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.lessThanOrEqual=" + SMALLER_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate is less than DEFAULT_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.lessThan=" + DEFAULT_RETURN_DATE);

        // Get all the rentalList where returnDate is less than UPDATED_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.lessThan=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByReturnDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where returnDate is greater than DEFAULT_RETURN_DATE
        defaultRentalShouldNotBeFound("returnDate.greaterThan=" + DEFAULT_RETURN_DATE);

        // Get all the rentalList where returnDate is greater than SMALLER_RETURN_DATE
        defaultRentalShouldBeFound("returnDate.greaterThan=" + SMALLER_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByPatronIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);
        Patron patron = PatronResourceIT.createEntity(em);
        em.persist(patron);
        em.flush();
        rental.setPatron(patron);
        rentalRepository.saveAndFlush(rental);
        Long patronId = patron.getId();

        // Get all the rentalList where patron equals to patronId
        defaultRentalShouldBeFound("patronId.equals=" + patronId);

        // Get all the rentalList where patron equals to (patronId + 1)
        defaultRentalShouldNotBeFound("patronId.equals=" + (patronId + 1));
    }

    @Test
    @Transactional
    void getAllRentalsByInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);
        Inventory inventory = InventoryResourceIT.createEntity(em);
        em.persist(inventory);
        em.flush();
        rental.setInventory(inventory);
        rentalRepository.saveAndFlush(rental);
        Long inventoryId = inventory.getId();

        // Get all the rentalList where inventory equals to inventoryId
        defaultRentalShouldBeFound("inventoryId.equals=" + inventoryId);

        // Get all the rentalList where inventory equals to (inventoryId + 1)
        defaultRentalShouldNotBeFound("inventoryId.equals=" + (inventoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRentalShouldBeFound(String filter) throws Exception {
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].rentalDate").value(hasItem(DEFAULT_RENTAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())));

        // Check, that the count call also returns 1
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRentalShouldNotBeFound(String filter) throws Exception {
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRental() throws Exception {
        // Get the rental
        restRentalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental
        Rental updatedRental = rentalRepository.findById(rental.getId()).get();
        // Disconnect from session so that the updates on updatedRental are not directly saved in db
        em.detach(updatedRental);
        updatedRental.rentalDate(UPDATED_RENTAL_DATE).returnDate(UPDATED_RETURN_DATE);
        RentalDTO rentalDTO = rentalMapper.toDto(updatedRental);

        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getRentalDate()).isEqualTo(UPDATED_RENTAL_DATE);
        assertThat(testRental.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void putNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getRentalDate()).isEqualTo(DEFAULT_RENTAL_DATE);
        assertThat(testRental.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
    }

    @Test
    @Transactional
    void fullUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental.rentalDate(UPDATED_RENTAL_DATE).returnDate(UPDATED_RETURN_DATE);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getRentalDate()).isEqualTo(UPDATED_RENTAL_DATE);
        assertThat(testRental.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rentalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeDelete = rentalRepository.findAll().size();

        // Delete the rental
        restRentalMockMvc
            .perform(delete(ENTITY_API_URL_ID, rental.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
