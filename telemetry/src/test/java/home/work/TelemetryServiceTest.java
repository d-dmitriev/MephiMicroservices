package home.work;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
class TelemetryServiceTest {

    private ManagedChannel channel;

    @BeforeEach
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8081).usePlaintext().build();
    }

    @AfterEach
    public void cleanup() throws InterruptedException {
        channel.shutdown();
        channel.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user", "administrator"})
    public void testHelloWorldServiceUsingMutinyStub() {
        GetLatestTelemetryReply reply = MutinyTelemetryGrpc.newMutinyStub(channel)
                .getLatestTelemetry(GetLatestTelemetryRequest.newBuilder().setId("neo-blocking").build())
                .await().atMost(Duration.ofSeconds(5));
        assertThat(reply.getId()).isEqualTo("neo-blocking");
    }

}
