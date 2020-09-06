package util;

import java.util.ResourceBundle;

public class Configurations {

	public static final String SALES_REPORT_PATH_IN = "sales.report.path.in";
	public static final String SALES_REPORT_PATH_OUT = "sales.report.path.out";
	
	
	private static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle(Configurations.class.getSimpleName().toLowerCase());
	}
	
	public static String getSalesReportPathIn(){
		return getValor(SALES_REPORT_PATH_IN);
	}

	public static String getSalesReportPathOut(){
		return getValor(SALES_REPORT_PATH_OUT);
	}

	public static String getValor(String chave) {
		return bundle.getString(chave);
	}

	private Configurations() {
		throw new AssertionError("Não instanciável!");
	}
}
