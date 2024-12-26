package home.work;

import io.quarkus.grpc.GrpcService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

import jakarta.annotation.security.RolesAllowed;

import java.util.HashMap;
import java.util.Map;

@GrpcService
@Authenticated
public class ControlDevicesService implements ControlDevices {
    private final Map<String, Float> devices = new HashMap<>();
    @RolesAllowed("${app.role.control-devices}")
    @Override
    public Uni<SetTemperatureReply> setTemperature(SetTemperatureRequest request) {
        devices.put(request.getId(), request.getTemperature());
        return Uni.createFrom().item(() ->
                SetTemperatureReply.newBuilder().setTemperature(request.getTemperature()).setId(request.getId()).build()
        );
    }
    @RolesAllowed("${app.role.control-devices}")
    @Override
    public Uni<GetTemperatureReply> getTemperature(GetTemperatureRequest request) {
        return Uni.createFrom().item(() ->
                GetTemperatureReply.newBuilder().setTemperature(devices.getOrDefault(request.getId(), 0.0f)).build()
        );
    }
}