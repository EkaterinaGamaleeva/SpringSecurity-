package com.SpringSecurityTest.SpringSecurityTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "consumers")
@NoArgsConstructor
@AllArgsConstructor
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Login should not be empty")
    @Size(min = 2, max = 100, message = "Login should be between 2 and 100 characters")
    @Column(name = "username")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 2, max = 100, message = "Password should be between 2 and 100 characters")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = " Sex should not be empty")
    @Column(name = "sex")
    private String sex;

    @Min(value = 0, message = "Значение не может быть меньше 0")
    @NotNull(message = " Age should not be empty")
    @Column(name = "age")
    private int age;

    @NotNull
    @Email
//    @Min(value = 0, message = "Значение не может быть меньше 0")
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private EnumRole role;
}
