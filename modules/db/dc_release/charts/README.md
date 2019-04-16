# 使用

## mysql

```sh
helm install -f /path/to/values.yaml --name dcdb mysql
```

## springapp

```sh
helm install -f /path/to/values.yaml --name dcuser springapp
```

```sh
helm install -f deploy-values/nginx-ingress.values.yaml --name nginx-ingress stable/nginx-ingress
```

## mysqldump

```sh
# 结构
mysqldump -hlocalhost -uroot database_name [table_name] > /dst/file
# 结构 + 数据
mysqldump -d -hlocalhost -uroot database_name [table_name] > /dst/file
```