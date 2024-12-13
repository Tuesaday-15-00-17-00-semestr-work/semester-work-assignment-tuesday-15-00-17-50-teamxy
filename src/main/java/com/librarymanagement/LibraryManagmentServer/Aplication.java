package com.librarymanagement.LibraryManagmentServer;

import com.librarymanagement.LibraryManagmentServer.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aplication {

	private static final Logger log= LoggerFactory.getLogger(Aplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Aplication.class, args);
	}


}
