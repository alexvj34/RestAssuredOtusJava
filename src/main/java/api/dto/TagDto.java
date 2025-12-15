package api.dto;

import java.util.Objects;

public class TagDto {
  private Long id;
  private String name;

  public TagDto() {
  }

  public TagDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TagDto tagDto = (TagDto) o;
    return Objects.equals(id, tagDto.id) &&
        Objects.equals(name, tagDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "TagDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }

  public static class Builder {
    private Long id;
    private String name;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public TagDto build() {
      TagDto tagDto = new TagDto();
      tagDto.setId(this.id);
      tagDto.setName(this.name);
      return tagDto;
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}