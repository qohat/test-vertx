package com.tests.web.prospect.domain.client;

import com.tests.web.core.prospect.exception.ProspectNotFoundException;
import com.tests.web.core.prospect.exception.ProspectPoliceRecordException;
import com.tests.web.prospect.domain.Prospect;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.rxjava.core.Vertx;

import java.util.Random;

public class ProspectClient implements IProspectClient {

  private WebClient webClient;
  private static final String API_URL = "localhost";
  private static final int API_PORT = 8080;
  private static final String COUNTRY_IDENTIFICATION = "/country-identification";
  private static final String POLICE_RECORD = "/police-records";

  public ProspectClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public Future<Boolean> countryIdentificationService(JsonObject prospect) {
    Future<Boolean> future = Future.future();
    webClient
      .post(API_PORT, API_URL, COUNTRY_IDENTIFICATION)
      .sendJson(prospect, ar -> {
        if (ar.succeeded()) {
          future.complete("true".equals(ar.result().bodyAsString()));
        } else {
          throw new ProspectNotFoundException();
        }
      });
    return future;
  }

  public Future<Boolean> policeRecord(JsonObject prospect) {
    Future<Boolean> future = Future.future();
    webClient
      .post(API_PORT, API_URL, POLICE_RECORD)
      .sendJson(prospect, ar -> {
        if (ar.succeeded()) {
          future.complete("true".equals(ar.result().bodyAsString()));
        } else {
          throw new ProspectPoliceRecordException();
        }
      });
    return future;
  }

  public boolean isValidScore() {
    Random random = new Random();
    return random.nextInt() >= 60;
  }
}
