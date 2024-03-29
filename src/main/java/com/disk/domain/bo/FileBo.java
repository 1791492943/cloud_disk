package com.disk.domain.bo;

import lombok.Data;

@Data
public class FileBo {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 当前路径
     */
    private String currentPath;

    /**
     * 最大展示数量
     */
    private Integer maxShowNum;

}
