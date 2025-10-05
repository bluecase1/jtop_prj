import requests
import json

# Flask 서버가 실행 중인 주소
# Colab에서 실행 중이라면 일반적으로 localhost 또는 127.0.0.1을 사용합니다.
# 만약 ngrok 등을 사용하여 외부로 노출했다면 해당 주소를 사용해야 합니다.
# 현재 코드는 Colab 내부에서 실행된다고 가정하고 127.0.0.1을 사용합니다.
url = 'http://127.0.0.1:5000/api/calculate'

# 테스트를 위한 데이터
data = {'data': [1, 2, 3, 4, 5]}

# POST 요청 보내기
response = requests.post(url, json=data)

# 응답 출력
print(f"상태 코드: {response.status_code}")
print(f"응답 본문: {response.json()}")