package com.tests.web.contact.domain.persistence;

import com.tests.web.contact.domain.Contact;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IContactRepository {
  Single<Contact> save(Contact Contact);
  Maybe<Contact> findById(String id);
}
