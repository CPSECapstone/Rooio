import sys
import base64
import os.path
import requests
import json

# Get the arguments for the script
teamProject=sys.argv[1]
authToken=sys.argv[2]
buildUri=sys.argv[3]
organization=sys.argv[4]

baseUrl = f"https://dev.azure.com/{organization}/{teamProject}/_apis"
header = {"content-type": "application/json; charset=utf-8", "Authorization": f"Basic {authToken}"}

# Get runId for current build
api = f'{baseUrl}/test/runs?buildUri={buildUri}&api-version=5.1'
response = requests.get(api, headers=header)
print("GET", api, "-->", response.status_code)
runId = json.loads(response.text)['value'][0]['id']

# Get failed tests and upload screenshot
api = f'{baseUrl}/test/runs/{runId}/results?api-version=5.1&outcomes=3'
response = requests.get(api, headers=header)
print("GET", api, "-->", response.status_code)
failures = json.loads(response.text)['value']
for failedTest in failures:
    resultId = failedTest['id']
    className = failedTest['automatedTestStorage']
    testName = failedTest['testCaseTitle']
    imgPath = "./app/build/reports/androidTests/connected/screenshots/failures/" + className + "/" + testName + ".png"
    if os.path.isfile(imgPath):
        print("Screenshot exists, will upload: ", imgPath)
        with open(imgPath, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
            api = f'{baseUrl}/test/runs/{runId}/results/{resultId}/attachments?api-version=5.1-preview.1'
            data = {'stream': encoded_string.decode('utf-8'), 'fileName': f'{testName}.png',
                    'comment': 'Uploaded by REST from pipeline', 'attachmentType': 'GeneralAttachment'}
            response = requests.post(api, headers=header, json=data)
            print("POST", api, "-->", response.status_code)
