package com.tests.web;

import com.tests.web.prospect.adapter.http.ProspectHttpVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

@ExtendWith(VertxExtension.class)
public class TestProspectHttpVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new ProspectHttpVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  /*@Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }*/

  @Test
  void getCustomerIdentification(Vertx vertx, VertxTestContext testContext) {
    Future<JsonObject> future = Future.future();
    WebClient.create(vertx)
      .post(8080, "localhost", "/country-identification-" + new Random().nextInt(1)) // Random to change de result
      .send(ar -> {
        if(ar.succeeded()) {
          testContext.completeNow();
        }
      });
  }
}
