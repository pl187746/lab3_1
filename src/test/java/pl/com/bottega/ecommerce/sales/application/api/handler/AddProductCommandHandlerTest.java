package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.invoicing.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {
	
	Money money = new Money(BigDecimal.valueOf(100));
	
	ClientData clientData = new ClientData(new Id("1"), "Jan Kowalski");
	
	ClientRepository clientRepository;
	
	SystemContext systemContext = new SystemContext();
	
	Product product, otherProduct;
	
	ProductRepository productRepository;
	
	Reservation reservation;
	
	ReservationRepository reservationRepository;
	
	SuggestionService suggestionService;
	
	AddProductCommand addProductCommand, addOtherProductCommand;
	
	AddProductCommandHandler addProductCommandHandler;
	
	@Before
	public void setUp() {
		clientRepository = mock(ClientRepository.class);
		when(clientRepository.load(any(Id.class))).thenReturn(new Client());
		reservation = new Reservation(new Id("2"), ReservationStatus.OPENED, clientData, Date.from(Instant.now()));
		reservationRepository = mock(ReservationRepository.class);
		when(reservationRepository.load(argThat(equalTo(reservation.getId())))).thenReturn(reservation);
		product = new ProductBuilder().withId(new Id("3")).build();
		otherProduct = new ProductBuilder().withId(new Id("4")).build();
		productRepository = mock(ProductRepository.class);
		when(productRepository.load(argThat(equalTo(product.getId())))).thenReturn(product);
		when(productRepository.load(argThat(equalTo(otherProduct.getId())))).thenReturn(otherProduct);
		suggestionService = mock(SuggestionService.class);
		when(suggestionService.suggestEquivalent(argThat(is(product)), any(Client.class))).thenReturn(otherProduct);
		addProductCommand = new AddProductCommand(reservation.getId(), product.getId(), 1);
		addOtherProductCommand = new AddProductCommand(reservation.getId(), otherProduct.getId(), 1);
		addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, suggestionService, clientRepository, systemContext);
	}
	
	@Test
	public void rezerwacjaZawieraDodanyProdukt() {
		addProductCommandHandler.handle(addProductCommand);
		assertThat(reservation.contains(product), is(true));
	}
	
	@Test
	public void liczbyDodanychTakichSamychProduktowSumujaSie() {
		addProductCommandHandler.handle(addProductCommand);
		addProductCommandHandler.handle(addProductCommand);
		assertThat(reservation.getReservedProducts().get(0).getQuantity(), is(2));
	}
	
	@Test
	public void liczbyDodanychRoznychProduktowNieSumujaSie() {
		addProductCommandHandler.handle(addProductCommand);
		addProductCommandHandler.handle(addOtherProductCommand);
		assertThat(reservation.getReservedProducts().get(0).getQuantity(), is(1));
	}
	
	@Test
	public void poDodaniuProduktuRezerwacjaJestZapisywana() {
		addProductCommandHandler.handle(addProductCommand);
		verify(reservationRepository).save(reservation);
	}
	
	@Test
	public void przyProbieDodaniaNieistniejacegoProduktuDodajeInnyProdukt() {
		product.markAsRemoved();
		addProductCommandHandler.handle(addProductCommand);
		assertThat(reservation.contains(otherProduct), is(true));
	}

}
