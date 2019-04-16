# 编译镜像

```sh
PUBLIC_URL=dmc ./build.sh \
           http://221.226.86.39/dmc/user \    # 用户接口
           http://221.226.86.39/dmc/apim      # 接口管理接口
```

如果 docker 版本 `<17.5` （支持 multi-stage building），执行

```sh
# SKIP_BUILD=true: 跳过编译阶段，使用之前的编译结果，只替换接口地址
DISABLE_MULTI_STAGE=true PUBLIC_URL=dmc ./build.sh \
                   http://221.226.86.39/dmc/user \
                   http://221.226.86.39/dmc/apim
```
