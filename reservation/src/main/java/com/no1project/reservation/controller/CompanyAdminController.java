package com.no1project.reservation.controller;

import com.no1project.reservation.model.Company;
import com.no1project.reservation.service.CompanyService;
import com.no1project.reservation.service.CompanyService.CompanyPageResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/companies")
public class CompanyAdminController {

    private final CompanyService companyService;

    public CompanyAdminController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Company company) {
        try {
            Company created = companyService.create(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public CompanyPageResponse list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return companyService.getPage(page, size);
    }
}
