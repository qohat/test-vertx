package com.tests.web;

import com.tests.web.prospect.aplication.ProspectCommandVerticle;
import com.tests.web.prospect.aplication.ProspectQueryVerticle;
import com.tests.web.prospect.adapter.http.ProspectHttpVerticle;
import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;

public class LaunchVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) {
    vertx.rxDeployVerticle(ProspectCommandVerticle.class.getName())
      .toCompletable()
      .andThen(vertx.rxDeployVerticle(ProspectQueryVerticle.class.getName()))
      .toCompletable()
      .andThen(vertx.rxDeployVerticle(ProspectHttpVerticle.class.getName()))
      .toCompletable()
      .subscribe(startFuture::complete, startFuture::fail);
  }
}
