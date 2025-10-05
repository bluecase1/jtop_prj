package com.example.demo.ApiCallFile; // 프로젝트의 테스트 패키지명에 맞게 변경

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc를 자동 설정합니다.
class ScriptUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 테스트용 파이썬 스크립트 내용 정의
    private static final String PYTHON_SCRIPT_CONTENT = 
        "import numpy as np\n" +
        "print('TEST_START')\n" +
        "print('Script output: 12345')\n" + 
        "print('TEST_END')";

    @Test
    void testUploadAndExecutePythonScript() throws Exception {
        
        // 1. Mock MultipartFile 객체 생성
        // 실제 파일을 업로드하는 것처럼 시뮬레이션합니다.
        MockMultipartFile pythonFile = new MockMultipartFile(
            "file", // 🚨 @RequestParam("file")와 이름이 일치해야 합니다.
            "test_upload.py", 
            "text/x-python", 
            PYTHON_SCRIPT_CONTENT.getBytes()
        );

        // 2. MockMvc를 사용하여 POST 요청 시뮬레이션
        mockMvc.perform(
                // /api/script/upload 엔드포인트에 Multipart 요청을 보냅니다.
                multipart("/api/script/upload")
                    .file(pythonFile)
            )
            // 3. 응답 검증 (HTTP 상태 코드 확인)
            .andExpect(status().isOk()) 
            // 4. 응답 본문 검증 (파이썬 실행 결과 확인)
            // 파이썬 스크립트가 출력한 내용이 응답에 포함되어 있는지 확인합니다.
            .andExpect(content().string(containsString("Script output: 12345")))
            .andExpect(content().string(containsString("File uploaded successfully")));
    }
}