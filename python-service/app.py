from flask import Flask, jsonify, request
import numpy as np

app = Flask(__name__)

@app.route('/api/calculate', methods=['POST'])
def calculate_endpoint():
    try:
        data = request.json
        input_list = data.get('data', [])
        
        # 1. C-기반 라이브러리 (NumPy)를 사용한 처리
        numbers = np.array(input_list, dtype=float)
        sum_result = np.sum(numbers)
        
        return jsonify({
            'status': 'OK',
            'sum_result': float(sum_result),
            'processed_by': 'Python Server'
        }), 200
    except Exception as e:
        return jsonify({'status': 'Error', 'message': str(e)}), 500

if __name__ == '__main__':
    # 0.0.0.0 바인딩은 컨테이너 환경에서 필수입니다.
    # .gitpod.yml에서 정의한 포트(5000)를 사용합니다.
    app.run(host='0.0.0.0', port=5000)
