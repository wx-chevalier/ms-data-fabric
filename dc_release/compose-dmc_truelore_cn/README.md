以 12345 集群为例部署，首先镜像构建:

```sh
export REGISTRY=
export USER_API_URL=http://dmc.truelore.cn/user
export APIM_API_URL=http://dmc.truelore.cn/apim
# 进入 modules 目录
./build.sh
```

部署

```sh
./start-12345-dmc.sh
```

启动反向代理

```sh
caddy -conf caddyfile/Caddyfile-12345
```

对于其它集群，首先需要修改 `docker-compose.yml` 中镜像的 `REGISTRY` 部分；
其次需要修改 `Caddyfile` 配置。
