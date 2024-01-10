#!/usr/bin/env python3

import requests

url = "http://localhost:3000/api/users/update/352"
data = {
    "name": "kicia2"
}

response = requests.put(url, json=data)

print("Status Code:", response.status_code)
print("Response:", response.text)
