package com.tests.web.contact.domain;

public class Contact {

  private ContactId id;

  private ContactName name;

  private ContactDocumentType documentType;

  private ContactDocument document;

  private ContactIssuingDate issuingDate;

  public Contact(ContactId id, ContactName name, ContactDocumentType documentType, ContactDocument document, ContactIssuingDate issuingDate) {
    this.id = id;
    this.name = name;
    this.documentType = documentType;
    this.document = document;
    this.issuingDate = issuingDate;
  }

  public class ContactCoreDomain {
    private final String id;
    private final String name;
    private final String documentType;
    private final String document;
    private final String issuingDate;

    public ContactCoreDomain(String id, String name, String documentType, String document, String issuingDate) {
      this.id = id;
      this.name = name;
      this.documentType = documentType;
      this.document = document;
      this.issuingDate = issuingDate;
    }
  }

  public ContactCoreDomain toCoreDomain(Contact c) {
    return new ContactCoreDomain(
      c.id.value(),
      c.name.value(),
      c.documentType.value(),
      c.document.value(),
      c.issuingDate.value()
    );
  }

}
