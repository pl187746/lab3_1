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
	
	Product product;
	
	ProductRepository productRepository;
	
	Reservation reservation;
	
	ReservationRepository reservationRepository;
	
	AddProductCommand addProductCommand;
	
	AddProductCommandHandler addProductCommandHandler;
	
	@Before
	public void setUp() {
		reservation = new Reservation(new Id("2"), ReservationStatus.OPENED, clientData, Date.from(Instant.now()));
		reservationRepository = mock(ReservationRepository.class);
		when(reservationRepository.load(argThat(equalTo(reservation.getId())))).thenReturn(reservation);
		product = new Product(new Id("3"), money, "ACME Product", ProductType.STANDARD);
		productRepository = mock(ProductRepository.class);
		when(productRepository.load(argThat(equalTo(product.getId())))).thenReturn(product);
		addProductCommand = new AddProductCommand(reservation.getId(), product.getId(), 1);
		addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, null, null, null);
	}
	
	@Test
	public void rezerwacjaZawieraDodanyProdukt() {
		addProductCommandHandler.handle(addProductCommand);
		assertThat(reservation.contains(product), is(true));
	}

}
