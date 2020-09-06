package com.ilegra.salesData;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import report.SalesReportGenerator;

@SpringBootApplication
public class SalesDataApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SalesDataApplication.class, args);
		
		SalesReportGenerator.generateReports();
	}

}
