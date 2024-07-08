package com.ahsan.scrap.model;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
public class Customer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	@NotNull
    @NotEmpty(message = "Name is required")
    @Column(name = "full_name")
	private String fullName;
    @NotEmpty(message = "Username is required")
    private String username;
    private String address;
    @Email(message = "Invalid email. Please enter a valid email address.")
    private String email;
    @Column(name = "phone_1")
    private String phone1;
    @Column(name = "phone_2")
    private String phone2;
    private String role;
    private String password;
}
