package com.safarov.tech_app.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "User_accounts", schema = "dbo")
public class Account {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "Balance")
    BigDecimal balance;
    @Column(name = "Currency")
    Currency currency;
    @Column(name = "Account_status")
    Boolean isActive;
    @Column(name = "Account_number", unique = true)
    Integer accountNo;
    @ManyToOne
    @JoinColumn(name = "User_id")
    TechUser user;
}
