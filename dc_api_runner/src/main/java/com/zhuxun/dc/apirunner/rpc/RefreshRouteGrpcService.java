package com.zhuxun.dc.apirunner.rpc;

import com.zhuxun.dc.apirunner.zuulapi.RefreshRouteService;
import io.grpc.stub.StreamObserver;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.http.HttpStatus;

@Slf4j
@GRpcService(applyGlobalInterceptors = false)
public class RefreshRouteGrpcService extends RefreshRouteServiceGrpc.RefreshRouteServiceImplBase {

  @Setter
  private RefreshRouteService refreshRouteService;

  @Override
  public void refreshRoute(GrpcRefreshRouteRequest request, StreamObserver<GrpcRefreshRouteResponse> responseObserver) {
    log.info("RefreshRouteGrpcService 接收到 RPC远程服务调用更新路由规则服务.....");
    refreshRouteService.refreshRoute();
    GrpcRefreshRouteResponse.Builder builder = GrpcRefreshRouteResponse.newBuilder()
            .setCount(HttpStatus.OK.value())
            .setMessage("成功更新映射关系");
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}
