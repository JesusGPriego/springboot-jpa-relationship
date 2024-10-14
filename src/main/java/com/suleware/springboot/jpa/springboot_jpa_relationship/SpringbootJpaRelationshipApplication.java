package com.suleware.springboot.jpa.springboot_jpa_relationship;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Address;
import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Client;
import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Invoice;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.ClientRepository;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.InvoiceRepository;

import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner {

	private ClientRepository clientRepository;
	private InvoiceRepository invoiceRepository;

	public SpringbootJpaRelationshipApplication(ClientRepository clientRepository,
			InvoiceRepository invoiceRepository) {
		this.clientRepository = clientRepository;
		this.invoiceRepository = invoiceRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		listOne();
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
