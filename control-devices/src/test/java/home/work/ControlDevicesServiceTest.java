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
class ControlDevicesServiceTest {

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
    public void testControlDevicesServiceServiceUsingMutinyStub() {
        SetTemperatureReply reply = MutinyControlDevicesGrpc.newMutinyStub(channel)
                .setTemperature(SetTemperatureRequest.newBuilder().setId("neo-blocking").setTemperature(20.0f).build())
                .await().atMost(Duration.ofSeconds(5));
        assertThat(reply.getId()).isEqualTo("neo-blocking");
        assertThat(reply.getTemperature()).isEqualTo(20.0f);
    }

}
