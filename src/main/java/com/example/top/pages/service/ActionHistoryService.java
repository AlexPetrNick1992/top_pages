package com.example.top.pages.service;

import com.example.top.pages.models.ActionHistory;
import com.example.top.pages.models.User;
import com.example.top.pages.repository.ActionHistoryRepository;
import com.example.top.pages.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ActionHistoryService {

    private final ActionHistoryRepository actionHistoryRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getUserHistory() {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());
        return ResponseEntity.ok(actionHistoryRepository.findHistoryByUserId(user.getId().toString()));
    }

    public enum TypeHistory {
        USER_UPDATE,
        ITEM_CREATE,
        VOTED,
        PAGES_CREATE,
        CATEGORY_CREATE,
        DELETED_RATE,
        DELETED_ITEM,
        DELETED_CATEGORY,
        USER_REGISTER
    }
    public enum TypeHistoryAdmin {
        APPROVED_RATE,
        APPROVED_ITEM,
        APPROVED_PAGES,
        APPROVED_CATEGORY,
        DISAPPROVED_RATE,
        DISAPPROVED_ITEM,
        DISAPPROVED_PAGES,
        DISAPPROVED_CATEGORY,
        DELETED_RATE,
        DELETED_ITEM,
        DELETED_PAGES,
        DELETED_CATEGORY
    }

    public void createHistoryByTypeWithAdmin(TypeHistoryAdmin typeHistory, String adminInfo, String addedParameter, User userAction) {
        String textHistory = "";
        String username = userAction.getName();
        textHistory = switch (typeHistory) {
            case TypeHistoryAdmin.APPROVED_RATE ->
                    String.format("Администратор %s подтвердил голос - %s пользователя %s", adminInfo, addedParameter, username);
            case TypeHistoryAdmin.APPROVED_ITEM ->
                    String.format("Администратор %s подтвердил сущность - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.APPROVED_PAGES ->
                    String.format("Администратор %s подтвердил новую страницу - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.APPROVED_CATEGORY ->
                    String.format("Администратор %s подтвердил новую категорю - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DISAPPROVED_RATE ->
                    String.format("Администратор %s отменил голос - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DISAPPROVED_ITEM ->
                    String.format("Администратор %s отменил сущность - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DISAPPROVED_PAGES ->
                    String.format("Администратор %s отменил новую страницу - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DISAPPROVED_CATEGORY ->
                    String.format("Администратор %s отменил новую категорю - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DELETED_RATE ->
                    String.format("Администратор %s удалил голос - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DELETED_ITEM ->
                    String.format("Администратор %s удалил сущность - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DELETED_PAGES ->
                    String.format("Администратор %s удалил новую страницу - %s пользователя %s", username, addedParameter, username);
            case TypeHistoryAdmin.DELETED_CATEGORY ->
                    String.format("Администратор %s удалил новую категортю - %s пользователя %s", username, addedParameter, username);
            default -> textHistory;
        };

        ActionHistory newAction = new ActionHistory(textHistory, userAction);
        actionHistoryRepository.save(newAction);
    }

    public void createHistoryByType(TypeHistory typeHistory, User userAction, String addedParameter) {
        String textHistory = "";
        String username = userAction.getName();
        textHistory = switch (typeHistory) {
            case TypeHistory.USER_UPDATE ->
                    String.format("Пользователь %s изменил параметр(ы) - %s", username, addedParameter);
            case TypeHistory.ITEM_CREATE ->
                    String.format("Пользователь %s создал выбор - %s", username, addedParameter);
            case TypeHistory.VOTED -> String.format("Пользователь %s поголосовал - %s", username, addedParameter);
            case TypeHistory.PAGES_CREATE ->
                    String.format("Пользователь %s создал новую страницу - %s", username, addedParameter);
            case TypeHistory.CATEGORY_CREATE ->
                    String.format("Пользователь %s создал новую категорию %s", username, addedParameter);
            case TypeHistory.DELETED_RATE ->
                    String.format("Администратор %s удалил голос - %s", username, addedParameter);
            case TypeHistory.DELETED_ITEM ->
                    String.format("Администратор %s удалил сущность - %s", username, addedParameter);
            case TypeHistory.DELETED_CATEGORY ->
                    String.format("Администратор %s удалил новую категортю - %s", username, addedParameter);
            case TypeHistory.USER_REGISTER ->
                    String.format("Пользователь %s зарегистрировался - %s", username, addedParameter);
            default -> textHistory;
        };

        ActionHistory newAction = new ActionHistory(textHistory, userAction);
        actionHistoryRepository.save(newAction);
    }


}
