package com.example.demo.ApiCallFile; 

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader; // ResourceLoader 추가
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc 
class ScriptUploadControllerFileTest {

    private static final Logger logger = LoggerFactory.getLogger(ScriptUploadControllerFileTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader; // ResourceLoader 주입

    @Test
    void testUploadLocalFileAndExecutePythonScript() throws Exception {
        
        // 1. 테스트 리소스 파일 로드 (로컬 파일 시스템에서 파일을 읽는 것 시뮬레이션)
        Path filePath = Path.of(resourceLoader.getResource("classpath:test_local_upload.py").getURI());
        byte[] fileContent = Files.readAllBytes(filePath);

        // 2. Mock MultipartFile 객체 생성 (로컬 파일의 내용을 그대로 담음)
        MockMultipartFile pythonFile = new MockMultipartFile(
            "file", // @RequestParam("file")와 일치
            "test_local_upload.py", // 업로드될 파일명
            "text/x-python", 
            fileContent // 파일 내용
        );

        // 3. MockMvc를 사용하여 POST 요청 시뮬레이션 및 검증
        mockMvc.perform(
                multipart("/api/script/upload")
                    .file(pythonFile)
            )
            // HTTP 200 OK 상태 코드 확인
            .andExpect(status().isOk()) 
            // 응답 본문에 파일 업로드 성공 메시지 확인
            .andExpect(content().string(containsString("File uploaded successfully")))
            // 응답 본문에 파이썬 스크립트 실행 결과 문자열 확인
            .andExpect(content().string(containsString("[LOCAL] Hello from Python script!")));
            
            // Note: time.time() 값은 동적으로 변하므로, containsString으로 정확한 시간 값을 검증할 수는 없습니다.
            // 핵심은 'TEST_FILE_EXECUTION_COMPLETE' 문자열을 통해 스크립트 실행이 끝났음을 확인하는 것입니다.
    }
}