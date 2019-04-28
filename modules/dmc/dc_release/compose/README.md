# 使用Docker-compose 部署项目步骤

## 1、编译各个项目

执行各个项目下的`build.sh`文件，以便生成Docker镜像

```text
 特变注意的是在编译前端项目的时候需要指定一些参数，格式如下
 PUBLIC_URL=xxxx ./build.sh [后台用户管理系统地址] [后台接口管理系统地址] 
 
 eg. 
 PUBLIC_URL= ./build.sh http://localhost:9002 http://localhost:9001
```

## 2、修改Docker-compose的配置文件
在使用APIM生成URL的时候，需要配置代理服务器的地址，proxy的服务器地址以及Apirunner的地址,因此需要根据实际情况配置.
文件位于`./config/apim-api/application.yml` 请根据实际情况修改之。
一般个人部署测试可以使用下面的配置

```yml
  proxyServer:
    host: 'http://192.168.1.101:'
    generateApiPrefix: '9004/api/'
    proxyApiPrefix: '9003/api/extra/'
```


## 3、启动项目

切换到当前文件目录下，使用`docker-compose up -d`开始进行镜像编排可能会用到以下命令

```text
docker-compose ps [查看运行情况]
docker-compose start/stop/restart [启动/停止/重启服务]
docker-compose rm [删除服务]
docker-compose log [查询服务日志]

docker exec -it (容器名称) sh [进入容器]
docker cp (宿主机文件路径) (容器名称:容器路径) [拷贝宿主机文件到容器]
docker cp (容器名称:容器路径) (宿主机文件路径) [拷贝容器文件文件到宿主机]
docker logs （容器名称/ID） -f [查看日志]
``` 