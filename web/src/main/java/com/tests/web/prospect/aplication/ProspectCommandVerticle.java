package com.tests.web.prospect.aplication;

import com.tests.web.prospect.adapter.http.ProspectHttpVerticle;
import com.tests.web.prospect.domain.Prospect;
import com.tests.web.prospect.domain.persintence.ProspectRepository;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.eventbus.MessageConsumer;

public class ProspectCommandVerticle extends AbstractVerticle {

  public static final String PROSPECT_COMMAND_CONSUMER = "prospect-command";
  public static final String SAVE_COMMAND = "save";

  private MessageConsumer<JsonObject> consumer;
  private ProspectRepository prospectRepository;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    prospectRepository = new ProspectRepository();
  }

  @Override
  public void start(Future<Void> startFuture) {
    registerConsumer(startFuture);
  }

  private void registerConsumer(Future<Void> startFuture) {
    consumer = vertx.eventBus().consumer(PROSPECT_COMMAND_CONSUMER, message -> {
      switch (message.headers().get(ProspectHttpVerticle.BUS_EVENT)) {
        case SAVE_COMMAND:
          saveProspect(message);
          break;
        default:
          message.fail(400, "Unknown action.");
      }
    });
    consumer.completionHandler(startFuture);
  }

  private void saveProspect(Message<JsonObject> message) {
    prospectRepository
      .save(message.body().mapTo(Prospect.class))
      .map(JsonObject::mapFrom)
      .doOnSuccess( s -> System.out.println("Prospect saved " + s.getString("id")))
      .subscribe(message::reply, e -> message.fail(500, e.getMessage()));
  }

  @Override
  public void stop(Future<Void> stopFuture) {
    consumer.unregister(stopFuture);
  }
}
