package com.example.blogsystemtest.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.CategoryListDto;
import com.example.blogsystemtest.domain.dto.PageDto;
import com.example.blogsystemtest.domain.entity.Category;
import com.example.blogsystemtest.domain.vo.AllCategoryVo;
import com.example.blogsystemtest.domain.vo.CategoryVo;
import com.example.blogsystemtest.domain.vo.ExcelStudentVo;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.service.CategoryService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


@RestController("admin-category-controller")
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult getListAllCategory(){
        List<AllCategoryVo> list = categoryService.getListAllCategory();
        return ResponseResult.okResult(list);
    }

    @PostMapping("/list")
    public ResponseResult pageCategoryList(@RequestBody PageDto pageDto){
        return categoryService.pageCategoryList(pageDto.getPageNum(),pageDto.getPageSize(),(CategoryListDto) pageDto.getObject());
    }



    @PostMapping("/add")
    public ResponseResult addCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.addCategory(categoryVo);
    }

    @GetMapping("/get/{id}")
    public ResponseResult getCategory(@PathVariable Long id){
        return categoryService.getCategory(id);
    }

    @PutMapping("/update")
    public ResponseResult updateCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.updateCategory(categoryVo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }
}
