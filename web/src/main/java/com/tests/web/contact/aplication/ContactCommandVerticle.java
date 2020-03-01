package com.tests.web.contact.aplication;

import com.tests.web.contact.domain.Contact;
import com.tests.web.contact.domain.persistence.ContactRepository;
import com.tests.web.contact.domain.persistence.IContactRepository;
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

public class ContactCommandVerticle extends AbstractVerticle {

  public static final String CONTACT_COMMAND_CONSUMER = "contact-command";
  public static final String SAVE_COMMAND = "save";

  public static final String BUS_EVENT = "event";

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
    consumer = vertx.eventBus().consumer(CONTACT_COMMAND_CONSUMER, message -> {
      switch (message.headers().get(BUS_EVENT)) {
        case SAVE_COMMAND:
          saveContact(message);
          break;
        default:
          message.fail(400, "Unknown action.");
      }
    });
    consumer.completionHandler(startFuture);
  }

  private void saveContact(Message<JsonObject> message) {
    contactRepository
      .save(message.body().mapTo(Contact.class))
      .map(JsonObject::mapFrom)
      .doOnSuccess( s -> System.out.println("contact saved " + s.getString("id")))
      .subscribe(message::reply, e -> message.fail(500, e.getMessage()));
  }

  @Override
  public void stop(Future<Void> stopFuture) {
    consumer.unregister(stopFuture);
  }
}
