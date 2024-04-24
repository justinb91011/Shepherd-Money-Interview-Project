package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.TreeMap;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    // Credit card's owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Credit card's balance history
    // Credit card's balance history
    @ElementCollection
    @CollectionTable(name = "balance_history", joinColumns = @JoinColumn(name = "credit_card_id"))
    @MapKeyColumn(name = "date")
    @Column(name = "balance")
    @OrderBy("date DESC")
    private TreeMap<String, Double> balanceHistory = new TreeMap<>();


}
