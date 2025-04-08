package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
