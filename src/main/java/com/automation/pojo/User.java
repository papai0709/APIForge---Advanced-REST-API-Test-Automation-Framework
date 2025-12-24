package com.automation.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User POJO class representing User entity
 * Example POJO that extends BasePojo
 */
public class User extends BasePojo {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("website")
    private String website;
    
    @JsonProperty("address")
    private Address address;
    
    @JsonProperty("company")
    private Company company;
    
    // Constructors
    public User() {}
    
    public User(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", address=" + address +
                ", company=" + company +
                ", " + super.toString() +
                '}';
    }
    
    // Nested classes for complex objects
    public static class Address {
        @JsonProperty("street")
        private String street;
        
        @JsonProperty("suite")
        private String suite;
        
        @JsonProperty("city")
        private String city;
        
        @JsonProperty("zipcode")
        private String zipcode;
        
        @JsonProperty("geo")
        private Geo geo;
        
        // Getters and Setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getSuite() { return suite; }
        public void setSuite(String suite) { this.suite = suite; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getZipcode() { return zipcode; }
        public void setZipcode(String zipcode) { this.zipcode = zipcode; }
        
        public Geo getGeo() { return geo; }
        public void setGeo(Geo geo) { this.geo = geo; }
        
        @Override
        public String toString() {
            return "Address{street='" + street + "', suite='" + suite + 
                   "', city='" + city + "', zipcode='" + zipcode + "', geo=" + geo + "}";
        }
    }
    
    public static class Geo {
        @JsonProperty("lat")
        private String lat;
        
        @JsonProperty("lng")
        private String lng;
        
        public String getLat() { return lat; }
        public void setLat(String lat) { this.lat = lat; }
        
        public String getLng() { return lng; }
        public void setLng(String lng) { this.lng = lng; }
        
        @Override
        public String toString() {
            return "Geo{lat='" + lat + "', lng='" + lng + "'}";
        }
    }
    
    public static class Company {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("catchPhrase")
        private String catchPhrase;
        
        @JsonProperty("bs")
        private String bs;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getCatchPhrase() { return catchPhrase; }
        public void setCatchPhrase(String catchPhrase) { this.catchPhrase = catchPhrase; }
        
        public String getBs() { return bs; }
        public void setBs(String bs) { this.bs = bs; }
        
        @Override
        public String toString() {
            return "Company{name='" + name + "', catchPhrase='" + catchPhrase + "', bs='" + bs + "'}";
        }
    }
}
