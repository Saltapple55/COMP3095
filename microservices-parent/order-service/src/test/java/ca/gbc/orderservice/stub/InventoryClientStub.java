package ca.gbc.orderservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class InventoryClientStub {

    public static void stubInventoryCall(String skuCode, Integer quantity){
        //fake response
        stubFor(get(urlEqualTo("/api/inventory?skuCode=" + skuCode+ "&quantity="+quantity))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("true")) //status code expect from inventory-service

        );
    }
}
