package com.bank.wealthstream.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(name = "ID_CUS", nullable = false,updatable = false)
    @PrimaryKeyJoinColumn(name = "ID_CUS",foreignKey = @ForeignKey(name = "FK_CUSTOMER_PERSON"))
    private String idCus;

    @OneToOne
    @JoinColumn(name = "ID_CUS",nullable = false,foreignKey = @ForeignKey(name = "FK_CUSTOMER_PERSON"))
    private Person idPer;

    @Column(name = "PASSWORD",nullable = false)
    private String password;

    @Column(name="EMAIL", nullable = false)
    private String email;

    @Column(name = "STATE",nullable = false)
    private Boolean state;

}
