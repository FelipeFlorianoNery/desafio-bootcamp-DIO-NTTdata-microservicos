package com.dio.ordersimulation;

import com.dio.ordersimulation.dto.OrderRequest;
import com.dio.ordersimulation.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCT_SERVICE_URL = "http://product-catalog-service";

    @PostMapping("/simulate")
    public ResponseEntity<String> simulateOrder(@RequestBody OrderRequest request) {
        try {

            String url = PRODUCT_SERVICE_URL + "/products/" + request.getProductId();
            ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);

            if (product != null) {
                double total = product.getPrice() * request.getQuantity();
                String response = String.format(
                        "Simulação de Pedido bem-sucedida! Produto: %s, Quantidade: %d, Total: R$ %.2f",
                        product.getName(), request.getQuantity(), total
                );
                return ResponseEntity.ok(response);
            } else {

                return ResponseEntity.status(404).body("Produto não encontrado!");
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(404).body("Produto com ID " + request.getProductId() + " não encontrado no catálogo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao comunicar com o serviço de catálogo: " + e.getMessage());
        }
    }
}