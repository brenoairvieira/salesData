package report;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.ilegra.salesData.fileWatcher.FileWatcher;

import domain.Client;
import domain.Product;
import domain.SalesInfo;
import domain.Salesman;
import domain.enums.DataTypeEnum;
import util.Configurations;

public class SalesReportGenerator {
	
	private static final String INLINE_DATA_SEPARATOR="ç";
	private static final String TXT_FILE_EXTENSION="txt";

	private static final String PATH_IN= Configurations.getSalesReportPathIn();
	private static final String PATH_OUT= Configurations.getSalesReportPathOut();
	
	final static Logger logger = Logger.getLogger(SalesReportGenerator.class);

	public static void generateReports() throws IOException {
		
		while (true) {
			Path filePath = FileWatcher.watch(PATH_IN, SalesReportGenerator.class);
			processFile(filePath);
		}
		
	}
	
	private static void processFile(Path filePath) throws IOException {
		File file = new File(filePath.toString());
		
		if (FilenameUtils.getExtension(file.getName()).equals(TXT_FILE_EXTENSION)){
			logger.info("Proessing new file: " + file.getName());
			
			List<String> lines = Arrays.asList(Files.readString(filePath).split("\\r?\\n"));
			

			List<Salesman> salesmanList = new ArrayList<Salesman>();
			List<Client> clientList = new ArrayList<Client>();
			List<SalesInfo> saleList = new ArrayList<SalesInfo>();
			
			for (String line : lines) {
				
				String dataType = line.substring(0,3);
				String[] lineSplit = line.split(INLINE_DATA_SEPARATOR);
				
					if (dataType.equals(DataTypeEnum.SALESMAN_DATA.getId())) {
						
						salesmanList.add(mountSalesmanData(lineSplit));

					} else if (dataType.equals(DataTypeEnum.CLIENT_DATA.getId())) {
							
						clientList.add(mountClientData(lineSplit));

					} else if (dataType.equals(DataTypeEnum.SALE_DATA.getId())) {
						
						saleList.add(mountSalesInfoData(lineSplit));
					}
				} 
				
			
			logger.info("Reading Process Finished" );
			
			writeOutputFile(file.getName(), salesmanList, clientList, saleList);
		} else {
			logger.info("CANCELED - File is not a .txt" );
		}
		
	}
	
	public static void writeOutputFile(String fileName, List<Salesman> salesmanList, List<Client> clientList, List<SalesInfo> saleList) {
		logger.info("Starting Writing Process" );
		
        Path filePath = Paths.get(PATH_OUT, fileName);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Client amount: ");
        sb.append(clientList.size());
        sb.append(System.getProperty("line.separator"));
        sb.append("Salesman amount: ");
        sb.append(salesmanList.size());
        sb.append(System.getProperty("line.separator"));

        SalesInfo saleWithHighestPrice = getSaleWithHighestPrice(saleList);
        sb.append("Sale with the highest price: ");
        sb.append(saleWithHighestPrice.getSaleId());
        sb.append(" - Price: R$");
        sb.append(saleWithHighestPrice.getSaleTotalPrice());
        
        sb.append(System.getProperty("line.separator"));
        
        Salesman worstSalesman = getWorstSalesman(saleList, salesmanList);
        sb.append("Worst salesman: ");
        sb.append("Name: " + worstSalesman.getName());
        sb.append(" - Cpf: " + worstSalesman.getCpf());
        sb.append(" - Total Sold: R$" + worstSalesman.getTotalSold());

        
        try {
        	File file = new File(filePath.toString());
        	if (file.exists()) {
        		Files.writeString(filePath, sb.toString(), StandardOpenOption.WRITE);
        	} else {
        		Files.writeString(filePath, sb.toString(), StandardOpenOption.CREATE_NEW);
        	}
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}

		logger.info("Writing Process Finished, check the data on: " + filePath.toString());
		
	}
	
	
	
	private static SalesInfo getSaleWithHighestPrice(List<SalesInfo> saleList) {
		SalesInfo saleWithHighestPrice = null;
		
		for (SalesInfo saleInfo : saleList) {
			if (saleWithHighestPrice == null || 
					(saleWithHighestPrice != null && saleInfo.getSaleTotalPrice().compareTo(saleWithHighestPrice.getSaleTotalPrice()) > 0)) {
				saleWithHighestPrice = saleInfo;
			} 
		}
		
		
		return saleWithHighestPrice;
	}
	
	
	private static Salesman getWorstSalesman(List<SalesInfo> saleList, List<Salesman> salesmanList) {
		BigDecimal lowestTotalSold = null;
		Salesman worstSalesman = null;
		
		for (Salesman saleman : salesmanList) {
			
			BigDecimal totalPriceSoldBySalesman = getTotalPriceSoldBySalesman(saleList, saleman.getName());
			
			if (lowestTotalSold == null || 
					(lowestTotalSold != null &&  totalPriceSoldBySalesman.compareTo(lowestTotalSold) < 0 )) {
				
				lowestTotalSold = totalPriceSoldBySalesman;
				worstSalesman = saleman;
				worstSalesman.setTotalSold(totalPriceSoldBySalesman);
			}
		}
		
		return worstSalesman;
	}
	
	public static BigDecimal getTotalPriceSoldBySalesman(List<SalesInfo> saleList, String salesmanName) {
		BigDecimal totalPrice = BigDecimal.ZERO;
		
		for (SalesInfo saleInfo : saleList) {
			if (saleInfo.getSalesmanName().toUpperCase().trim().equals(salesmanName.toUpperCase().trim())) {
				totalPrice = totalPrice.add(saleInfo.getSaleTotalPrice());
			}
		}
		return totalPrice;
	}
	

	public static Salesman mountSalesmanData(String[] lineSplit) {
		Salesman salesman = new Salesman();
		
		salesman.setCpf(lineSplit[1]);
		salesman.setName(lineSplit[2]);
		salesman.setSalary(new BigDecimal(lineSplit[3]));
		
		return salesman;
	}
	
	public static Client mountClientData(String[] lineSplit) {
		Client client = new Client();

		client.setCnpj(lineSplit[1]);
		client.setName(lineSplit[2]);
		client.setBusinessArea(lineSplit[3]);
		
		return client;
	}
	
	public static SalesInfo mountSalesInfoData(String[] lineSplit) {
		SalesInfo salesInfo = new SalesInfo();
		List<Product> productList = new ArrayList<Product>();

		salesInfo.setSaleId(lineSplit[1]);
		
		List<String> productDataList = Arrays.asList(lineSplit[2].substring(1, lineSplit[2].length() -1).split(","));
		
		for (String productData : productDataList) {
			Product product = new Product();
			
			String[] productDataSplit = productData.split("-");
			product.setItemId(Long.parseLong(productDataSplit[0]));
			product.setItemAmount(new BigDecimal(productDataSplit[1]));
			product.setItemPrice(new BigDecimal(productDataSplit[2]));
			
			productList.add(product);
		}
		
		salesInfo.setProductList(productList);
		salesInfo.setSalesmanName(lineSplit[3]);
		
		return salesInfo;
	}
	
	
}
