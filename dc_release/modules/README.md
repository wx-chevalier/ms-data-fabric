# 子模块

## 编译

如果有 registry 可以设定：

```sh
export REGISTRY=localhost:5000
```

如果要编译前端，需要设定其部署路径以及相关接口地址：

```sh
export PUBLIC_URL=dmc
export USER_API_URL=http://127.0.0.1:9001
export APIM_API_URL=http://127.0.0.1:9002
```

构建所有模块：

```sh
./build.sh
```

构建特定模块：

```sh
./build.sh dmc_apim
```
