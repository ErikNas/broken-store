from check_swear import SwearingCheck
from flask import Flask, request, jsonify

app = Flask(__name__)
ru_profanity_validator = SwearingCheck()

@app.route('/validate-profanity', methods=['POST'])
def validate_profanity():
    data = request.get_json()
    if data:
        phrase = data.get('phrase')
        predict = ru_profanity_validator.predict(phrase, 0.2)[0]
        predict_proba = ru_profanity_validator.predict_proba(phrase)[0]
        return jsonify({"predict": predict, "predict_proba": predict_proba})
    else:
        return jsonify({'error': 'Invalid JSON data: phrase not specified'}), 422

if __name__ == '__main__':
    app.run()
