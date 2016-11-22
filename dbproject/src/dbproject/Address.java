/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

/**
 *
 * @author bmcclint
 */
public class Address {
  private final String addr_code;
  private final String addr_type;
  private final String street;
  private final String zip_code;
  private final String state;
  private final String city;
  
  public Address (String addr_code, String addr_type, String street, 
          String zip_code, String state, String city) {
    this.addr_code = addr_code;
    this.addr_type = addr_type;
    this.city = city;
    this.state = state;
    this.street = street;
    this.zip_code = zip_code;
  }
  
  public void addAddress(){}
  
  public void deleteAddress(){}
}
