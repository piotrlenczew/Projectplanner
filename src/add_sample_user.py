#!/usr/bin/env python3

import requests

url = "http://localhost:3000/api/users/register"
data = {"name": "nazwa", "password": "pass"}

response = requests.post(url, json=data)

print("Status Code:", response.status_code)
print("Response:", response.text)
