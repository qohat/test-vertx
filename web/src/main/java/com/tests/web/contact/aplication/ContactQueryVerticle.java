package com.tests.web.contact.aplication;

import com.tests.web.contact.domain.persistence.ContactRepository;
import com.tests.web.contact.domain.persistence.IContactRepository;
import com.tests.web.prospect.adapter.http.ProspectHttpVerticle;
import com.tests.web.prospect.domain.persintence.ProspectRepository;
import io.reactivex.Single;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.eventbus.MessageConsumer;

public class ContactQueryVerticle extends AbstractVerticle {

  public static final String CONTACT_QUERY_CONSUMER = "contact-query";
  public static final String FIND_COMMAND = "find";

  private MessageConsumer<JsonObject> consumer;
  private IContactRepository contactRepository;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    contactRepository = new ContactRepository();
  }

  @Override
  public void start(Future<Void> startFuture) {
    registerConsumer(startFuture);
  }

  private void registerConsumer(Future<Void> startFuture) {
    consumer = vertx.eventBus().consumer(CONTACT_QUERY_CONSUMER, message -> {
      switch (message.headers().get(ProspectHttpVerticle.BUS_EVENT)) {
        case FIND_COMMAND:
          findProspect(message);
          break;
        default:
          message.fail(400, "Unknown action.");
      }
    });
    consumer.completionHandler(startFuture);
  }

  @Override
  public void stop(Future<Void> stopFuture) {
    consumer.unregister(stopFuture);
  }

  private void findProspect(Message<JsonObject> message) {
    contactRepository
      .findById(message.body().getString("id"))
      .switchIfEmpty(Single.error(new RuntimeException("Foo not found.")))
      .map(JsonObject::mapFrom)
      .subscribe(message::reply, e -> message.fail(404, e.getMessage()));
  }
}
