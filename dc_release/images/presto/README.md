docker presto
=============

构建镜像
------------

可以执行脚本 `./build.sh` 编译镜像，或者按照脚本中步骤自己手工构建。

使用
----

假定 catalog 配置在目录 `<catalog-path>` 中，`coordinator` IP 地址为 `<coordinator-IP>`：

启动 coordinator

```sh
docker run --rm --name coord -it \
       -e PRESTO_DISCOVERY_URI=http://127.0.0.1:8080 \
       -p 8080:8080 \
       -v <catalog-path>:/presto/etc/catalog \
       presto:0.208 coordinator
```

启动 worker

```sh
docker run --rm --name worker -it \
       -e PRESTO_DISCOVERY_URI=http://<coordinator-IP>:8080 \
       -v <catalog-path>:/presto/etc/catalog \
       presto:0.208 worker
```

启动 presto repl:

```sh
docker exec -it coord presto
```

docker-compose 使用
-------------------

```sh
docker create network vnet
docker-compose up
```

启动 presto repl:

```sh
docker-compose exec -it coordinator-1 presto
```
