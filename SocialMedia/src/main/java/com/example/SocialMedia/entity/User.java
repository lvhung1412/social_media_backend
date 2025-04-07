package com.example.SocialMedia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(length = 45)
    private String username;

    @Column(length = 45)
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Password is required")
    private String password;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = ("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
            + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$"), message = "Invalid email")
    @Size(max = 30, min = 10, message = "Invalid mail size")
    private String email;

    @Column(unique = true)
    @Size(max = 12, min = 9, message = "Invalid phone size")
    @NotNull(message = "Phone is required")
    private String phone;

    @CreationTimestamp
    private Date birthday;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address", referencedColumnName = "id")
    private Address address;

    @CreationTimestamp
    private Date createDate;

    private String gender;

    private String avatar;

    private String nickname;

    private String bio;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(length = 64)
    private String verificationCode;

    @NotNull(message = "Enable is required")
    private Boolean enable;

    @NotNull(message = "Security is required")
    private String security = "PUBLIC";
}
