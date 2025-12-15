package api.dto;

import java.util.List;
import java.util.Objects;

public class PetDto {
  private Long id;
  private CategoryDto category;
  private String name;
  private List<String> photoUrls;
  private List<TagDto> tags;
  private Status status;

  public PetDto() {
  }

  public PetDto(Long id, CategoryDto category, String name, List<String> photoUrls,
                List<TagDto> tags, Status status) {
    this.id = id;
    this.category = category;
    this.name = name;
    this.photoUrls = photoUrls;
    this.tags = tags;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CategoryDto getCategory() {
    return category;
  }

  public void setCategory(CategoryDto category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPhotoUrls() {
    return photoUrls;
  }

  public void setPhotoUrls(List<String> photoUrls) {
    this.photoUrls = photoUrls;
  }

  public List<TagDto> getTags() {
    return tags;
  }

  public void setTags(List<TagDto> tags) {
    this.tags = tags;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public enum Status {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

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
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PetDto petDto = (PetDto) o;
    return Objects.equals(id, petDto.id) &&
        Objects.equals(category, petDto.category) &&
        Objects.equals(name, petDto.name) &&
        Objects.equals(photoUrls, petDto.photoUrls) &&
        Objects.equals(tags, petDto.tags) &&
        status == petDto.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, category, name, photoUrls, tags, status);
  }

  @Override
  public String toString() {
    return "PetDto{" +
        "id=" + id +
        ", category=" + category +
        ", name='" + name + '\'' +
        ", photoUrls=" + photoUrls +
        ", tags=" + tags +
        ", status=" + status +
        '}';
  }

  public static class Builder {
    private Long id;
    private CategoryDto category;
    private String name;
    private List<String> photoUrls;
    private List<TagDto> tags;
    private Status status;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder category(CategoryDto category) {
      this.category = category;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder photoUrls(List<String> photoUrls) {
      this.photoUrls = photoUrls;
      return this;
    }

    public Builder tags(List<TagDto> tags) {
      this.tags = tags;
      return this;
    }

    public Builder status(Status status) {
      this.status = status;
      return this;
    }

    public PetDto build() {
      PetDto petDto = new PetDto();
      petDto.setId(this.id);
      petDto.setCategory(this.category);
      petDto.setName(this.name);
      petDto.setPhotoUrls(this.photoUrls);
      petDto.setTags(this.tags);
      petDto.setStatus(this.status);
      return petDto;
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}