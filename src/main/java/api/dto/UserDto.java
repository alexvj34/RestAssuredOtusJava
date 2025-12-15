package api.dto;

import java.util.Objects;

public class UserDto {
  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String phone;
  private Integer userStatus;

  public UserDto() {
  }

  public UserDto(Long id, String username, String firstName, String lastName,
                 String email, String password, String phone, Integer userStatus) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.userStatus = userStatus;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Integer getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(Integer userStatus) {
    this.userStatus = userStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDto userDto = (UserDto) o;
    return Objects.equals(id, userDto.id) &&
        Objects.equals(username, userDto.username) &&
        Objects.equals(firstName, userDto.firstName) &&
        Objects.equals(lastName, userDto.lastName) &&
        Objects.equals(email, userDto.email) &&
        Objects.equals(password, userDto.password) &&
        Objects.equals(phone, userDto.phone) &&
        Objects.equals(userStatus, userDto.userStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstName, lastName, email, password, phone, userStatus);
  }

  @Override
  public String toString() {
    return "UserDto{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", password='" + (password != null ? "[PROTECTED]" : "null") + '\'' +
        ", phone='" + phone + '\'' +
        ", userStatus=" + userStatus +
        '}';
  }

  public static class Builder {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Integer userStatus;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder userStatus(Integer userStatus) {
      this.userStatus = userStatus;
      return this;
    }

    public UserDto build() {
      UserDto userDto = new UserDto();
      userDto.setId(this.id);
      userDto.setUsername(this.username);
      userDto.setFirstName(this.firstName);
      userDto.setLastName(this.lastName);
      userDto.setEmail(this.email);
      userDto.setPassword(this.password);
      userDto.setPhone(this.phone);
      userDto.setUserStatus(this.userStatus);
      return userDto;
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}