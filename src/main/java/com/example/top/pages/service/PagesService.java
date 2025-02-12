package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.Pages;
import com.example.top.pages.models.Rate;
import com.example.top.pages.payload.request.PagesRequest;
import com.example.top.pages.payload.request.PagesUpdate;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.payload.response.ResponseSinglePages;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.PagesRepository;
import com.example.top.pages.repository.models.PagesCategory;
import com.example.top.pages.repository.models.PagesItems;
import com.example.top.pages.repository.models.ReturnCountRates;
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

    public Map<String, Object> transformPagesListToDict(List<PagesCategory> listObject) {
        Map<String, Object> mappedList = new HashMap<>();

        List<PagesCategory> temp = listObject.stream().peek(pages -> mappedList.put(
                pages.getPagesId(),
                new HashMap<>() {{
                    put("title", pages.getTitle());
                    put("description_pages", pages.getDescriptionPages());
                    put("isApproved", pages.getIsApproved());
                    put("category", new HashMap<>() {{
                        put("id", pages.getCategoryId());
                        put("name", pages.getNameCategory());
                    }});
                }}
        )).toList();
        log.debug(temp.toString());
        return mappedList;
    }

    public Map<String, Object> parsedCountRatesItems(List<ReturnCountRates> itemRates) {
        Map<String, Object> mappedList = new HashMap<>();
        List<ReturnCountRates> temp = itemRates.stream().peek(items -> mappedList.put(
                items.getId(),
                new HashMap<String, Object>() {{
                    put("count_rates", items.getCount());
                    put("name", items.getName());
                    put("desc", items.getDescription());
                }}
        )).toList();
        log.debug(temp.toString());
        return mappedList;
    }

    public Map<String, List<String>> mappedItemsByPage(List<PagesItems> pagesItems) {
        Map<String, List<String>> groupedItems = new HashMap<>();
        List<PagesItems> temp = pagesItems.stream().peek(items -> {
            if (groupedItems.containsKey(items.getPagesId())) {
                List<String> tempList = new ArrayList<>(groupedItems.get(items.getPagesId()));
                tempList.add(items.getItemsId());
                groupedItems.put(items.getPagesId(), tempList);
            } else {
                groupedItems.put(items.getPagesId(), Collections.singletonList(items.getItemsId()));
            }
        }).toList();
        log.debug(temp.toString());
        return groupedItems;
    }

    public List<HashMap<Object, Object>> getPagesInfo(List<PagesCategory> listPages) {
        List<String> pagesListIds = listPages.stream().map(PagesCategory::getPagesId).toList();
        /* Получаем pages */
        Map<String, Object> pagesParsed = transformPagesListToDict(listPages);
        System.out.println(pagesParsed);
        /* Получаем pages_items */
        List<PagesItems> pagesItemsList = pagesRepository.getItemsPages(pagesListIds);
        Map<String, List<String>> groupedItemsByPages = mappedItemsByPage(pagesItemsList);
        /* Получаем items count rates */
        List<ReturnCountRates> itemsRates = itemsRepository.getCountRatesByItemsAll(
                pagesItemsList.stream().map(PagesItems::getItemsId).toList()
        );
        Map<String, Object> parsedCounts = parsedCountRatesItems(itemsRates);
        return pagesListIds.stream().map(pages -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> pageInfo = (Map<String, Object>) pagesParsed.get(pages);
            List<String> itemsByPages;
            if (groupedItemsByPages.get(pages) == null) itemsByPages = new ArrayList<>();
            else { itemsByPages = groupedItemsByPages.get(pages); }
            return new HashMap<>() {{
                put("id", pages);
                put("title", pageInfo.get("title"));
                put("description", pageInfo.get("description_pages"));
                put("category", pageInfo.get("category"));
                put("items", itemsByPages.stream().map(parsedCounts::get).toList());
            }};
        }).toList();
    }

    public ResponseEntity<?> getPagesList() {
        List<PagesCategory> pagesList = pagesRepository.getAllPagesWithCategory();
        return ResponseEntity.ok(getPagesInfo(pagesList));
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
        Collection<Category> categoriesList = item.getCategory();
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
        if (pages_id == null) {pages_id = "";}
        if (pages_id.isEmpty()) {pages = pagesRepository.getAllPagesWithCategoryByTitle(pages_title);
        } else {pages = pagesRepository.getAllPagesWithCategoryByUUID(pages_id);}
        if (pages.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages %s not exists", pages_id);
        }
        PagesCategory originalPages = pages.get();
        HashMap<Object, Object> infoPage = getPagesInfo(List.of(originalPages)).getFirst();
        return ResponseEntity.ok(infoPage);
    }

    private List<Items> filterItems(List<Items> itemsList, String mode, String type) {
        List<Items> filterItems = itemsList;
        /* Фильтруем Rates isApproved*/
        if (!Objects.equals(mode, "full")) {
            /* Убираем все неподтвержденные items */
            filterItems = filterItems.stream().filter(Items::getIsApproved).toList();
            /* Убираем все неподтвержденные rates */
            filterItems = filterItems.stream().peek(item -> item.setRate(item.getRate().stream().filter(Rate::isApproved).toList())).toList();
        }
        /* Фильтруем на позитивный и негативный */
        if (type != null) {
            if (Objects.equals(type, "negative")) {
                filterItems = filterItems.stream().peek(item -> item.setRate(item.getRate().stream().filter(rate -> !rate.isPositive()).toList())).toList();
            } else {
                filterItems = filterItems.stream().peek(item -> item.setRate(item.getRate().stream().filter(Rate::isPositive).toList())).toList();
            }
        }
        return filterItems;
    }

    public ResponseEntity<?> createPages(PagesRequest pagesRequest) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        Optional<Category> categoryCheck = categoryRepository.findCategoryByName(pagesRequest.getCategory());
        if (categoryCheck.isPresent()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Pages with category %s - is exists", pagesRequest.getCategory()), null);
        }
        Optional<Pages> pageCheck = pagesRepository.getPagesByName(pagesRequest.getName());
        if (pageCheck.isPresent()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Pages %s - is exists", pagesRequest.getName()), null);
        }
        Category category = new Category(pagesRequest.getCategory());
        categoryRepository.save(category);

        Pages page = new Pages(category, roles.contains("ROLE_ADMIN"), pagesRequest.getName());
        String description = pagesRequest.getDescription();
        if (description != null && !description.isEmpty()) {
            page.setDescriptionPages(description);
        }
        Pages infoCreated = pagesRepository.save(page);
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
}
