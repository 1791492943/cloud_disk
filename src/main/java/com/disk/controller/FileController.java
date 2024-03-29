package com.disk.controller;

import com.disk.common.R;
import com.disk.domain.bo.FileBo;
import com.disk.service.FileService;
import com.disk.utils.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutionException;

/**
 * 文件 Controller
 */

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 搜索文件
     */
    @PostMapping("/search")
    public R<List<File>> searchFile(@RequestBody FileBo fileBo) throws ExecutionException, InterruptedException, BrokenBarrierException {
        fileBo.setMaxShowNum(FileUtil.MAX_SHOW_NUM);
        List<File> files = fileService.searchFile(fileBo);
        return R.ok(files);
    }

    /**
     * 搜索文件 (导出)
     */
    @PostMapping("/searchExport")
    public void searchFileExport(@RequestBody FileBo fileBo, HttpServletResponse response) throws IOException, InterruptedException {
        fileBo.setMaxShowNum(Integer.MAX_VALUE);
        fileService.searchFileExport(fileBo,response);
    }
}
