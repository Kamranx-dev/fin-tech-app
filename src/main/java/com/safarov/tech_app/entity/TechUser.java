package com.safarov.tech_app.entity;

import com.safarov.tech_app.dto.request.AccountRequestDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tech_users", schema = "dbo")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TechUser {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "User_name")
    String name;
    @Column(name = "User_surname")
    String surname;
    @Column(name = "Pin", unique = true)
    String pin;
    @Column(name = "Password")
    String password;
    @Column(name = "role")
    String role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    List<Account> accountList;

    public void addAccountToUser(List<AccountRequestDTO> accountRequestDTOList) {
        accountList = new ArrayList<>();
        accountRequestDTOList.forEach(accountRequestDTO -> accountList.add(Account.builder()
                .user(this)
                .accountNo(accountRequestDTO.getAccountNo())
                .balance(accountRequestDTO.getBalance())
                .currency(accountRequestDTO.getCurrency())
                .isActive(accountRequestDTO.getIsActive()).build()));

    }
}
