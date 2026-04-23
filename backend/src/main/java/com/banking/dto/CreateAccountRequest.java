package com.banking.dto;

/**
 * DTO (Data Transfer Object) for creating a new account.
 *
 * DTOs are simple classes used to carry data between the client and server.
 * They help us control exactly what data we accept from the user,
 * separate from our database models.
 */
public class CreateAccountRequest {

    private String ownerName;

    public CreateAccountRequest() {}

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
}
