package domain;

import java.math.BigDecimal;

public class Product {
	
	private Long itemId;
	
	private BigDecimal itemAmount;
	
	private BigDecimal itemPrice;
	
	public Product() {
		super();
	}

	public Product(Long itemId, BigDecimal itemAmount, BigDecimal itemPrice) {
		super();
		this.itemId = itemId;
		this.itemAmount = itemAmount;
		this.itemPrice = itemPrice;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	/**
	 * Returns the total cost of the sale item 
	 **/
	public BigDecimal getTotalCost() {
		return getItemPrice().multiply(getItemAmount());
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
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
		Product other = (Product) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		return true;
	}
}
