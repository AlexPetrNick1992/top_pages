package com.example.top.pages.service;

import com.example.top.pages.models.*;
import com.example.top.pages.payload.request.ItemInPagesCreate;
import com.example.top.pages.payload.request.PagesRequest;
import com.example.top.pages.payload.request.PagesUpdate;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.PagesRepository;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import com.example.top.pages.repository.models.Pages.PagesCategory;
import com.example.top.pages.repository.models.Pages.PagesItems;
import com.example.top.pages.repository.models.Pages.PagesItemsExistedRates;
import com.example.top.pages.repository.models.Pages.ReturnCountRates;
import com.example.top.pages.utils.DateWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagesService {
    private final PagesRepository pagesRepository;
    private final ResponseEntityAppResponse responseEntityAppResponse;
    private final CategoryRepository categoryRepository;
    private final ItemsRepository itemsRepository;
    private final UserRepository userRepository;
    private final DateWorker dateWorker;

    public ResponseEntity<?> getPagesList() {
        return ResponseEntity.ok(getPagesInfo(pagesRepository.getAllPagesIds()));
    }

    public ResponseEntity<?> joinItem(String itemId, String pagesId) {
        Optional<Items> itemCheck = itemsRepository.findByStringUUID(itemId);
        if (itemCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Items not exists", itemId);
        }
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pagesId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not exists", pagesId);
        }
        Items item = itemCheck.get();
        Pages page = pagesCheck.get();
        /* Добавляем к item зависимость category */
        List<Category> categoriesList = item.getCategory();
        categoriesList.add(page.getCategory());
        item.setCategory(categoriesList);
        itemsRepository.save(item);

        /* Добавляем зависимость items к pages */
        Collection<Items> items = page.getItems();
        Collection<UUID> itemsIds = items.stream().map(Items::getId).toList();
        if (itemsIds.contains(item.getId())) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "This page contains item", String.valueOf(item.getId()));
        }
        items.add(item);
        pagesRepository.save(page);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully add item", String.valueOf(item.getId()));
    }

    public ResponseEntity<?> deletePages(String pagesId) {
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pagesId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not exists", pagesId);
        } else {
            Pages page = pagesCheck.get();
            Category category = categoryRepository.findByUUIDStringStrick(String.valueOf(page.getCategory().getId()));
            categoryRepository.delete(category);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully deleted", String.valueOf(page.getId()));
        }
    }

    public ResponseEntity<?> approvePages(String pageId) {
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pageId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not exists", pageId);
        } else {
            Pages page = pagesCheck.get();
            if (page.isApproved()) {
                return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages already approved", pageId);
            }
            page.setApproved(true);
            pagesRepository.save(page);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully approved", null);
        }
    }

    public ResponseEntity<?> disprovePages(String pageId) {
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pageId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not exists", pageId);
        } else {
            Pages page = pagesCheck.get();
            if (!page.isApproved()) {
                return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages already disprove", pageId);
            }
            page.setApproved(false);
            pagesRepository.save(page);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully disprove", null);
        }
    }

    public ResponseEntity<?> getSinglePage(String pages_id, String pages_title, String mode, String type) {
        Optional<PagesCategory> pages;
        if (pages_id == null && pages_title == null) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Request need pages_id or pages_title", null);
        }
        if (pages_id == null) {
            pages_id = "";
        }
        if (pages_id.isEmpty()) {
            pages = pagesRepository.getAllPagesWithCategoryByTitle(pages_title);
        } else {
            pages = pagesRepository.getAllPagesWithCategoryByUUID(pages_id);
        }
        if (pages.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages %s not exists", pages_id);
        }
        Map<String, Object> infoPage = getPagesInfo(List.of(pages_id)).getFirst();
        return ResponseEntity.ok(infoPage);
    }

    public ResponseEntity<?> createPages(PagesRequest pagesRequest) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        User user = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

        Optional<Category> categoryCheck = categoryRepository.findCategoryByName(pagesRequest.getCategory());
        if (categoryCheck.isPresent()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Pages with category %s - is exists", pagesRequest.getCategory()), null);
        }
        Optional<Pages> pageCheck = pagesRepository.getPagesByName(pagesRequest.getName());
        if (pageCheck.isPresent()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Pages %s - is exists", pagesRequest.getName()), null);
        }
        Category category = new Category(pagesRequest.getCategory(), user, dateWorker.getCurrentTimeISO());
        categoryRepository.save(category);

        Pages page = new Pages(
                category,
                roles.contains("ROLE_ADMIN"),
                pagesRequest.getName(),
                user,
                dateWorker.getCurrentTimeISO()
        );
        String description = pagesRequest.getDescription();
        if (description != null && !description.isEmpty()) {
            page.setDescriptionPages(description);
        }
        Pages infoCreated = pagesRepository.save(page);

        if (!pagesRequest.getItemsRequestList().isEmpty()) {
            List<Items> itemsForPage = new ArrayList<>();
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(infoCreated.getCategory());
            /* Если присутствует item проверяем наличие id или если его нет создаем новый item */
            for (ItemInPagesCreate itemRequest: pagesRequest.getItemsRequestList()) {
                if (itemRequest.getId() != null && !itemRequest.getId().isEmpty()) {
                    Optional<Items> itemCheck = itemsRepository.findByStringUUID(itemRequest.getId());
                    if (itemCheck.isEmpty()) return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Item %s not exists, check data", itemRequest.getId()), null);
                    Items existItems = itemCheck.get();
                    existItems.setCategory(categoryList);
                    Items newItem = itemsRepository.save(existItems);
                    itemsForPage.add(newItem);
                } else {
                    Items newItems = new Items(
                            itemRequest.getItemsName(),
                            roles.contains("ROLE_ADMIN"),
                            user,
                            dateWorker.getCurrentTimeISO()
                    );
                    newItems.setCategory(categoryList);
                    Items newItem = itemsRepository.save(newItems);
                    itemsForPage.add(newItem);
                }
            }
            infoCreated.setItems(itemsForPage);
            pagesRepository.save(infoCreated);
        }
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully create", String.valueOf(infoCreated.getId()));
    }

    public ResponseEntity<?> updatePages(String pagesId, PagesUpdate pagesUpdate) {
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pagesId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not found", pagesId);
        }
        Pages pages = pagesCheck.get();
        String newName = pagesUpdate.getName();
        if (newName != null && !newName.isEmpty()) {
            pages.setTitle(newName);
        }
        String newDesc = pagesUpdate.getDescription();
        if (newDesc != null && !newDesc.isEmpty()) {
            pages.setDescriptionPages(newDesc);
        }
        pagesRepository.save(pages);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully update", pagesId);
    }

    public ResponseEntity<?> searchPages(String word) {
        return ResponseEntity.ok(pagesRepository.searchTitlesByWord(word));
    }

    public List<Map<String, Object>> getPagesInfo(List<String> pagesList) {
        List<Map<String, Object>> readyList = new ArrayList<>();

        for (String pagesId: pagesList) {
            Pages pageEntity = pagesRepository.getStrickPagesByID(pagesId);
            String categoryId = pageEntity.getCategory().getId().toString();
            List<ItemsCategory> itemsId = itemsRepository.getItemsByCategory(categoryId);
            List<PagesItemsExistedRates> existedRates = pagesRepository.getExistedRatesFromItemsByCategoryAndPages(pagesId, categoryId);
            Map<String, Object> response = new HashMap<>();
            response.put("id", pagesId);
            response.put("title", pageEntity.getTitle());
            response.put("description", pageEntity.getDescriptionPages());
            response.put("category", new HashMap<String, Object>() {{
                put("id", pageEntity.getCategory().getId().toString());
                put("name", pageEntity.getCategory().getName());
            }});

            /* Получаем итемы */
            List<Map<String,Object>> itemsList = itemsId.stream().map(item -> {
                List<PagesItemsExistedRates> listFilter = existedRates.stream().filter(it -> Objects.equals(it.getItemId(), item.getItemId())).toList();
                Long countRates;
                if (!listFilter.isEmpty()) countRates = listFilter.getFirst().getCount();
                else countRates = 0L;
                Map<String,Object> mappedItems = new HashMap<>();
                mappedItems.put("name", item.getItemName());
                mappedItems.put("id", item.getItemId());
                mappedItems.put("desc", item.getDescription());
                mappedItems. put("count_rates", countRates);
                return mappedItems;
            }).toList();

            /* Сортируем итемы */
            List<Map<String, Object>> modifiableList = new ArrayList<>(itemsList);
            modifiableList.sort((item1, item2) -> {
                return Long.compare((long) item2.get("count_rates"), (long) item1.get("count_rates"));
            });

            response.put("items", modifiableList);
            readyList.add(response);
        }

        return readyList;
    }

}
