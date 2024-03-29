package com.disk.service;

import com.disk.domain.bo.FileBo;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutionException;

public interface FileService {
    List<File> searchFile(FileBo fileBo) throws ExecutionException, InterruptedException, BrokenBarrierException;

    void searchFileExport(FileBo fileBo, HttpServletResponse response) throws InterruptedException, IOException;
}
