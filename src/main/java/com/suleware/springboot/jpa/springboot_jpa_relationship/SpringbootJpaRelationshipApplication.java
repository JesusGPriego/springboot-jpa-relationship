package com.suleware.springboot.jpa.springboot_jpa_relationship;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Client;
import com.suleware.springboot.jpa.springboot_jpa_relationship.entities.Invoice;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.ClientRepository;
import com.suleware.springboot.jpa.springboot_jpa_relationship.repositories.InvoiceRepository;

import jakarta.transaction.Transactional;

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
		manyToOne();
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
