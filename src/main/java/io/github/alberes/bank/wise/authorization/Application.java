package io.github.alberes.bank.wise.authorization;

import io.github.alberes.bank.wise.authorization.domains.Client;
import io.github.alberes.bank.wise.authorization.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Application implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String clientId = "admin-client-id";
		Client client = this.clientRepository.findByClientId(clientId);
		if(client == null){
			client = new Client();
			client.setClientId(clientId);
			client.setClientSecret("$2a$12$40ulvRdrXkyEszgVHD/kEOdZTEzJpnrM7JN0WqGzoFkNJ3eSLRrm.");
			client.setRedirectURI("http://localhost:8080/login/oauth2/code/bank-wise-client-oidc");
			client.setScope("ADMIN");
			this.clientRepository.save(client);
		}
	}
}
