package airhacks.mockend.crud.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CRUDResourceIT {

    URI uri;
    HttpClient client;

    @BeforeEach
    public void init() {
        this.uri = URI.create("http://localhost:8080/crud/");
        this.client = HttpClient.newHttpClient();
    }
    
    @Test
    public void createWithPut() throws IOException, InterruptedException {
        var input = """
                {
                  "message":"hello frontend",
                  "sender":"mockend"
                }
                """;
        var id = String.valueOf(System.nanoTime());
        var request = HttpRequest.newBuilder(uri.resolve(id)).PUT(BodyPublishers.ofString(input))
                .header("Content-type", "application/json").build();
        var response = client.send(request, BodyHandlers.ofString());
        var status = response.statusCode();
        var headers = response.headers();
        var location = headers.firstValue("Location").get();
        assertTrue(location.endsWith(id));
        assertEquals(201, status);
    }
    
    @Test
    public void updateWithPut() throws IOException, InterruptedException {
        var input = """
                {
                  "message":"hello frontend",
                  "sender":"mockend"
                }
                """;
        var id = String.valueOf(System.nanoTime());

        var request = HttpRequest.newBuilder(uri.resolve(id)).PUT(BodyPublishers.ofString(input))
                .header("Content-type", "application/json").build();
        var response = client.send(request, BodyHandlers.ofString());
        var status = response.statusCode();
        var headers = response.headers();
        var location = headers.firstValue("Location").get();
        assertTrue(location.endsWith(id));
        assertEquals(201, status);

        response = client.send(request, BodyHandlers.ofString());
        status = response.statusCode();
        headers = response.headers();
        assertEquals(204, status);
    }
    
    @Test
    public void createWithPOST() throws IOException, InterruptedException {
        var input = """
                {
                  "message":"hello frontend",
                  "sender":"mockend"
                }
                """;
        var request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(input))
                .header("Content-type", "application/json").build();
        var response = client.send(request, BodyHandlers.ofString());
        var status = response.statusCode();
        var headers = response.headers();
        var location = headers.firstValue("Location").get();
        assertNotNull(location);
        assertEquals(201, status);
    }

    
    @Test
    public void createWithPOSTAndFetchList() throws IOException, InterruptedException {
        this.createWithPOST();
        var request = HttpRequest.newBuilder(uri).GET().headers("Accept", "application/json").build();
        var response = client.send(request, BodyHandlers.ofString());
        var status = response.statusCode();
        assertEquals(200, status);
        var body = response.body();
        assertNotNull(body);
        System.out.println(body);
    }
    
    @Test
    public void deleteEverything() throws IOException, InterruptedException {
        this.createWithPOST();
        var request = HttpRequest.newBuilder(uri).GET().headers("Accept", "application/json").build();
        var response = client.send(request, BodyHandlers.ofString());
        var status = response.statusCode();
        assertEquals(200, status);
        var body = response.body();
        assertNotNull(body);

        var deleteRequest = HttpRequest.newBuilder(uri).DELETE().build();
        var deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        assertEquals(204, deleteResponse.statusCode());

        response = client.send(request, BodyHandlers.ofString());
        status = response.statusCode();
        assertEquals(200, status);

    }

}
