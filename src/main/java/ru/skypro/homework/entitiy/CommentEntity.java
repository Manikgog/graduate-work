package ru.skypro.homework.entitiy;

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
    @JoinColumn(name = "author_id")
    UserEntity author;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    AdEntity ad;

    @Column(name = "created_at")
    Long createdAt;

    @Column(name = "text", nullable = false, columnDefinition = "VARCHAR(64)")
    String text;

}
