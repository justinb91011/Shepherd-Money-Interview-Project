package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "MyUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    // User's credit cards. Need to have a one-to-many relationship.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CreditCard> creditCards = new HashSet<>();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addCreditCard(CreditCard creditCard) {
        this.creditCards.add(creditCard);
        creditCard.setUser(this);
    }

    public void removeCreditCard(CreditCard creditCard) {
        this.creditCards.remove(creditCard);
        creditCard.setUser(null);
    }
}
