package com.no1project.reservation.service;

import com.no1project.reservation.model.Company;
import com.no1project.reservation.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company create(Company company) {
        if (!StringUtils.hasText(company.getName())) {
            throw new IllegalArgumentException("会社名は必須です");
        }

        int newId = companyRepository.insert(company);
        company.setCompanyId(newId);
        return company;
    }


public record CompanyPageResponse(
        List<Company> items,
        int page,
        int size,
        int totalItems,
        int totalPages
) {}

public CompanyPageResponse getPage(int page, int size) {
    if (page < 1) page = 1;
    if (size < 1) size = 10;

    int totalItems = companyRepository.countAll();
    int totalPages = (int) Math.ceil(totalItems / (double) size);

    int offset = (page - 1) * size;
    List<Company> items = companyRepository.findPage(size, offset);

    return new CompanyPageResponse(items, page, size, totalItems, totalPages);
}

}
