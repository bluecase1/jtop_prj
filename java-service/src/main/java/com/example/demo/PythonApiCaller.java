package com.example.demo; // 실제 프로젝트 패키지명으로 변경

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PythonApiCaller {

    // Spring의 HTTP 통신 클래스
    private final RestTemplate restTemplate;
    
    // Gitpod 워크스페이스 내에서 파이썬 서버로 접근하는 URL
    private static final String PYTHON_API_URL = "http://localhost:5000/api/calculate";

    @Autowired
    public PythonApiCaller(RestTemplateBuilder restTemplateBuilder) {
        // RestTemplate 인스턴스 초기화
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Python API에 데이터 리스트를 보내고 응답을 받습니다.
     * @param data 파이썬으로 보낼 데이터 (숫자 리스트)
     * @return 파이썬 서버의 응답 문자열 (JSON 형태)
     */
    public String callPythonApi(List<Double> data) {
        
        // 1. 요청할 JSON 데이터 구조화 (파이썬 Flask에서 'data' 키를 기대함)
        Map<String, List<Double>> requestBody = new HashMap<>();
        requestBody.put("data", data);

        try {
            // 2. POST 요청 보내기
            ResponseEntity<String> response = restTemplate.postForEntity(
                PYTHON_API_URL, 
                requestBody, 
                String.class // 응답 본문 타입을 문자열로 받음
            );

            // 3. 응답 상태 코드 확인 및 본문 반환
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Java Client: Python 서버로부터 응답 수신 성공.");
                return response.getBody();
            } else {
                return "Error calling Python API. Status: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Connection Error: Could not reach Python server (Is Flask running on port 5000?). Error: " + e.getMessage();
        }
    }
}
