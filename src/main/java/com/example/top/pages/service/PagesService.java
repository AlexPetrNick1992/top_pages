package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Pages;
import com.example.top.pages.payload.request.PagesRequest;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.payload.response.ResponseSinglePages;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.PagesRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagesService {

    private final PagesRepository pagesRepository;
    private final ResponseEntityAppResponse responseEntityAppResponse;
    private final CategoryRepository categoryRepository;

    public List<Pages> getPagesList() {
        return pagesRepository.findAll();
    }

    public ResponseEntity<?> deletePages(String pagesId) {
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pagesId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not exists", pagesId);
        } else {
            Pages page = pagesCheck.get();
            pagesRepository.delete(page);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully deleted", String.valueOf(page.getId()));
        }
    }

    public ResponseEntity<?> approvePages(String pageId) {
        Optional<Pages> pagesCheck = pagesRepository.findByUUIDString(pageId);
        if (pagesCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages not exists", pageId);
        } else {
            Pages page = pagesCheck.get();
            if (page.isApproved()) { return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages already approved", pageId); }
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
            if (!page.isApproved()) { return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages already disprove", pageId); }
            page.setApproved(false);
            pagesRepository.save(page);
            return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully disprove", null);
        }
    }

    public ResponseEntity<?> getSinglePage(String pages_id, String pages_name) {
        Optional<Pages> pages;
        if (pages_id == null && pages_name == null) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Request need pages_id or pages_name", null);
        }
        if (pages_id == null) {pages_id = "";}
        if (pages_id.isEmpty()) {pages = pagesRepository.getPagesByName(pages_name);
        } else { pages = pagesRepository.findByUUIDString(pages_id);}
        if (pages.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Pages %s not exists", pages_id);
        } else {
            return ResponseEntity.ok(new ResponseSinglePages(pages.get()));
        }
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
        Pages page = new Pages(category, roles.contains("ROLE_ADMIN"), pagesRequest.getName());
        String description = pagesRequest.getDescription();
        if (description != null && !description.isEmpty()) {page.setDescription(description);}
        Pages infoCreated = pagesRepository.save(page);
        System.out.println(infoCreated);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Pages successfully create", String.valueOf(infoCreated.getId()));
    }

}
