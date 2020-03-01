package com.tests.web.prospect.domain;

public final class ProspectDocumentType {

  private final int id;
  private final String value;

  public ProspectDocumentType(final int id, final String value) {
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
