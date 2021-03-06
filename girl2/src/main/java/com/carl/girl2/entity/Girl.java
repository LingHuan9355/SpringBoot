package com.carl.girl2.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : Girl Created with IDEA
 *
 * @author:CarlLing @CreateDate : 2018-08-16 23:04 @Description :
 */
@Entity
public class Girl {

  @Id @GeneratedValue private Integer id;

  private String cupSize;

  @Min(value = 18, message = "未成年少女禁止入内")
  private Integer age;

  @NotNull(message = "金额必传")
  private Double money;

  public Girl() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCupSize() {
    return cupSize;
  }

  public void setCupSize(String cupSize) {
    this.cupSize = cupSize;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Double getMoney() {
    return money;
  }

  public void setMoney(Double money) {
    this.money = money;
  }

  @Override
  public String toString() {
    return "Girl{"
        + "id="
        + id
        + ", cupSize='"
        + cupSize
        + '\''
        + ", age="
        + age
        + ", money="
        + money
        + '}';
  }
}
