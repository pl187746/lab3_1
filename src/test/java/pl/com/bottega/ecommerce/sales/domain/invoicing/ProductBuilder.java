package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	
	private Id productId = Id.generate();
	
	private Money price = new Money(BigDecimal.valueOf(100));
	
	private String name = "Some Product";
	
	private Date snapshotDate = Date.from(Instant.now());
		
	private ProductType type = ProductType.STANDARD;
	
	public ProductData build() {
		return new ProductData(productId, price, name, type, snapshotDate);
	}

	public Id getProductId() {
		return productId;
	}

	public void setProductId(Id productId) {
		this.productId = productId;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getSnapshotDate() {
		return snapshotDate;
	}

	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

}
