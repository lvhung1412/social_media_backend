package com.example.SocialMedia.entity;

import com.example.SocialMedia.entity.keys.RelationShipKey;
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
@Table(name = "relationship")
@IdClass(RelationShipKey.class)
public class RelationShip {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_from")
    private User userFrom;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_to")
    private User userTo;

    @CreationTimestamp
    private Date createDate;

    @NotNull(message = "Status is required")
    private String status;
}
