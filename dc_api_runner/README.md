# GRPC服务使用

## GRPC初始化
在重新clone项目或清理项目之后,项目是缺少GRPC支持文件的，此时项目无法正常运行，IDEA提示缺少类文件，此类文件可以通过**gradle generateProto**编译生成即可。 


## GRPC服务开发流程
+ 编写 **src/main/proto/RefreshRoute.proto**文件,Protobuf参考[Protobuf语言指南——.proto文件语法详解](https://blog.csdn.net/u014308482/article/details/52958148)
+ 在Gradle中使用 **gradle generateProto** 命令编译Proto文件，编译生成的文件目录在 **build/generated/source/proto/main/**
+ 使用编译的文件可以实现RPC服务

## Grpc和SpringBoot整合
 在和SpringBoot整合的过程中，使用[https://github.com/LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)这一组件,
 端口默认6565 ,可通过grpc.port来进行配置，使用随机端口请设置为0即可。
 
## 


