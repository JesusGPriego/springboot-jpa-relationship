package com.suleware.springboot.jpa.springboot_jpa_relationship.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "client_details")
@Data
public class ClientDetails {

    public ClientDetails() {
    }

    public ClientDetails(boolean isPremium, Integer points) {
        this.isPremium = isPremium;
        this.points = points;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isPremium;
    private Integer points;

}
