package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	
	private Id id = Id.generate();
	
	private Money price = new Money(BigDecimal.valueOf(100));
	
	private String name = "Some Product";
	
	private Date snapshotDate = Date.from(Instant.now());
		
	private ProductType type = ProductType.STANDARD;
	
	public ProductData buildData() {
		return new ProductData(id, price, name, type, snapshotDate);
	}
	
	public Product build() {
		return new Product(id, price, name, type);
	}

	public ProductBuilder withId(Id id) {
		this.id = id;
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
