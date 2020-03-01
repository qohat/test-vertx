package com.tests.web.contact.domain.persistence;

import com.tests.web.contact.domain.Contact;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContactRepository implements IContactRepository {

  private final Map<String, Contact> store = new HashMap<>();

  public Single<Contact> save(Contact Contact) {
    return Single.just(Contact);
  }

  public Maybe<Contact> findById(String id) {
    return Single
      .just(store.get(id))
      .filter(Objects::nonNull);
  }
}
