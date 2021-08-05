package com.creditsuisse.task.service.exception;

public class ItemAlreadyRentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ItemAlreadyRentException() {
        super("Inventory item is already rent!");
    }
}
