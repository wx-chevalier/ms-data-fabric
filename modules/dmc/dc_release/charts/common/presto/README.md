Migrate from [kubernetes/charts/stable/presto](https://github.com/kubernetes/charts/tree/master/stable/presto)

# Presto Chart

[Presto](http://prestodb.io/) is an open source distributed SQL query engine for running interactive analytic queries against data sources of all sizes ranging from gigabytes to petabytes.

## Chart Details

This chart will do the following:

* Install a single server which acts both as coordinator and worker
* Install a configmap for it
* Install a secret for it (for catalog configurations)
* Install a service

## Installing the Chart

To install the chart with the release name `my-release`:

```bash
$ git clone https://github.com/lotuc/presto-chart
$ helm install --name my-release .
```

## Configuration

Configurable values are documented in the `values.yaml`.

You can add new catalog like this (`values.yaml`):

```yaml
...
server
  ...
  catalog:
    log.properties: |
      connector.name=localfile
      presto-logs.http-request-log.location=/presto/etc/data/var/log
      presto-logs.http-request-log.pattern=*.log
    dbmysql.properties:
      connector.name=mysql
      connection-url=jdbc:mysql://example.com:port
      connection-user=user
      connection-password=password
...
```

Then upgrade the release:

```bash
$ ls
presto-chart
$ helm upgrade --recreate-pods -f /path/to/your/values.yaml my-release ./presto-chart
```


> **Tip**: You can use the default [values.yaml](values.yaml)
