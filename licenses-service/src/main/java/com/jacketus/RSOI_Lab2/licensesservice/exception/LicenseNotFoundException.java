package com.jacketus.RSOI_Lab2.licensesservice.exception;

public class LicenseNotFoundException extends Exception {
    public LicenseNotFoundException(Long id){
        super("License with id= " + id + " was not found!");
    }
}
