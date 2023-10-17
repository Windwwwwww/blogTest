package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.CategoryListDto;
import com.example.blogsystemtest.domain.entity.Category;
import com.example.blogsystemtest.domain.vo.AllCategoryVo;
import com.example.blogsystemtest.domain.vo.CategoryVo;

import java.util.List;



public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();


    List<AllCategoryVo> getListAllCategory();

    ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult addCategory(CategoryVo categoryVo);

    ResponseResult getCategory(Long id);

    ResponseResult updateCategory(CategoryVo categoryVo);

    ResponseResult deleteCategory(Long id);
}
