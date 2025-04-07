package com.example.SocialMedia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from", referencedColumnName = "username")
    private User userFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to", referencedColumnName = "username")
    private User userTo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    private Chat chat;

    @NotNull(message = "Create date is required")
    @CreationTimestamp
    private Date createDate;

    @NotNull(message = "Value is required")
    private String value;

    @NotNull(message = "Type is required")
    private Integer type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction", referencedColumnName = "id")
    private Reaction reaction;

    @NotNull(message = "Status is required")
    private String status;
}
