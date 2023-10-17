package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 选择标签小菜单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTagVo {

    private Long id;
    //标签名
    private String name;
}
