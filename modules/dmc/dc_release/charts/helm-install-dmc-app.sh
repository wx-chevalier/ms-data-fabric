helm install -f deploy-values/dmc-apim.values.yaml --name dmc-apim springapp/
helm install -f deploy-values/dmc-user.values.yaml --name dmc-user springapp/
helm install -f deploy-values/dmc-apicr.values.yaml --name dmc-apicr apirunner/
