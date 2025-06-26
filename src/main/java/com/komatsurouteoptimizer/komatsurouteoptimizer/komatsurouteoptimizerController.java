package com.komatsurouteoptimizer.komatsurouteoptimizer;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

@RestController
public class komatsurouteoptimizerController {

    // PRIVADO PARA PREPARAR EN CASO DE QUE SE TENGA MAS ENDPOINTS
    private Map<String, Map<String, Integer>> buildGraph(List<RouteData> routes) {
        Map<String, Map<String, Integer>> graph = new HashMap<>(routes.size() / 2, 0.75f);
        
        for (RouteData route : routes) {
            graph.computeIfAbsent(route.getLocStart(), k -> new HashMap<>(4, 0.75f))
                 .put(route.getLocEnd(), route.getTime());
        }
        
        return graph;
    }
    
    // PRIVADO PARA PREPARAR EN CASO DE QUE SE TENGA MAS ENDPOINTS
    private RouteResult findShortestPath(Map<String, Map<String, Integer>> graph, String start, String end) {
        int graphSize = graph.size();
        Map<String, Integer> distances = new HashMap<>(graphSize, 0.75f);
        Map<String, String> previous = new HashMap<>(graphSize, 0.75f);
        PriorityQueue<Node> pq = new PriorityQueue<>(graphSize, Comparator.comparingInt(n -> n.distance));
        Set<String> visited = new HashSet<>(graphSize, 0.75f);
        
        // Inicializar todas las ubicaciones con distancia infinita
        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.offer(new Node(start, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String currentLoc = current.location;
            
            if (visited.contains(currentLoc)) continue;
            visited.add(currentLoc);
            
            if (currentLoc.equals(end)) break;
            
            Map<String, Integer> neighbors = graph.get(currentLoc);
            if (neighbors == null) continue;
            
            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                String neighborLoc = neighbor.getKey();
                int weight = neighbor.getValue();
                
                if (visited.contains(neighborLoc)) continue;
                
                int newDistance = distances.get(currentLoc) + weight;
                // Obtener la distancia actual del vecino, si no existe usar Integer.MAX_VALUE
                Integer currentDistance = distances.get(neighborLoc);
                if (currentDistance == null) {
                    currentDistance = Integer.MAX_VALUE;
                    distances.put(neighborLoc, Integer.MAX_VALUE);
                }
                
                if (newDistance < currentDistance) {
                    distances.put(neighborLoc, newDistance);
                    previous.put(neighborLoc, currentLoc);
                    pq.offer(new Node(neighborLoc, newDistance));
                }
            }
        }
        
        if (distances.get(end) == Integer.MAX_VALUE) {
            return null;
        }
        
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        
        return new RouteResult(path, distances.get(end));
    }
    
    // PRIVADO PORQUE QUE FUNCIONE UNICAMENTE ESTE ENDPOINT
    private List<RouteData> readCsvFile(MultipartFile file) throws IOException {
        List<RouteData> data = new ArrayList<>();
        
        int estimatedSize = Math.max(1000, (int)(file.getSize() / 50));
        data = new ArrayList<>(estimatedSize);
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()), 8192)) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (isFirstLine) {
                    String[] headers = line.split(";");
                    if (headers.length != 3 || 
                        !headers[0].trim().equals("loc_start") ||
                        !headers[1].trim().equals("loc_end") ||
                        !headers[2].trim().equals("time")) {
                        throw new IllegalArgumentException("El CSV debe tener el formato: loc_start;loc_end;time");
                    }
                    isFirstLine = false;
                    continue;
                }
                
                String[] values = line.split(";");
                if (values.length != 3) {
                    throw new IllegalArgumentException("Cada línea debe tener exactamente 3 valores separados por punto y coma");
                }
                
                String locStart = values[0].trim();
                String locEnd = values[1].trim();
                
                if (locStart.isEmpty() || locEnd.isEmpty()) {
                    throw new IllegalArgumentException("Los nombres de ubicación no pueden estar vacíos");
                }
                
                try {
                    int time = Integer.parseInt(values[2].trim());
                    if (time < 0) {
                        throw new IllegalArgumentException("El tiempo debe ser un número positivo");
                    }
                    data.add(new RouteData(locStart, locEnd, time));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El tiempo debe ser un número entero válido");
                }
            }
        }
        
        return data;
    }

    public static class RouteData {
        private String locStart;
        private String locEnd;
        private int time;
        
        public RouteData(String locStart, String locEnd, int time) {
            this.locStart = locStart;
            this.locEnd = locEnd;
            this.time = time;
        }
        
        public String getLocStart() { return locStart; }
        public String getLocEnd() { return locEnd; }
        public int getTime() { return time; }
        
        public void setLocStart(String locStart) { this.locStart = locStart; }
        public void setLocEnd(String locEnd) { this.locEnd = locEnd; }
        public void setTime(int time) { this.time = time; }
    }
    
    private static class Node {
        String location;
        int distance;
        
        Node(String location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }
    
    private static class RouteResult {
        List<String> path;
        int totalTime;
        
        RouteResult(List<String> path, int totalTime) {
            this.path = path;
            this.totalTime = totalTime;
        }
    }

    @PostMapping("/route-optimization")
    public ResponseEntity<Map<String, Object>> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin) {
        
        // INICIO DEL PROCESO TIMER!!
        long startTime = System.currentTimeMillis();
        
        // PARA NO CREAR UNA CLASE PARA LA RESPUESTA
        Map<String, Object> response = new HashMap<>();
        
        // CASO DE QUE EL ARCHIVO ESTE VACIO
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "CSV vacío");
            return ResponseEntity.badRequest().body(response);
        }

        // CASO DE QUE EL ARCHIVO NO SEA UN CSV
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            response.put("success", false);
            response.put("message", "El archivo debe ser un CSV");
            return ResponseEntity.badRequest().body(response);
        }
        
        // CASO DE QUE EL PUNTO DE INICIO NO SEA UNA UBICACION VALIDA
        if (inicio == null || inicio.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Debes especificar el punto de inicio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // CASO DE QUE EL PUNTO DE FIN NO SEA UNA UBICACION VALIDA
        if (fin == null || fin.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Debes especificar el punto de fin");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // DEMORA LECTURA DEL CSV Y CREACION DE LA LISTA
            long csvStartTime = System.currentTimeMillis();
            List<RouteData> routeData = readCsvFile(file);
            long csvTime = System.currentTimeMillis() - csvStartTime;
            
            // DEMORA CREACION DEL GRAFO Y MAPEO DEL MISMO
            long graphStartTime = System.currentTimeMillis();
            Map<String, Map<String, Integer>> graph = buildGraph(routeData);
            long graphTime = System.currentTimeMillis() - graphStartTime;
            
            // VARIABLES PARA EL INICIO Y FIN QUE VIENEN EN LOS PARAM REQUEST
            String origin = inicio.trim();
            String destination = fin.trim();
            
            // CASO DE QUE EL PUNTO DE INICIO NO EXISTA EN EL GRAFO
            if (!graph.containsKey(origin)) {
                response.put("success", false);
                response.put("message", "La ubicación de inicio '" + origin + "' no existe en el archivo");
                return ResponseEntity.badRequest().body(response);
            }
            
            // CASO DE QUE EL PUNTO DE FIN NO EXISTA EN EL GRAFO
            if (!graph.containsKey(destination)) {
                response.put("success", false);
                response.put("message", "La ubicación de fin '" + destination + "' no existe en el archivo");
                return ResponseEntity.badRequest().body(response);
            }
            
            // DEMORA EN LA BUSQUEDA DE LA RUTA MAS CORTA
            long dijkstraStartTime = System.currentTimeMillis();
            RouteResult routeResult = findShortestPath(graph, origin, destination);
            long dijkstraTime = System.currentTimeMillis() - dijkstraStartTime;
            
            // CASO DE QUE NO EXISTA UNA RUTA ENTRE EL INICIO Y EL FIN
            if (routeResult == null) {
                response.put("success", false);
                response.put("message", "No existe una ruta entre '" + origin + "' y '" + destination + "'");
                return ResponseEntity.badRequest().body(response);
            }
            
            // TIEMPO TOTAL DEL PROCESO
            long totalTime = System.currentTimeMillis() - startTime;

            // RESPUESTA
            response.put("success", true);
            response.put("message", "Ruta más corta encontrada");
            response.put("inicio", origin);
            response.put("fin", destination);
            response.put("ruta", routeResult.path);
            response.put("tiempoTotal", routeResult.totalTime);
            response.put("performance", Map.of(
                "csvReadTimeMs", csvTime,
                "graphBuildTimeMs", graphTime,
                "dijkstraTimeMs", dijkstraTime,
                "meetsTarget", totalTime < 300
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error al leer el archivo: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Error en el formato del CSV: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
