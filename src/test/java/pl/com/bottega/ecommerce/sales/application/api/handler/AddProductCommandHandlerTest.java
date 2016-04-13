package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class AddProductCommandHandlerTest {
	
	Money money = new Money(BigDecimal.valueOf(100));
	
	ClientData clientData = new ClientData(new Id("1"), "Jan Kowalski");
	
	Product product, otherProduct;
	
	ProductRepository productRepository;
	
	Reservation reservation;
	
	ReservationRepository reservationRepository;
	
	AddProductCommand addProductCommand, addOtherProductCommand;
	
	AddProductCommandHandler addProductCommandHandler;
	
	@Before
	public void setUp() {
		reservation = new Reservation(new Id("2"), ReservationStatus.OPENED, clientData, Date.from(Instant.now()));
		reservationRepository = mock(ReservationRepository.class);
		when(reservationRepository.load(argThat(equalTo(reservation.getId())))).thenReturn(reservation);
		product = new Product(new Id("3"), money, "ACME Product", ProductType.STANDARD);
		otherProduct = new Product(new Id("4"), money, "Other Product", ProductType.STANDARD);
		productRepository = mock(ProductRepository.class);
		when(productRepository.load(argThat(equalTo(product.getId())))).thenReturn(product);
		when(productRepository.load(argThat(equalTo(otherProduct.getId())))).thenReturn(otherProduct);
		addProductCommand = new AddProductCommand(reservation.getId(), product.getId(), 1);
		addOtherProductCommand = new AddProductCommand(reservation.getId(), otherProduct.getId(), 1);
		addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, null, null, null);
	}
	
	@Test
	public void rezerwacjaZawieraDodanyProdukt() {
		addProductCommandHandler.handle(addProductCommand);
		assertThat(reservation.contains(product), is(true));
	}
	
	@Test
	public void liczbyDodanychTakichSamychProduktowSumujaSie()
	{
		addProductCommandHandler.handle(addProductCommand);
		addProductCommandHandler.handle(addProductCommand);
		assertThat(reservation.getReservedProducts().get(0).getQuantity(), is(2));
	}
	
	@Test
	public void liczbyDodanychRoznychProduktowNieSumujaSie()
	{
		addProductCommandHandler.handle(addProductCommand);
		addProductCommandHandler.handle(addOtherProductCommand);
		assertThat(reservation.getReservedProducts().get(0).getQuantity(), is(1));
	}

}
