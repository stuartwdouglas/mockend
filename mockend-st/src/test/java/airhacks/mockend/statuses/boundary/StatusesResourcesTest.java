package airhacks.mockend.statuses.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class StatusesResourcesTest {

    @TestHTTPResource("statuses")
    URI uri;
    HttpClient client;


    @BeforeEach
    public void init() {
        this.client = HttpClient.newHttpClient();
    }

    @Test
    public void get200() throws IOException, InterruptedException {
        var expected = 200;
        var request = HttpRequest.newBuilder(uri).GET().build();
        var status = client.send(request, BodyHandlers.ofString()).statusCode();
        assertEquals(expected, status);

        expected = 201;
        request = HttpRequest.newBuilder(uri).GET().header("status",String.valueOf(expected)).build();
        status = client.send(request, BodyHandlers.ofString()).statusCode();
        assertEquals(expected,status);

    }

}
