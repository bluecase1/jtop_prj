package com.example.demo.ApiCallFile; // 실제 프로젝트 패키지명으로 변경해야 합니다.

import com.example.demo.ApiCallFile.ScriptExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/script")
public class ScriptUploadController {

    private final ScriptExecutionService scriptExecutionService;
    
    // 파일을 저장할 서버의 임시 디렉토리 경로 (Gitpod 환경 기준)
    private static final String SCRIPT_DIR = "/tmp/python_scripts/"; 

    @Autowired
    public ScriptUploadController(ScriptExecutionService scriptExecutionService) {
        this.scriptExecutionService = scriptExecutionService;
    }

    @PostMapping("/upload")
    public String uploadScript(@RequestParam("file") MultipartFile file) throws IOException {
        
        // 1. 디렉토리 생성 확인
        File directory = new File(SCRIPT_DIR);
        if (!directory.exists()) {
            directory.mkdirs(); 
        }

        // 2. 파일명 유효성 검사 및 경로 설정
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            return "Error: File name is missing.";
        }
        Path filePath = Paths.get(SCRIPT_DIR, filename);

        try {
            // 3. 파일 저장
            file.transferTo(filePath.toFile());

            // 4. 저장된 파일 실행을 서비스에 위임하고 결과를 반환
            String result = scriptExecutionService.execute(filePath.toString());

            // 5. 파이썬 실행 결과를 최종 응답에 포함하여 반환
            return "File uploaded successfully: " + filename + "\nExecution Result:\n" + result;

        } catch (IOException e) {
            // 파일 저장 중 발생한 오류 처리
            return "File storage failed: " + e.getMessage();
        }
    }
}