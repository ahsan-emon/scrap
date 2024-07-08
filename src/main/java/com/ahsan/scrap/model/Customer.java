package com.ahsan.scrap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private String fullName;
    private String username;
    private String address;
    private String email;
    @Column(name = "phone_1")
    private String phone1;
    @Column(name = "phone_2")
    private String phone2;
    private String role;
    private String password;
}
