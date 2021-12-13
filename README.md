## Fabric8 Kubernetes Client Exec Demo

Demo application to test exec functionality of Fabric8 Kubernetes Client on top of OpenShift.

### How to Build?
```shell
gradle clean build
```

### How to Run?
```
java -jar build/libs/kubernetes-client-execwatch-test-1.0-SNAPSHOT-all.jar
```

### Deploying to Kubernetes
1. Apply Role, RoleBinding and ServiceAccount to OpenShift Cluster. Make sure you change namespace in `rolebinding.yaml` to desired namespace before applying:
   ```shell
   oc create -f manifests/role.yaml
   oc create -f manifests/rolebinding.yaml
   oc create -f manifests/serviceaccount.yaml
   ```
2. Run Eclipse JKube's OpenShift Gradle Plugin tasks to deploy application to OpenShift:
   ```shell
   gradle ocBuild ocResource ocApply
   ```
3. Check Application logs:
   ```shell
   gradle ocLog
   ```
