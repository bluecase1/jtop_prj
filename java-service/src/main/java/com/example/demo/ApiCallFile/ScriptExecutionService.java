package com.example.demo.ApiCallFile;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ScriptExecutionService {

    public String execute(String scriptPath) {
        StringBuilder output = new StringBuilder();
        
        try {
            // 1. ProcessBuilder 설정: 'python3'로 스크립트 파일을 실행하도록 명령 구성
            ProcessBuilder pb = new ProcessBuilder("python3", scriptPath);
            
            // 2. 프로세스 시작
            Process p = pb.start();
            
            // 3. 파이썬 스크립트의 표준 출력(stdout) 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // 4. 프로세스가 종료될 때까지 대기
            int exitCode = p.waitFor(); 

            if (exitCode != 0) {
                // 5. 실행 오류(stderr)가 발생했을 경우
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String errorLine;
                StringBuilder errorOutput = new StringBuilder("Python Execution Error (Exit Code " + exitCode + "):\n");
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
                return errorOutput.toString();
            }

        } catch (IOException | InterruptedException e) {
            return "Execution failed due to system error: " + e.getMessage();
        }

        return output.toString();
    }
}