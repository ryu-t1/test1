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
    //会社新規登録
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
            int totalPages) {
    }

    public CompanyPageResponse getPage(int page, int size) {
        if (page < 1)
            page = 1;
        if (size < 1)
            size = 10;

        int totalItems = companyRepository.countAll();
        int totalPages = (int) Math.ceil(totalItems / (double) size);

        int offset = (page - 1) * size;
        List<Company> items = companyRepository.findPage(size, offset);

        return new CompanyPageResponse(items, page, size, totalItems, totalPages);
    }

    // ★編集
    public Company update(int companyId, Company req) {
        if (companyId <= 0)
            throw new IllegalArgumentException("companyId が不正です");
        if (!StringUtils.hasText(req.getName()))
            throw new IllegalArgumentException("会社名は必須です");

        Company c = new Company();
        c.setCompanyId(companyId);
        c.setName(req.getName());
        c.setAddress(req.getAddress());
        c.setWebsite(req.getWebsite());

        int updated = companyRepository.update(c);
        if (updated == 0)
            throw new IllegalArgumentException("対象の会社が見つかりません");

        return companyRepository.findById(companyId);
    }

    // ★削除（紐づきがあるなら拒否）
    public void delete(int companyId) {
        if (companyId <= 0)
            throw new IllegalArgumentException("companyId が不正です");

        int cnt = companyRepository.countEventsByCompanyId(companyId);
        if (cnt > 0) {
            // 409 にしたいので Controller で受けて status を変える
            throw new IllegalStateException("この会社は説明会に使用されています（" + cnt + "件）。先に説明会を削除/変更してください。");
        }

        int deleted = companyRepository.deleteById(companyId);
        if (deleted == 0)
            throw new IllegalArgumentException("対象の会社が見つかりません");
    }

}
