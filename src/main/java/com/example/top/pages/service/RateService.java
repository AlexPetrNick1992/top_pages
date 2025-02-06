package com.example.top.pages.service;

import com.example.top.pages.payload.response.AppResponse;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.RateAction;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.RateRepository;
import com.example.top.pages.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@RequiredArgsConstructor
@Service
public class RateService {
    private final RateRepository rateRepository;
    private final ItemsRepository itemsRepository;
    private final UserRepository userRepository;

    public List<Rate> getRateList() {return rateRepository.findAll();}

    public ResponseEntity<?> rateToItem(RateAction rateAction) {
        Optional<Items> tempItem = itemsRepository.findByStringUUID(rateAction.getItemId());
        User userItem = userRepository.findCheckedUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
        );
        System.out.println("!!!!!!!!!!");

        /* Не найден item */
        if (tempItem.isEmpty()) {
            return new ResponseEntity<>(
                    new AppResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            String.format("Item %s not found", rateAction.getItemId())
                    ),
                    HttpStatus.BAD_REQUEST
            );
        } else if (rateAction.getComment() == null || rateAction.getComment().isEmpty()) {
            return new ResponseEntity<>(
                    new AppResponse(
                            HttpStatus.BAD_REQUEST.value(), "Comment is empty"
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
        else {
            Rate rate = new Rate(
                    tempItem.get(),
                    userItem,
                    rateAction.getComment()
            );
            rateRepository.save(rate);
            return new ResponseEntity<>(
                    new AppResponse(
                            HttpStatus.OK.value(),
                            "Success rate"),
                    HttpStatus.OK);
        }
    }


}
