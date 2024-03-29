package com.disk.service.impl;

import com.disk.domain.bo.FileBo;
import com.disk.service.FileService;
import com.disk.utils.SimpleLatch;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public List<File> searchFile(FileBo fileBo) throws InterruptedException {
        List<File> fileList = Collections.synchronizedList(new ArrayList<>());
        SimpleLatch latch = new SimpleLatch(1);
        executorService.submit(() -> searchFile(fileBo, fileBo.getCurrentPath(), fileList, latch));
        latch.await();
        return fileList;
    }

    @Override
    public void searchFileExport(FileBo fileBo, HttpServletResponse response) throws InterruptedException, IOException {
        List<File> fileList = this.searchFile(fileBo);
        String fileName = "exported_files.txt"; // 设置导出文件名

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (ServletOutputStream outputStream = response.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            fileList.forEach(f -> {
                try {
                    writer.write(f.getAbsolutePath());
                    writer.newLine();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private void searchFile(FileBo fileBo, String currentPath, List<File> fileList, SimpleLatch latch) {
        try {
            File[] files = currentPath == null ? File.listRoots() : new File(currentPath).listFiles();
            if (files == null) {
                latch.countDown();
                return;
            }

            for (File file : files) {
                if (fileList.size() >= fileBo.getMaxShowNum()) {
                    latch.awakenNow();
                    return;
                }
                if (file.getName().contains(fileBo.getFileName())) {
                    fileList.add(file);
                }
                if (file.isDirectory()) {
                    // 增加计数
                    latch.countUp();
                    executorService.submit(() -> searchFile(fileBo, file.getAbsolutePath(), fileList, latch));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }

    }

}
