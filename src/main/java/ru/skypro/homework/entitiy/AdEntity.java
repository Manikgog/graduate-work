package ru.skypro.homework.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ad_table")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    UserEntity author;

    @Column(name = "image", nullable = false, columnDefinition = "TEXT")
    String image;

    @Column(name = "price", nullable = false)
    Integer price;

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(32)")
    String title;

    @Column(name = "description", nullable = false, columnDefinition = "VARCHAR(64)")
    String description;
}
