package home.work;

import io.quarkus.grpc.GrpcService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

import jakarta.annotation.security.RolesAllowed;

@GrpcService
@Authenticated
public class HelloService implements Greeter {
    @RolesAllowed("${app.role.reports}")
    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return Uni.createFrom().item(() ->
                HelloReply.newBuilder().setMessage("Hello 2 " + request.getName()).build()
        );
    }
}