package com.example.blogsystemtest.controller.admin;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.MenuDto;
import com.example.blogsystemtest.domain.vo.MenuListVo;
import com.example.blogsystemtest.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(MenuDto menuDto){
        return menuService.list(menuDto);
    }

    @PostMapping("/add")
    public ResponseResult addMenu(@RequestBody MenuListVo menuListVo){
        return menuService.addMenu(menuListVo);
    }

    @GetMapping("/get/{id}")
    public ResponseResult getMenu(@PathVariable Long id){
        return menuService.getMenu(id);
    }

    @PutMapping("/update")
    public ResponseResult updateMenu(@RequestBody MenuListVo menuListVo){
        return menuService.updateMenu(menuListVo);
    }

    @DeleteMapping("/delete/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        return menuService.deleteMenu(menuId);
    }
}
