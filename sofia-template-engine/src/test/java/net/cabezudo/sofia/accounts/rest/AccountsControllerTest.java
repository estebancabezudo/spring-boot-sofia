package net.cabezudo.sofia.accounts.rest;

import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsControllerTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private WebClientDataManager webClientDataManager;

  @Test
  public void testSetSessionAccount() throws Exception {
    System.out.println(this.restTemplate.getForObject("http://localhost:" + port + "/v1/session/account/set", String.class));
  }

}