# Rooio [![Build Status](https://dev.azure.com/CPSECapstone/Rooio/_apis/build/status/Repairs%20Application%20-%20CI?branchName=master)](https://dev.azure.com/CPSECapstone/Rooio/_build/latest?definitionId=7&branchName=master) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CPSECapstone_Rooio&metric=alert_status)](https://sonarcloud.io/dashboard?id=CPSECapstone_Rooio)

To clone this repository, use the clone or download button to get a URL or SSH key. Then use the following command in the directory you would like to clone it, replacing <URL/SSH> with the actual URL/SSH key of the repository.
```
git clone <URL/SSH>
```
## Running the application
Click [here](https://developer.android.com/studio) to download the most recent version of Android Studio. Then, open an existing project and choose the Repairs directory from your newly cloned directory, which Android Studio should recognize as a project. The application should build with a default Android configuration set up, which will exclude any Clover functionality.

## Setting up the emulator
Follow the instructions [here](https://docs.clover.com/clover-platform/docs/setting-up-an-android-emulator) to set up the Clover emulator.
The information below consists of helpful tips to reference while going through the Clover documented instructions. The section titles and step numbers coordinate with the Clover documentation.

### Steps 1 and 2: Downloading Clover hardware profiles:
> For this step you cannot simply download the file to your computer, as it is a text box. You need to copy and paste clover-station- 2018.xml to a new file on your computer. This file will be needed for creating the emulator profile. This file can also be found in the git repository at Rooio/Repairs/emulator along with any other necessary downloads.

### Steps 3 and Step 5: Creating a new emulator profile:
> The Clover hardware profile may not appear at first. Just continue refreshing the page or close and reopen Android Studio. Make sure you are under the Tablet section when looking for the added Clover profile.

### Step 4: Sideloading required Clover APKs using ADB
> The ‘adb devices’ command probably won’t work at first. You need to find the path where your adb is located. 

#### Mac Users
> These two commands will most likely work for Mac users, make sure you change “username” to your username. Also type in these commands in the same order as presented below.

```
export ANDROID\_HOME=/Users/username/Library/Android/sdk 
```
```
export PATH=\$\{PATH\}:\$ANDROID\_HOME/tools:\$ANDROID\_HOME/platform-tools 
```

> Then run the emulator, and while it is running type the following into the Android Studio terminal.
```
adb devices.
```
> While the emulator is running, do steps 5 and 6 of the installation commands.

#### Windows Users
> This is the directory where adb.exe is located typically. Search in other directories if it cannot be found, and add the path to your environment variables under Path.
```
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools
```
> Then run the emulator, and while it is running type the following into the Android Studio terminal. 
```
adb devices
```
> While the emulator is running, do steps 5 and 6 of the installation commands.

## Testing the application

Once the emulator is working, the application can be built and installed by clicking the green run button. By right clicking on a runnable class, such a unit test class, it is possible to check for failing tests based on modules as well as coverage for unit tests.  To create a combined code coverage report, the following command can be used in the Android Studio terminal.
```
gradlew connectedDebugAndroidTest jacocoTestReport
```
This will create individual unit and UI test reports at the following location.
```
Rooio\Repairs\app\build\reports\tests\testDebugUnitTest
Rooio\Repairs\app\build\reports\androidTests\connected
```
Finally, there will be a code coverage report at the following location.
```
Rooio\Repairs\app\build\reports\jacoco\jacocoTestReport\html\index.html
```
To create a debug APK, use the following command.
```
gradlew assembleDebug
```
This will create an APK that is for debugging at the following location.
```
Rooio\Repairs\app\build\outputs\apk\debug\app-debug.apk
```

## Continuous Integration
Any changes should be made on a branch and not committed directly to master, as any commit to master triggers a CI build in Azure DevOps. This causes all of the unit tests to be ran and creates a code coverage report of the entire project. The status of the most recent build can be seen with the Azure badge. The Azure badge can also be used to access the project pipeline, which contains information pertaining to specific tests and code coverage of the build.
