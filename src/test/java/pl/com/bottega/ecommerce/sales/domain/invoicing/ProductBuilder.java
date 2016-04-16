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

	public ProductBuilder withProductId(Id productId) {
		this.productId = productId;
		return this;
	}

	public ProductBuilder withPrice(Money price) {
		this.price = price;
		return this;
	}

	public ProductBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ProductBuilder withSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
		return this;
	}

	public ProductBuilder withType(ProductType type) {
		this.type = type;
		return this;
	}

}
