package com.komatsurouteoptimizer;

import com.komatsurouteoptimizer.komatsurouteoptimizer.komatsurouteoptimizerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias para Komatsu Route Optimizer API")
public class CSVTest {

    private komatsurouteoptimizerController controller;

    @BeforeEach
    void setUp() {
        controller = new komatsurouteoptimizerController();
    }

    private int getIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new IllegalArgumentException("El valor no es un número: " + value);
    }

    @Test
    @DisplayName("Prueba con chain_10000.csv - WH01 a ST9990")
    void testChain10000WH01ToST9990() throws IOException {
        File csvFile = new File("test_csvs/chain_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "chain_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "WH01", "ST9990"
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        // Verificar rendimiento < 300ms
        @SuppressWarnings("unchecked")
        Map<String, Object> performance = (Map<String, Object>) body.get("performance");
        assertNotNull(performance);
        assertTrue((Boolean) performance.get("meetsTarget"), 
            "El rendimiento debe cumplir el objetivo de menos de 300ms");
        
        assertTrue(getIntValue(performance.get("csvReadTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("graphBuildTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("dijkstraTimeMs")) >= 0);
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con dense_10000.csv - CP13 a R80")
    void testDense10000CP13ToR80() throws IOException {
        File csvFile = new File("test_csvs/dense_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "dense_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "CP13", "R80"
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        // Verificar rendimiento < 300ms
        @SuppressWarnings("unchecked")
        Map<String, Object> performance = (Map<String, Object>) body.get("performance");
        assertNotNull(performance);
        assertTrue((Boolean) performance.get("meetsTarget"), 
            "El rendimiento debe cumplir el objetivo de menos de 300ms");
        
        assertTrue(getIntValue(performance.get("csvReadTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("graphBuildTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("dijkstraTimeMs")) >= 0);
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con sparse_10000.csv - ST52 a DC981")
    void testSparse10000ST52ToDC981() throws IOException {
        File csvFile = new File("test_csvs/sparse_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "sparse_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "ST52", "DC981"
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        // Verificar rendimiento < 300ms
        @SuppressWarnings("unchecked")
        Map<String, Object> performance = (Map<String, Object>) body.get("performance");
        assertNotNull(performance);
        assertTrue((Boolean) performance.get("meetsTarget"), 
            "El rendimiento debe cumplir el objetivo de menos de 300ms");
        
        assertTrue(getIntValue(performance.get("csvReadTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("graphBuildTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("dijkstraTimeMs")) >= 0);
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con grid_10000.csv - G1427 a G1857")
    void testGrid10000G1427ToG1857() throws IOException {
        File csvFile = new File("test_csvs/grid_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "grid_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "G1427", "G1857"
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        // Verificar rendimiento < 300ms
        @SuppressWarnings("unchecked")
        Map<String, Object> performance = (Map<String, Object>) body.get("performance");
        assertNotNull(performance);
        assertTrue((Boolean) performance.get("meetsTarget"), 
            "El rendimiento debe cumplir el objetivo de menos de 300ms");
        
        assertTrue(getIntValue(performance.get("csvReadTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("graphBuildTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("dijkstraTimeMs")) >= 0);
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con long_route_100.csv - R03 a DC99")
    void testLongRoute100R03ToDC99() throws IOException {
        File csvFile = new File("test_csvs/long_route_100.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "long_route_100.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "R03", "DC99"
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        // Verificar rendimiento < 300ms
        @SuppressWarnings("unchecked")
        Map<String, Object> performance = (Map<String, Object>) body.get("performance");
        assertNotNull(performance);
        assertTrue((Boolean) performance.get("meetsTarget"), 
            "El rendimiento debe cumplir el objetivo de menos de 300ms");
        
        assertTrue(getIntValue(performance.get("csvReadTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("graphBuildTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("dijkstraTimeMs")) >= 0);
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con very_dense_200.csv - CP07 a DC14")
    void testVeryDense200CP07ToDC14() throws IOException {
        File csvFile = new File("test_csvs/very_dense_200.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "very_dense_200.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "CP07", "DC14"
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        // Verificar rendimiento < 300ms
        @SuppressWarnings("unchecked")
        Map<String, Object> performance = (Map<String, Object>) body.get("performance");
        assertNotNull(performance);
        assertTrue((Boolean) performance.get("meetsTarget"), 
            "El rendimiento debe cumplir el objetivo de menos de 300ms");
        
        assertTrue(getIntValue(performance.get("csvReadTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("graphBuildTimeMs")) >= 0);
        assertTrue(getIntValue(performance.get("dijkstraTimeMs")) >= 0);
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con archivo vacío")
    void testEmptyFile() {
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "empty.csv", 
            "text/csv", 
            new byte[0]
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "CP13", "R80"
        );

        assertTrue(response.getStatusCode().is4xxClientError());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("CSV vacío", body.get("message"));
    }

    @Test
    @DisplayName("Prueba con archivo que no es CSV")
    void testNonCsvFile() {
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "test.txt", 
            "text/plain", 
            "some content".getBytes()
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "CP13", "R80"
        );

        assertTrue(response.getStatusCode().is4xxClientError());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("El archivo debe ser un CSV", body.get("message"));
    }

    @Test
    @DisplayName("Prueba con inicio vacío")
    void testEmptyStart() throws IOException {
        File csvFile = new File("test_csvs/dense_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "dense_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "", "R80"
        );

        assertTrue(response.getStatusCode().is4xxClientError());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Debes especificar el punto de inicio", body.get("message"));
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con fin vacío")
    void testEmptyEnd() throws IOException {
        File csvFile = new File("test_csvs/dense_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "dense_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "CP13", ""
        );

        assertTrue(response.getStatusCode().is4xxClientError());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Debes especificar el punto de fin", body.get("message"));
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con ubicación de inicio inexistente")
    void testNonExistentStart() throws IOException {
        File csvFile = new File("test_csvs/dense_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "dense_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "INEXISTENTE", "R80"
        );

        assertTrue(response.getStatusCode().is4xxClientError());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertTrue(body.get("message").toString().contains("no existe en el archivo"));
        
        input.close();
    }

    @Test
    @DisplayName("Prueba con ubicación de fin inexistente")
    void testNonExistentEnd() throws IOException {
        File csvFile = new File("test_csvs/dense_10000.csv");
        FileInputStream input = new FileInputStream(csvFile);
        MultipartFile multipartFile = new MockMultipartFile(
            "file", 
            "dense_10000.csv", 
            "text/csv", 
            input
        );

        ResponseEntity<Map<String, Object>> response = controller.uploadCsv(
            multipartFile, "CP13", "INEXISTENTE"
        );

        assertTrue(response.getStatusCode().is4xxClientError());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertTrue(body.get("message").toString().contains("no existe en el archivo"));
        
        input.close();
    }
}
