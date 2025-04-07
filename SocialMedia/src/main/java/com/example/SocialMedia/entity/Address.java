package com.example.SocialMedia.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    private String id;

    @NotNull(message = "Address city is required")
    private String country;

    @NotNull(message = "Address province is required")
    private String province;

    @NotNull(message = "Address district is required")
    private String district;

    @NotNull(message = "Address ward is required")
    private String ward;

    @NotNull(message = "Address detail is required")
    private String addressDetail;

    @NotNull(message = "Status is required")
    private String status;
}
