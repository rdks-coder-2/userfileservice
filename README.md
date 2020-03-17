# ExtraHop Coding Exercise - User File Service
by Ramandeep Kaur

## Overview

This app is a Spring Boot and Maven based web application written in Java 8. It takes as input a user info file with a format same as a linux system's `/etc/passwd` file (explained here: ` https://en.wikipedia.org/wiki/Passwd#Password_file`). It uses this file to perform the following two operations via separate REST APIs:
* Get detailed information about a user
* Change a user's home directory or shell

The application runs on Spring's inbuilt web server, so a separate web server to deploy this application is not required.


## Assumptions
- The user info file is not huge
- User password is not updated


## Prerequisites
- Java version 1.8 or above for compiling and running the application. (1.8.0_131 precise Java version).
- Maven version 3.x if you want to compile the source code.
- The application runs on port 8080, so please make sure this port is free.

## Usage

1. The `userfileservice.zip` bundle has the source code, the compiled jar file, sample user info file and this README file inside it.
2. Unzip the bundle `userfileservice.zip`
3. You will find the following files and directories inside it:
- *src* - This contains all the source code
- *target* - This is where the compiled .jar file goes
- *userinfo.txt* - This is the sample user info file
- *userfileservice-0.0.1-SNAPSHOT.jar* - This a pre-compiled jar file that I have copied from the target folder and kept here at the root for the ease of access. In ideal scenarios, the jar file present inside the target folder is used since that has the most up to date code changes
- *pom.xml* - This is the maven pom.xml file
- *README.md* - This README file``

4. The application is run by giving the following command:
`java -jar userfileservice-0.0.1-SNAPSHOT.jar userinfo.txt` where
   -  `userfileservice-0.0.1-SNAPSHOT.jar` is the name of the application package. The general maven packaging version convention has been followed here, and hence the version number `0.0.1-SNAPSHOT` exists in the name of the jar file. 
   - `userinfo.txt` is the input userinfo.txt
   - The input file has been included in the package as a sample. You can change the name and location of these files as per your choice.
   
The application prints the actions performed (and errors, if any) on the console as it runs. There are a number of validations and sanity checks that happen within the code. If for any reason, there is an error (for example, a file is not found), the application prints the error message and exits.
If you want to build the source code again, use this command:
`mvn clean install`. Please note that running this command would update the .jar file in the target folder with the latest one.

## API Usage

The APIs can be called by using curl command or by using tools like Postman or any browser based REST API plugin (example: ARC for Chrome)
### GET API to get user info by username

**HTTP Method:** GET

**URL: ** http://localhost:8080/api/userfile/users/{username}

**Example URL: ** http://localhost:8080/api/userfile/users/root

**Headers: ** None

**Responses:**
- 200 OK with the user info in case of success (Example: root:x:0:0:root:/root:/bin/bash)
- 400 Bad Request in case the user with the given username is not found
- 500 Internal server error in case of any other error

### PUT API to update a user info

**HTTP Method:** PUT

**URL: ** http://localhost:8080/api/userfile/users

**Example URL: ** http://localhost:8080/api/userfile/users

**Headers: ** Content-Type: application/json

**Sample JSON Body: **
{
	"username" : "root",
    "homeDir" : "/ggg/dd",
    "loginShell" : "/bbb/ss"
}

**Responses:**
- 200 OK if the userinfo is updated successfully
- 400 Bad Request in case the user with the given username is not found
- 500 Internal server error in case of any other error

## Notes

- Due to lack of time, unit tests have not been included in this exercise
- Due to lack of time, dockerization has not been done as part of this exercise
