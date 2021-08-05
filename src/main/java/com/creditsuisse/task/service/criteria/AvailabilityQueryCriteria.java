package com.creditsuisse.task.service.criteria;

import java.io.Serializable;

public class AvailabilityQueryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
