FROM node:8-alpine AS build
ARG PUBLIC_URL=/

RUN echo -e 'https://mirror.tuna.tsinghua.edu.cn/alpine/edge/main\nhttps://mirror.tuna.tsinghua.edu.cn/alpine/edge/community\nhttps://mirror.tuna.tsinghua.edu.cn/alpine/edge/testing' > /etc/apk/repositories \
    && apk add --no-cache yarn
WORKDIR /app
COPY . /app

RUN PUBLIC_URL=${PUBLIC_URL} yarn --registry https://registry.npm.taobao.org install
RUN yarn build

FROM busybox:1.29 AS env-processing
ARG USER_API_DEPLOY_ADDR
ARG APIM_API_DEPLOY_ADDR

COPY --from=build /app/build /build
RUN for f in $(find /build/static -type f \( -name "*.js" -o -name "*.css" \)); do \
      echo sed -i "s|USER_API_DEPLOY_ADDR|${USER_API_DEPLOY_ADDR}|g" $f; \
      sed -i "s|USER_API_DEPLOY_ADDR|${USER_API_DEPLOY_ADDR}|g" $f; \
      echo sed -i "s|APIM_API_DEPLOY_ADDR|${APIM_API_DEPLOY_ADDR}|g" $f; \
      sed -i "s|APIM_API_DEPLOY_ADDR|${APIM_API_DEPLOY_ADDR}|g" $f; \
    done

FROM nginx:stable-alpine as release
RUN rm -rf /usr/share/nginx/html
COPY --from=env-processing /build /usr/share/nginx/html
