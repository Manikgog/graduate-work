package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "comment_table")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    UserEntity author;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    AdEntity ad;

    @Column(name = "created_at", nullable = false, columnDefinition = "BIGINT")
    Long createdAt;

    @Column(name = "text", nullable = false, columnDefinition = "VARCHAR(64)")
    String text;

}
