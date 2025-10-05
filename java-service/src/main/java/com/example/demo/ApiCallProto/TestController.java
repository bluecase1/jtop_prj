package com.example.demo.ApiCallProto; // 실제 프로젝트 패키지명으로 변경

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {

    private final PythonApiCaller pythonApiCaller;

    @Autowired
    public TestController(PythonApiCaller pythonApiCaller) {
        this.pythonApiCaller = pythonApiCaller;
    }

    /**
     * 브라우저에서 이 경로로 접속하면 Python API를 호출합니다.
     * http://localhost:8080/test-python-api (Gitpod 공개 URL 사용)
     * @return Python API의 최종 응답 (JSON 문자열)
     */
    @GetMapping("/test-python-api")
    public String testPythonCall() {
        // 1. 파이썬으로 보낼 테스트 데이터 준비
        List<Double> testData = Arrays.asList(10.5, 20.2, 30.3, 40.0);
        
        // 2. Python API 호출
        String pythonResponse = pythonApiCaller.callPythonApi(testData);

        // 3. 최종 결과 반환
        return "Python API 호출 결과:\n" + pythonResponse;
    }
}
