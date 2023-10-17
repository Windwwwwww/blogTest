package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 更新标签
 * 相较AddTagDto增加id
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagDto {

    private Long id;

    //标签名
    private String name;

    //备注
    private String remark;
}
