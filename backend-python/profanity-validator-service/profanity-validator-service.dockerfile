FROM python:3.11-slim-bookworm

WORKDIR /app
COPY ./backend-python/profanity-validator-service /app/.

RUN pip install -r requirements.txt

CMD ["gunicorn", "--bind", "0.0.0.0:8080", "profanity-validator-controller:app"]
