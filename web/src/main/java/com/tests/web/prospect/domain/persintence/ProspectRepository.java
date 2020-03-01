package com.tests.web.prospect.domain.persintence;

import com.tests.web.prospect.domain.Prospect;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProspectRepository implements IProspectRepository {
  private final Map<String, Prospect> store = new HashMap<>();

  public Single<Prospect> save(Prospect prospect) {
    return Single.just(prospect);
  }

  public Maybe<Prospect> findById(String id) {
    return Single
      .just(store.get(id))
      .filter(Objects::nonNull);
  }
}
