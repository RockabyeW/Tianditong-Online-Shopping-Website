package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 查找所有category
     * @return
     */
    public List<Category> findAllCategory();
}
