package home.work;

import io.grpc.Metadata;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcClientUtils;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jboss.resteasy.reactive.RestHeader;

@Path("/hello")
@Authenticated
public class ExampleResource {
    private static final Metadata.Key<String> AUTHORIZATION = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    @GrpcClient
    Greeter hello;

    @GET
    @Path("/{name}")
    public Uni<String> hello(@RestHeader("Authorization") String auth, String name) {
        Metadata extraHeaders = new Metadata();
        if (auth != null) {
            extraHeaders.put(AUTHORIZATION, auth);
        }

        Greeter alteredClient = GrpcClientUtils.attachHeaders(hello, extraHeaders); 

        return alteredClient.sayHello(HelloRequest.newBuilder().setName(name).build())
                .onItem().transform(HelloReply::getMessage);
    }
}
