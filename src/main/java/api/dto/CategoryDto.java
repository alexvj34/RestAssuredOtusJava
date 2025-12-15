package api.dto;

import java.util.Objects;

public class CategoryDto {
  private Long id;
  private String name;

  public CategoryDto() {
  }

  public CategoryDto(Long id, String name) {
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
    CategoryDto that = (CategoryDto) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "CategoryDto{" + "id=" + id + ", name='" + name + '\'' + '}';
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

    public CategoryDto build() {
      CategoryDto categoryDto = new CategoryDto();
      categoryDto.setId(this.id);
      categoryDto.setName(this.name);
      return categoryDto;
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}