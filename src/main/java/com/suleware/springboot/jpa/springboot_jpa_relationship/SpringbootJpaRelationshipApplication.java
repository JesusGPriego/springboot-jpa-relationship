package com.suleware.springboot.jpa.springboot_jpa_relationship;

import java.util.Optional;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Address;
import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Client;
import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.ClientDetails;
import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Invoice;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.ClientDetailsRepository;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.ClientRepository;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.InvoiceRepository;

import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner {

	private ClientRepository clientRepository;
	private InvoiceRepository invoiceRepository;

	public SpringbootJpaRelationshipApplication(
			ClientRepository clientRepository,
			InvoiceRepository invoiceRepository,
			ClientDetailsRepository clientDetailsRepository) {
		this.clientRepository = clientRepository;
		this.invoiceRepository = invoiceRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		oneToOneExistent();
	}

	@Transactional
	public void oneToOneExistent() {

	}

	@Transactional
	public void oneToOne() {
		Client newClient = new Client("Mary", "Poppins");
		ClientDetails newClientDetails = new ClientDetails(true, 1000);
		newClient.setClientDetails(newClientDetails);
		Client c = clientRepository.save(newClient);
		System.out.println(c);
	}

	@Transactional
	public void removeInvoiceBidirectional() {
		Optional<Client> oClient = clientRepository.findOne(1L);
		oClient.ifPresent(client -> {
			Invoice invoice1 = new Invoice("Asus TÜF Gaming", 150L);
			client.getInvoices().add(invoice1);
			invoice1.setClient(client);
			clientRepository.save(client);
		});
		Optional<Client> optClient = clientRepository.findOne(1L);

		optClient.ifPresent(client -> {
			Long lastInvoiceId = client.getInvoices().stream().reduce((a, b) -> b).get().getId();
			Optional<Invoice> optInvoice = invoiceRepository.findById(lastInvoiceId);
			optInvoice.ifPresentOrElse(invoice -> {
				client.getInvoices().remove(invoice);
				invoice.setClient(null);
				clientRepository.save(client);
				System.out.println(client);
			}, () -> System.out.println("nothing to delete"));
		});
	}

	@Transactional
	public void getClientDatafindByClientId() {
		Optional<Client> oClient = clientRepository.findOneClientWithInvoices(2L);
		oClient.ifPresentOrElse(client -> {
			Invoice invoice1 = new Invoice("Mac 14 pro M3", 3000L);
			Invoice invoice2 = new Invoice("Lenovo Thinkpad X1 Carbon", 860L);

			client.getInvoices().addAll(Arrays.asList(invoice1, invoice2));

			client.getInvoices().forEach(invoice -> invoice.setClient(client));

			Client updatedClient = clientRepository.save(client);
			System.out.println(updatedClient);
		}, () -> System.out.println("No client found"));
	}

	@Transactional
	public void oneToManyBidirectional() {
		Client newClient = new Client("Stephen", "Hawking");

		Invoice invoice1 = new Invoice("Logitech 3s", 90L);
		Invoice invoice2 = new Invoice("Surface 5", 860L);

		newClient.getInvoices().addAll(Arrays.asList(invoice1, invoice2));

		newClient.getInvoices().forEach(invoice -> invoice.setClient(newClient));

		clientRepository.save(newClient);

		System.out.println(newClient);

	}

	@Transactional(readOnly = true)
	public void listOne() {
		clientRepository.findOne(1L).ifPresent(System.out::println);
	}

	@Transactional(readOnly = true)
	public void listClients() {
		clientRepository.findAll().forEach(System.out::println);
	}

	@Transactional
	public void removeClientAddress() {
		Client client = new Client("Steve", "Allen");

		Address address1 = new Address("Real Street", 123);
		Address address2 = new Address("Imagine Street", 12);

		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		Client savedClient = clientRepository.save(client);
		System.out.println(savedClient);

		Optional<Client> oClient = clientRepository.findById(savedClient.getId());
		oClient.ifPresent(sClient -> {
			sClient.getAddresses().remove(address1);
			clientRepository.save(sClient);
			System.out.println(sClient);
		});

	}

	@Transactional
	public void oneToMany() {
		Client client = new Client("Steve", "Allen");

		Address address1 = new Address("Real Street", 123);
		Address address2 = new Address("Imagine Street", 12);

		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

	}

	@Transactional
	public void manyToOne() {
		Client c = new Client("Jane", "Doe");
		clientRepository.save(c);

		Invoice invoice = new Invoice("Office supplies", 1500L);
		invoice.setClient(c);
		System.out.println(invoiceRepository.save(invoice));
	}

	@Transactional
	public void manyToOneToExistent() {
		Long id = 1L;
		Optional<Client> oClient = clientRepository.findById(id);
		oClient.ifPresentOrElse(client -> {
			Invoice invoice = new Invoice("Office supplies", 1500L);
			invoice.setClient(client);
			System.out.println(invoiceRepository.save(invoice));
		}, () -> {
			String error = String.format("No client found with ID %s", id);
			System.out.println(error);
		});
	}
}
