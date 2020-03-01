package com.tests.web.prospect.domain.client;

import com.tests.web.prospect.domain.Prospect;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface IProspectClient {

  Future<Boolean> countryIdentificationService(JsonObject prospect);

  Future<Boolean> policeRecord(JsonObject prospect);

  boolean isValidScore();
}
