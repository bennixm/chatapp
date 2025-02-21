
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install -r requirements.txt
RUN pip install bcrypt
RUN pip install openai==0.28


COPY . .

CMD ["python", "app.py"]
