package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());;
	
	ClientData client = new ClientData(Id.generate(), "Jan Kowalski");
	
	Money someMoney = new Money(BigDecimal.valueOf(100));
	
	Tax someTax = new Tax(someMoney, "TT");
	
	ProductData productData;
	
	InvoiceRequest invoiceRequest;
	
	TaxPolicy taxPolicy;
	
	@Before
	public void setUp() {
		productData = mock(ProductData.class);
		when(productData.getPrice()).thenReturn(someMoney);
		invoiceRequest = new InvoiceRequest(client);
		taxPolicy = mock(TaxPolicy.class);
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(someTax);
	}
	
	
	private void addRequestItems(int n) {
		for(int i = 0; i < n; ++i)
			invoiceRequest.add(new RequestItem(productData, 1, someMoney));
	}
	
	private void zadanieFakturyZNPozycjamiZwracaFaktureZNPozycjami(int n) {
		addRequestItems(n);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), equalTo(n));
	}
	
	@Test
	public void zadanieFakturyZJednaPozycjaZwracaFaktureZJednaPozycja() {
		zadanieFakturyZNPozycjamiZwracaFaktureZNPozycjami(1);
	}
	
	@Test
	public void zadanieFakturyZWielomaPozycjamiZwracaFaktureZWlasciwaLiczbaPozycji() {
		zadanieFakturyZNPozycjamiZwracaFaktureZNPozycjami(12);
	}
	
	@Test
	public void zadanieFakturyZDwomaPozycjamiWywolujeCalculateTaxDwaRazy() {
		addRequestItems(2);
		bookKeeper.issuance(invoiceRequest, taxPolicy);
		verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
	}
	
	@Test
	public void zadanieFakturyWywolujeCalculateTaxZWlasciwaKwota() {
		addRequestItems(1);
		bookKeeper.issuance(invoiceRequest, taxPolicy);
		verify(taxPolicy, times(1)).calculateTax(any(ProductType.class), argThat(equalTo(someMoney)));
	}
	
}
