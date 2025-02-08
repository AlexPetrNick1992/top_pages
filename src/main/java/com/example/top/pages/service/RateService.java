package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.payload.request.RateUpdate;
import com.example.top.pages.payload.response.AppResponse;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.RateAction;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public List<Rate> getRateList() {
        return rateRepository.findAll();
    }

    public List<Rate> getListRatesForUser() {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        String userContextEmail = contextUser.getPrincipal().toString();
        User user = userRepository.findCheckedUserByEmail(userContextEmail);
        return rateRepository.getListRatesByIdUser(String.valueOf(user.getId()));
    }

    public ResponseEntity<?> approve(String rate_id, String category_id) {
        /* Проверка категории */
        Optional<Category> categoryCheck = categoryRepository.findByUUIDString(category_id);
        if (categoryCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not exists", rate_id);
        }
        Optional<Rate> rateEntity = rateRepository.getRateByUUID(rate_id);
        if (rateEntity.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Rate %s not exists", rate_id);
        }
        Rate rate = rateEntity.get();
        if (rate.isApproved()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "This rate already is approved", null);
        }
        rate.setApproved(true);
        rateRepository.save(rate);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Rate successfully approved", rate_id);
    }

    public ResponseEntity<?> disprove(String rate_id, String category_id) {
        /* Проверка категории */
        Optional<Category> categoryCheck = categoryRepository.findByUUIDString(category_id);
        if (categoryCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not exists", rate_id);
        }
        Optional<Rate> rateEntity = rateRepository.getRateByUUID(rate_id);
        if (rateEntity.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Rate not exists", rate_id);
        }
        Rate rate = rateEntity.get();
        if (!rate.isApproved()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "This rate already is disproved", null);
        }
        rate.setApproved(false);
        rateRepository.save(rate);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Rate successfully disprove", rate_id);
    }

    public ResponseEntity<?> deleteRate(String rate_id, String category_id) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        String userContextEmail = contextUser.getPrincipal().toString();
        /* Проверка категории */
        Optional<Category> categoryCheck = categoryRepository.findByUUIDString(category_id);
        if (categoryCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not exists", rate_id);
        }
        Optional<Rate> rateEntity = rateRepository.getRateByUUID(rate_id);
        if (rateEntity.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Rate not exists", rate_id);
        }
        Rate rate = rateEntity.get();
        /* Роли */
        if (!roles.contains("ROLE_ADMIN") && !Objects.equals(rate.getUser().getEmail(), userContextEmail)) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "User hasn't permission to delete this rate", null);
        }
        rateRepository.delete(rate);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Rate successfully delete", rate_id);
    }

    public ResponseEntity<?> updateRate(String rate_id, RateUpdate comment) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        String userContextEmail = contextUser.getPrincipal().toString();
        /* Категория */
        if (categoryRepository.findByUUIDString(comment.getCategoryId()).isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not found", comment.getCategoryId());
        } else {
            Optional<Rate> rateEntity = rateRepository.getRateByIDAndCategory(rate_id, comment.getCategoryId());
            if (rateEntity.isEmpty()) {
                return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Rate not exists", rate_id);
            } else {
                /* Проверка ролей */
                Rate rate = rateEntity.get();
                if (!roles.contains("ROLE_ADMIN") && !Objects.equals(rate.getUser().getEmail(), userContextEmail)) {
                    return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "User hasn't permission to change this rate", null);
                }
                rate.setComment(comment.getComment());
                rate.setPositive(comment.isPositive());
                rateRepository.save(rate);
                return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Rate successfully update", rate_id);
            }
        }
    }

    public ResponseEntity<?> rateToItem(RateAction rateAction) {
        Items tempItem;
        Optional<Items> itemsCheck = itemsRepository.findByStringUUID(rateAction.getItemId());
        if (itemsCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Item not found", rateAction.getItemId());
        } else {
            tempItem = itemsCheck.get();
        }

        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        User userItem = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

        /* Категория */
        Optional<Category> categoryCheck = categoryRepository.findByUUIDString(rateAction.getCategoryId());
        if (categoryCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not found", rateAction.getCategoryId());
        } else {
            /* Если существует - то нельзя повторно */
            System.out.println(rateAction.getCategoryId());
            Optional<Rate> rateEntity = rateRepository.getItemAndRateByUserAndItemAndCategory(
                    userItem.getId().toString(),
                    String.valueOf(tempItem.getId()),
                    rateAction.getCategoryId()
            );
            if (rateEntity.isPresent()) {
                String username = rateEntity.get().getItem().getName();
                return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("User has rate on item %s", username), null);
            }

            Category category = categoryCheck.get();
            /* Админ isApproved = true */
            Boolean isApproved = roles.contains("ROLE_ADMIN") || roles.contains("ROLE_APPROVED");
            Rate rate = new Rate(
                    tempItem,
                    userItem,
                    rateAction.getComment(),
                    isApproved,
                    rateAction.getIsPositive(),
                    category
            );
            rateRepository.save(rate);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Success rate", String.valueOf(rate.getId()));
        }
    }
}
