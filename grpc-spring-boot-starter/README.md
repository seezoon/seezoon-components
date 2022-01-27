# grpc-spring-boot-starter

快速是业务拥有GRPC通信能力

## server 端

### 服务注册

使用注解`@GrpcService`标注 ，则会注册为GRPC服务，该service 一定是继承GRPC 生成的server，同时在注解中可以配置拦截器。

```java

@GrpcService
@Slf4j
public class HelloWorldService extends HelloWorldImplBase {

    @Override
    public void say(HelloWorldRequest request,
            StreamObserver<HelloWorldResponse> responseObserver) {
        log.info("recieve request name :{}", request.getName());
        responseObserver
                .onNext(HelloWorldResponse.newBuilder().setMessage("hello " + request.getName())
                        .build());
        responseObserver.onCompleted();
    }
}

```

### 服务端配置

`GrpcServerProperties` 配置类具体需要参看，主要可以配置ip、port，全局拦截器等

## client 端

### 服务注入

服务桩使用注解`@GrpcClient`注入，该类为grpc生成的三种类型的stub,同时需要在配置中声明。

```java

@SpringBootTest
class ClientApplicationTest {

    @GrpcClient
    private HelloWorldStub helloWorldStub;

    @Test
    public void testSay() throws IOException {
        helloWorldStub.say(HelloWorldRequest.newBuilder().setName("df").build(),
                new StreamObserver<HelloWorldResponse>() {
                    @Override
                    public void onNext(HelloWorldResponse value) {
                        System.out.println("response: " + value.getMessage());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("error:" + t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }
                });
        System.in.read();
    }
}
```

### 客户端配置

参见`GrpcClientProperties`,需要定义channels,stub，也可以配置拦截器。

```yaml
grpc:
  client:
    channels:
      hello_world_server:
        target: 127.0.0.1:9000
    stubs:
      - clazz: com.seezoon.helloworld.HelloWorldGrpc.HelloWorldStub
        channel: hello_world_server
```

