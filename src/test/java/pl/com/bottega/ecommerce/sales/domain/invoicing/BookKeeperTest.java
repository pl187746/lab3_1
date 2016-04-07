package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookKeeperTest {
	
	BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());;
	
	ClientData client = new ClientData(Id.generate(), "Jan Kowalski");
	
	ProductData productData;
	
	@Before
	public void setUp() {
		productData = mock(ProductData.class);
	}

	@Test
	public void zadanieFakturyZJednaPozycjaZwracaFaktureZJednaPozycja() {
		InvoiceRequest invReq = new InvoiceRequest(client);
		invReq.add(new RequestItem(productData, 1, new Money(BigDecimal.valueOf(100))));
		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(BigDecimal.valueOf(10)), "TT"));
		Invoice invoice = bookKeeper.issuance(invReq, taxPolicy);
		assertThat(invoice.getItems().size(), equalTo(1));
	}
	
}
