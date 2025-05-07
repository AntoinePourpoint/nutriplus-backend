package io.ajl.nutriplus.controller;

import io.ajl.nutriplus.service.ScanService;
import io.ajl.nutriplus.service.model.Product;
import io.ajl.nutriplus.util.Ean13;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/public/scan")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping("/{ean}")
    public Product scan(@PathVariable String ean) {
        var ean13 = Ean13.of(ean);

        return scanService
                .scanProduct(ean13)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ean));
    }
}
