package home.work;

import io.quarkus.grpc.GrpcService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@GrpcService
@Authenticated
public class TelemetryService implements Telemetry {

    @RolesAllowed("${app.role.telemetry}")
    @Override
    public Uni<GetLatestTelemetryReply> getLatestTelemetry(GetLatestTelemetryRequest request) {
        return Uni.createFrom().item(() ->
                GetLatestTelemetryReply.newBuilder().setId(request.getId()).setTelemetry(TelemetryItem.newBuilder().setCurrentTemperature(20.5f).setLastUpdate(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(3))).build()).build()
        );
    }

    @RolesAllowed("${app.role.telemetry}")
    @Override
    public Uni<GetTelemetryReply> getTelemetry(GetTelemetryRequest request) {
        return Uni.createFrom().item(() ->
                GetTelemetryReply.newBuilder().setId(request.getId())
                    .addTelemetry(TelemetryItem.newBuilder().setCurrentTemperature(20.5f).setLastUpdate(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(3))).build())
                    .addTelemetry(TelemetryItem.newBuilder().setCurrentTemperature(20.0f).setLastUpdate(LocalDateTime.now().minusHours(10).toEpochSecond(ZoneOffset.ofHours(3))).build())
                    .build()
        );
    }
}