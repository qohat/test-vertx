package com.tests.web.prospect.adapter.http;

import com.tests.web.contact.aplication.ContactCommandVerticle;
import com.tests.web.core.prospect.exception.ProspectNotValidException;
import com.tests.web.prospect.aplication.ProspectCommandVerticle;
import com.tests.web.prospect.domain.client.IProspectClient;
import com.tests.web.prospect.domain.client.ProspectClient;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.BodyHandler;

public class ProspectHttpVerticle extends AbstractVerticle {

  public static final String BUS_EVENT = "event";
  private IProspectClient prospectClient;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    prospectClient = new ProspectClient(WebClient.create(vertx));
  }

  @Override
  public void start(Future<Void> startPromise) {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post("/prospect/validate").handler(this::validateProspect);
    server.requestHandler(router).listen(8888, ar -> {
      if (ar.succeeded())  {
        System.out.println("Everything was good");
      } else {
        System.out.println("Some went wrong" + ar.cause());
      }
    });
  }

  public void validateProspect(RoutingContext routingContext) {
    CompositeFuture
      .all(prospectClient.countryIdentificationService(routingContext.getBodyAsJson()), prospectClient.policeRecord(routingContext.getBodyAsJson()))
      .map( compositeFuture -> compositeFuture.<Boolean>resultAt(0) && compositeFuture.<Boolean>resultAt(1))
      .flatMap( result -> {
        if(result) {
          // Contact directory storage
          vertx.eventBus().publish(ContactCommandVerticle.CONTACT_COMMAND_CONSUMER,
            routingContext.getBodyAsJson(),
            addEvent(ContactCommandVerticle.SAVE_COMMAND));
        } else {
          // In memory storage
          vertx.eventBus().publish(ProspectCommandVerticle.PROSPECT_COMMAND_CONSUMER,
            routingContext.getBodyAsJson(),
            addEvent(ProspectCommandVerticle.SAVE_COMMAND));
        }
        return Future.future();
      })
      .setHandler( ar -> {
        if(ar.succeeded()) {
          System.out.println("Some future failed");
        } else {
          System.out.println("Some future failed");
        }
      });
  }

  private static DeliveryOptions addEvent(String action) {
    return new DeliveryOptions().addHeader(BUS_EVENT, action);
  }
}
