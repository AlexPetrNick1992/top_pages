package com.example.top.pages.service;

import com.example.top.pages.payload.request.RateUpdate;
import com.example.top.pages.payload.response.AppResponse;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.RateAction;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.RateRepository;
import com.example.top.pages.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final ResponseEntityAppResponse responseEntityAppResponse;

    public List<Rate> getRateList() {return rateRepository.findAll();}

    public List<Rate> getListRatesForUser() {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        String userContextEmail = contextUser.getPrincipal().toString();
        User user = userRepository.findCheckedUserByEmail(userContextEmail);
        return rateRepository.getListRatesByIdUser(String.valueOf(user.getId()));
    }

    public ResponseEntity<?> approve(String rate_id) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        Optional<Rate> rateEntity = rateRepository.getRateByUUID(rate_id);
        if (rateEntity.isEmpty()) {return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Rate %s not exists", rate_id), null);}
        if (!roles.contains("ROLE_ADMIN")) {return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "User hasn't permission to approve this rate", null);}
        Rate rate = rateEntity.get();
        if (rate.isApproved()) {return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "This rate already is approved", null);}
        rate.setApproved(true);
        rateRepository.save(rate);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, String.format("Rate %s successfully approved", rate_id), rate_id);

    }

    public ResponseEntity<?> deleteRate(String rate_id) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        Optional<Rate> rateEntity = rateRepository.getRateByUUID(rate_id);
        String userContextEmail = contextUser.getPrincipal().toString();

        if (rateEntity.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Rate %s not exists", rate_id), null);
        }
        Rate rate = rateEntity.get();
        /* Роли */
        if (!roles.contains("ROLE_ADMIN") && !Objects.equals(rate.getUser().getEmail(), userContextEmail)) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "User hasn't permission to delete this rate", null);
        }
        rateRepository.delete(rate);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, String.format("Rate %s successfully delete", rate_id), rate_id);

    }

    public ResponseEntity<?> updateRate (String rate_id, RateUpdate comment) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        String userContextEmail = contextUser.getPrincipal().toString();

        if (comment.getComment() == null || comment.getComment().isEmpty()) {return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Comment hasn't be empty", null);}

        Optional<Rate> rateEntity = rateRepository.getRateByUUID(rate_id);
        if (rateEntity.isEmpty()) { return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Rate %s not exists", rate_id), null);
        } else {
            /* Проверка ролей */
            Rate rate = rateEntity.get();
            if (!roles.contains("ROLE_ADMIN") && !Objects.equals(rate.getUser().getEmail(), userContextEmail)) {
                return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "User hasn't permission to change this rate", null);
            }
            rate.setComment(comment.getComment());
            rate.setPositive(comment.isPositive());
            rateRepository.save(rate);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, String.format("Rate %s successfully update", rate_id), rate_id);
        }
    }

    public ResponseEntity<?> rateToItem(RateAction rateAction) {
        Optional<Items> tempItem = itemsRepository.findByStringUUID(rateAction.getItemId());
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        String userContextEmail = contextUser.getPrincipal().toString();
        User userItem = userRepository.findCheckedUserByEmail(userContextEmail);
        String userContextUUID = userItem.getId().toString();

        /* Проверяем наличие Item */
        if (tempItem.isEmpty()) {return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Item %s not found", rateAction.getItemId()), null);}

        Optional<Rate> rateEntity = rateRepository.getItemAndRateByUserAndItem(userContextUUID, String.valueOf(tempItem.get().getId()));

        /* Если существует - то нельзя повторно */
        if (rateEntity.isPresent()) {
            String username = rateEntity.get().getItem().getName();
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("User has rate on item %s", username), null);
        }
        if (rateAction.getComment() == null || rateAction.getComment().isEmpty()) {return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Comment is empty", null);}
        else {
            /* Админ isApproved = true */
            Boolean isApproved = roles.contains("ROLE_ADMIN") || roles.contains("ROLE_APPROVED");

            Rate rate = new Rate(
                    tempItem.get(),
                    userItem,
                    rateAction.getComment(),
                    isApproved,
                    rateAction.getIsPositive()
            );
            System.out.println(rate);
            rateRepository.save(rate);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Success rate", String.valueOf(rate.getId()));
        }
    }
}
