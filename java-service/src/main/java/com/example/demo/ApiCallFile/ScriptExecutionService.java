package com.example.demo.ApiCallFile; // 실제 프로젝트 패키지명으로 변경해야 합니다.

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class ScriptExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(ScriptExecutionService.class);

    /**
     * 지정된 경로의 파이썬 스크립트를 외부 프로세스로 실행하고 결과를 반환합니다.
     * @param scriptPath 실행할 파이썬 파일의 절대 경로
     * @return 파이썬 스크립트의 표준 출력(stdout) 또는 오류 메시지
     */
    public String execute(String scriptPath) {
        
        StringBuilder output = new StringBuilder();
        
        try {
            logger.info("Starting Python script execution: {}", scriptPath);
            
            // 1. ProcessBuilder 설정: 'python3' 명령어와 실행할 스크립트 경로를 지정
            ProcessBuilder pb = new ProcessBuilder("python3", scriptPath);
            
            // 2. 프로세스 시작
            Process p = pb.start();
            
            // 3. 파이썬 스크립트의 표준 출력(stdout) 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                int lineNum = 1;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    logger.info("**** line[{}] : {}", lineNum, line);
                }
            }
            
            // 4. 프로세스가 종료될 때까지 대기하고 종료 코드(Exit Code)를 받음
            int exitCode = p.waitFor(); 
            logger.info("Python script finished with Exit Code: {}", exitCode); 

            if (exitCode != 0) {
                // 5. 실행 오류(stderr)가 발생했을 경우
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
                    String errorOutput = errorReader.lines().collect(Collectors.joining("\n"));
                    logger.error("Python script error detected. Full error output:\n{}", errorOutput);
                    
                    // 파이썬 오류 코드를 반환하여 디버깅에 도움
                    return "Python Execution Failed (Exit Code " + exitCode + "): " + errorOutput;
                }
            }

        } catch (IOException e) {
            logger.error("System process execution failed (IOException). Check if 'python3' is in PATH.", e);
            return "Execution failed (System Error): " + e.getMessage();
        } catch (InterruptedException e) {
            logger.error("Process waiting was interrupted.", e);
            Thread.currentThread().interrupt();
            return "Execution interrupted: " + e.getMessage();
        }

        // 6. 성공 시, 파이썬 스크립트의 표준 출력(stdout) 결과를 반환
        logger.info("Python script output (stdout) successfully captured.");
        return output.toString(); 
    }
}