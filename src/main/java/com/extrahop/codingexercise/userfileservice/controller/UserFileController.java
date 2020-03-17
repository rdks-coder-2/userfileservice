package com.extrahop.codingexercise.userfileservice.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.extrahop.codingexercise.userfileservice.model.UserUpdateDTO;

@RestController
@RequestMapping("/api/userfile/users")
public class UserFileController {

	private static final Logger logger = LoggerFactory.getLogger(UserFileController.class);

	private String inputFile;

	/**
	 * Get a user's info by user name
	 * 
	 * @param username
	 * @return the user info string
	 * @throws IOException
	 * @throws Exception
	 */
	@GetMapping(value = "/{username}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> getUserInfoByUserName(@PathVariable String username) throws IOException {

		logger.debug("Request to get a user's info by username)", username);

		try {
			String userInfo = Files.lines(Paths.get(this.inputFile)).filter(s -> s.startsWith(username)).findFirst()
					.orElse("Not found");
			if (userInfo.equals("Not found")) {
				return new ResponseEntity<String>(username + " not found", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>(userInfo, HttpStatus.OK);

		} catch (NoSuchFileException e) {
			logger.error("Error: ", e);
			return new ResponseEntity<String>("File not found: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update a user's home directory and/or login shell
	 * 
	 * @param userUpdateDTO
	 * @return the success or failure message
	 * @throws Exception
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> updateUserHomeDirLoginShell(@Valid @RequestBody UserUpdateDTO userUpdateDTO)
			throws Exception {

		logger.debug("Request to update a user info");
		
		Path path;
		String userInfo;
		try {
			path = Paths.get(this.inputFile);
			userInfo = Files.lines(path).filter(s -> s.startsWith(userUpdateDTO.getUsername())).findFirst()
					.orElse("Not found");
		} catch (NoSuchFileException e) {
			logger.error("Error: ", e);
			return new ResponseEntity<String>("File not found: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (userInfo.equals("Not found")) {
			return new ResponseEntity<String>(userUpdateDTO.getUsername() + " not found", HttpStatus.BAD_REQUEST);
		}

		Scanner scanner = null;
		PrintWriter printer = null;
		try {
			File file = path.toFile();
			scanner = new Scanner(file);
			StringBuilder buffer = new StringBuilder();
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith(userUpdateDTO.getUsername())) {
					String[] info = line.split(":");
					if (userUpdateDTO.getHomeDir() != null && !userUpdateDTO.getHomeDir().isEmpty()) {
						info[5] = userUpdateDTO.getHomeDir();
					}
					if (userUpdateDTO.getLoginShell() != null && !userUpdateDTO.getLoginShell().isEmpty()) {
						info[6] = userUpdateDTO.getLoginShell();
					}
					StringBuffer updatedLine = new StringBuffer();
					for (String string : info) {
						updatedLine.append(string).append(":");
					}
					updatedLine.setLength(updatedLine.length() - 1);
					line = updatedLine.toString();
				}
				buffer.append(line);
				if (scanner.hasNext())
					buffer.append("\n");
			}

			printer = new PrintWriter(file);
			printer.print(buffer);

		} catch (Exception e) {
			throw (e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			if (printer != null) {
				printer.close();
			}
		}

		return new ResponseEntity<String>(userUpdateDTO.getUsername() + " updated successfully !", HttpStatus.OK);
	}

	public void setInputFile(String inputFile) {
		logger.info("Input file: {}", inputFile);
		this.inputFile = inputFile;
	}
}
