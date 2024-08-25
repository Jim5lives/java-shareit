package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
}
