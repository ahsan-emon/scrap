package com.ahsan.scrap.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
@Table(name = "user_dtls")
public class UserDtls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name is required")
    @Column(name = "full_name")
    private String fullName;
    @NotEmpty(message = "Username is required")
    private String username;
    @Email(message = "Invalid email. Please enter a valid email address.")
    private String email;
    private String address;
    private String iqama;
    private String phone;
    private String passport;
    @Column(name = "visa_exp_date")
    private LocalDate visaExpDate;
    private String password;
    private String role;
    private String photoPath;
}
