package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class CreditCardController {

    private CreditCardRepository creditCardRepository;
    private UserRepository userRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        User user = userRepository.findById(payload.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(400);
        }
        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setIssuanceBank(payload.getCardIssuanceBank());
        newCreditCard.setNumber(payload.getCardNumber());
        newCreditCard.setUser(user);

        CreditCard saveCreditCard = creditCardRepository.save(newCreditCard);

        // Return the ID of the newly created credit card
        return ResponseEntity.ok(saveCreditCard.getId());
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getCreditCards().isEmpty()) {
            // If user does not exist or user has no credit cards return empty List.
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<CreditCardView> cards = user.getCreditCards().stream().
                map(card -> new CreditCardView(card.getIssuanceBank(), card.getNumber()))
                .toList();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        CreditCard card = creditCardRepository
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        return null;
    }

    @PostMapping("/credit-card:update-balance")
    public SomeEnityData postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      1. For the balance history in the credit card
        //      2. If there are gaps between two balance dates, fill the empty date with the balance of the previous date
        //      3. Given the payload `payload`, calculate the balance different between the payload and the actual balance stored in the database
        //      4. If the different is not 0, update all the following budget with the difference
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a balance amount of {date: 4/11, amount: 110}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
        
        return null;
    }
    
}
