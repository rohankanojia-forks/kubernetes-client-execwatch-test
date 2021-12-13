package io.fabric8.demo;

import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExecOnPodTest {
  public static void main(String[] args) {
    try (OpenShiftClient openShiftClient = new DefaultOpenShiftClient()) {
      ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
      String result = execOnPod(openShiftClient, resourceBundle.getString("exec.namespace"), resourceBundle.getString("exec.pod"), resourceBundle.getString("exec.container"), resourceBundle.getString("exec.command"));
      System.out.println(result);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static String execOnPod(OpenShiftClient openShiftClient, String namespace, String podName, String containerId, String... command)
      throws InterruptedException {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    CountDownLatch execLatch = new CountDownLatch(1);

    System.out.printf("Running %s command in %s container in %s/%s Pod.", Arrays.toString(command), containerId, namespace, podName);
    try (ExecWatch execWatch = openShiftClient.pods().inNamespace(namespace).withName(podName).inContainer(containerId)
        .writingOutput(out)
        .usingListener(new ExecListener() {
          @Override
          public void onOpen(Response response) {
            // do nothing
          }

          @Override
          public void onFailure(Throwable throwable, Response response) {
            execLatch.countDown();
          }

          @Override
          public void onClose(int i, String s) {
            execLatch.countDown();
          }
        })
        .exec(command)) {
      execLatch.await(1, TimeUnit.MINUTES);
      return out.toString();
    }
  }
}
