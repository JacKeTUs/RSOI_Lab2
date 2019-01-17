package com.jacketus.RSOI_Lab2.purchasesservice.exception;

public class PurchaseNotFoundException extends Exception {
    public PurchaseNotFoundException(Long id){
        super("Purchase with id= " + id + " was not found!");
    }

    public PurchaseNotFoundException(String login){
        super("Purchase with login= " + login + " was not found!");
    }
}
