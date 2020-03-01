package com.tests.web.contact.domain;

public final class ContactDocumentType {

  private final int id;
  private final String value;

  public ContactDocumentType(final int id, final String value) {
    this.id = id;
    this.value = value;
  }

  public int id() {
    return id;
  }

  public String value() {
    return value;
  }
}
