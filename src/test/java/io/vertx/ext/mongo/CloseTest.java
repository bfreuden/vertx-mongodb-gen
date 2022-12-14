package io.vertx.ext.mongo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.MongoClient;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author <a href="mailto:kostya05983@mail.ru">Konstantin Volivach</a>
 */
public class CloseTest extends MongoTestBase {
  private static final ClientConfig theConfig = getConfig();

  public static class SharedVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startFuture) {
      MongoClient client = MongoClient.create(vertx, theConfig);
      startFuture.complete();
    }
  }

  @Test
  public void testCloseWhenVerticleUndeployed() throws InterruptedException, ExecutionException, TimeoutException {
    CompletableFuture<String> id = new CompletableFuture<>();
    vertx.deployVerticle(SharedVerticle.class.getName(), new DeploymentOptions().setInstances(1), onSuccess(id::complete));

    close(id.get(10, TimeUnit.SECONDS));
  }

  private void close(String deploymentId) throws InterruptedException {
    CountDownLatch closeLatch = new CountDownLatch(1);
    vertx.undeploy(deploymentId, onSuccess(v -> {
      closeLatch.countDown();
    }));
    awaitLatch(closeLatch);
  }
}
