package com.tests.web.prospect.domain.persintence;

import com.tests.web.prospect.domain.Prospect;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IProspectRepository {
  Single<Prospect> save(Prospect prospect);

  Maybe<Prospect> findById(String id);
}
