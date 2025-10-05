package com.example.demo.ApiCallFile;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@RestController
@RequestMapping("/api/script")
public class ScriptUploadController {

    // 파일을 저장할 서버의 임시 디렉토리 경로
    private static final String SCRIPT_DIR = "/tmp/python_scripts/"; 

    @PostMapping("/upload")
    public String uploadScript(@RequestParam("file") MultipartFile file) throws IOException {
        
        // 1. 디렉토리 생성 확인
        File directory = new File(SCRIPT_DIR);
        if (!directory.exists()) {
            directory.mkdirs(); 
        }

        // 2. 파일 저장 경로 설정
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            return "Error: File name is missing.";
        }
        Path filePath = Paths.get(SCRIPT_DIR, filename);

        // 3. 파일 저장
        file.transferTo(filePath.toFile());
        
        // 4. 저장된 파일 실행을 서비스에 위임
        // (이후 2단계에서 구현할 실행 로직)
        // String result = scriptExecutionService.execute(filePath.toString());

        return "File uploaded successfully: " + filename; // + "\nExecution Result: " + result;
    }
}