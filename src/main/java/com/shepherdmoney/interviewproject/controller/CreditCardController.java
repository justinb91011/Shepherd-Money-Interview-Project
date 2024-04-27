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
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;



@RestController
public class CreditCardController {

    private CreditCardRepository creditCardRepository;
    private UserRepository userRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        User user = userRepository.findById(payload.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
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
        CreditCard card = creditCardRepository.findByNumber(creditCardNumber);
        if (card != null && card.getUser() != null) {
            // If the card exists and it has a user. Return the userId
            return ResponseEntity.ok(card.getUser().getId());
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<String> postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        for (UpdateBalancePayload payloads: payload) {
            CreditCard card = creditCardRepository.findByNumber(payloads.getCreditCardNumber());
            if (card == null) {
                return ResponseEntity.badRequest().body("Credit card number " + payloads.getCreditCardNumber() + " not found");
            }
            // Call function to update the Balance
            updateBalanceHistory(card.getBalanceHistory(), payloads);
        }
        return ResponseEntity.ok("Balances updated successfully.");
    }

    private void updateBalanceHistory(NavigableMap<String, Double> balanceHistory, UpdateBalancePayload payload) {
        String payloadDate = payload.getBalanceDate().toString();
        String floorKey = balanceHistory.floorKey(payloadDate);
        if (floorKey == null || !floorKey.equals(payloadDate)) {
            // If there is no exact match, and the date is not in the map, insert with the same balance as the closest previous date
            double previousBalance = (floorKey == null) ? 0 : balanceHistory.get(floorKey);
            balanceHistory.put(payloadDate, previousBalance + payload.getBalanceAmount());
        } else {
            // If there's an exact match, update the balance
            balanceHistory.put(payloadDate, balanceHistory.get(payloadDate) + payload.getBalanceAmount());
        }
        balanceHistory.tailMap(payloadDate, false).replaceAll((date, balance) -> balance + payload.getBalanceAmount());
    }
}
