package domain;

import java.math.BigDecimal;
import java.util.List;

public class SalesInfo {

	private String saleId;

	private String salesmanName;
	
	private List<Product> productList;

	public SalesInfo() {
		super();
	}

	public SalesInfo(String saleId, String salesmanName, List<Product> productList) {
		super();
		this.saleId = saleId;
		this.salesmanName = salesmanName;
		this.productList = productList;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}


	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	
	public BigDecimal getSaleTotalPrice( ) {
		BigDecimal totalCost = BigDecimal.ZERO;
		
		for (Product product : getProductList()) {
			totalCost  = totalCost.add(product.getTotalCost());
		}
		
		return totalCost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((saleId == null) ? 0 : saleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesInfo other = (SalesInfo) obj;
		if (saleId == null) {
			if (other.saleId != null)
				return false;
		} else if (!saleId.equals(other.saleId))
			return false;
		return true;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
	
	
}
