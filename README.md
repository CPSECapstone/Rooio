# Rooio [![Build Status](https://dev.azure.com/CPSECapstone/Rooio/_apis/build/status/Repairs%20Application%20-%20CI?branchName=master)](https://dev.azure.com/CPSECapstone/Rooio/_build/latest?definitionId=7&branchName=master)

To clone this repository, click the clone or download button, and use git clone to create the local repository.

Running the application
-----------------------
Go to https://developer.android.com/studio to download the most recent version of Android Studio. Then, open an existing project and choose the Repairs directory in your newly cloned directory, which Android Studio should recognize as a project. The Gradle build script is included in this repository and the application should build with a default Android configuration set up, which will exclude any Clover functionality.

Setting up the emulator
-----------------------
Follow the instructions on the link below to set up the Clover emulator
https://docs.clover.com/clover-platform/docs/setting-up-an-android-emulator.
The information below consists of helpful tips to reference while going through the Clover documented instructions. The section titles and step numbers coordinate with the Clover documentation. 

**Steps 1 and 2: Downloading Clover hardware profiles:**
For this step you cannot simply download the file to your computer, as it is a text box. You need to copy and paste clover-station-2018.xml to a new file on your computer. This file will be needed for creating the emulator profile.
This file can also be found in the git repository at Rooio/Repairs/emulator along with any other necessary downloads.

**Step 3 and Step 5: Creating a new emulator profile:**
The Clover hardware profile may not appear at first. Just continue refreshing the page or close and reopen Android Studio. Make sure you are under the Tablet section when looking for the added Clover profile.

**Step 4: Sideloading required Clover APKs using ADB**
The ‘adb devices’ command probably won’t work at first. You need to find the path where your adb is located. 

**Mac Users**
These two commands will most likely work for Mac users, make sure you change “username” to your username. Also type in these commands in the same order as presented below.

export ANDROID\_HOME=/Users/username/Library/Android/sdk 

export PATH=\$\{PATH\}:\$ANDROID\_HOME/tools:\$ANDROID\_HOME/platform-tools 

Then run the emulator, and while it is running type into the Android Studio terminal: adb devices.
Also while the emulator is running, do step 5 and 6 of the installation commands.

**Windows Users**
The path to the adb is usually user\textbackslash AppData\textbackslash Local\textbackslash Android\textbackslash Sdk\textbackslash platform-tools, which is the folder where the adb.exe is located. Add this path to your environment variables, or search in folders to find the exact path to the adb.

Then run the emulator, and while it is running type into the Android Studio terminal: adb devices.
Also while the emulator is running, do step 5 and 6 of the installation commands.

**Continuous Integration**
After setting up the Clover emulator inside Android studio, the application should be able to run with full functionality. Any changes should be done on a branch and not directly to master.

Any commit to master triggers a CI build in Azure DevOps. The status of the most recent build can be seen on the GitHub repository, and the Azure badge can be used to access the project pipelines.
