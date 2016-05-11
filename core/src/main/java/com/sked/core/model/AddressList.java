package com.sked.core.model;

import java.io.Serializable;

/**
 * Created by Mishra on 1/29/2016.
 */
public class AddressList implements Serializable {
    private String firstName;
    private String lastName;
    private String company;
    private boolean isChecked;
    private String addrees1;
    private String address2;
    private String pincode;

    public String getAddrees1() {
        return addrees1;
    }

    public void setAddrees1(String addrees1) {
        this.addrees1 = addrees1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
