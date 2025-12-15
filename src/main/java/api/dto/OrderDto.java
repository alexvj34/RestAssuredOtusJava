package api.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderDto {
  private Long id;
  private Long petId;
  private Integer quantity;
  private String shipDate;
  private Status status;
  private Boolean complete;

  public OrderDto() {
  }

  public OrderDto(Long id, Long petId, Integer quantity, String shipDate, Status status, Boolean complete) {
    this.id = id;
    this.petId = petId;
    this.quantity = quantity;
    this.shipDate = shipDate;
    this.status = status;
    this.complete = complete;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPetId() {
    return petId;
  }

  public void setPetId(Long petId) {
    this.petId = petId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getShipDate() {
    return shipDate;
  }

  public void setShipDate(String shipDate) {
    this.shipDate = shipDate;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Boolean getComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }

  public void setShipDate(LocalDateTime dateTime) {
    this.shipDate = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public LocalDateTime getShipDateAsLocalDateTime() {
    return LocalDateTime.parse(shipDate, DateTimeFormatter.ISO_DATE_TIME);
  }

  public enum Status {
    PLACED("placed"),
    APPROVED("approved"),
    DELIVERED("delivered");

    private final String value;

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return this.value;
    }

    public static Status fromString(String value) {
      for (Status status : Status.values()) {
        if (status.value.equalsIgnoreCase(value)) {
          return status;
        }
      }
      throw new IllegalArgumentException("Unknown status: " + value);
    }
  }

  @Override
  public String toString() {
    return "OrderDTO{" +
        "id=" + id +
        ", petId=" + petId +
        ", quantity=" + quantity +
        ", shipDate='" + shipDate + '\'' +
        ", status=" + status +
        ", complete=" + complete +
        '}';
  }
}