package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_provider")
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceProviderId;

    private String name;


    //Mapping the relationship between admin and serviceProvider
    @ManyToOne
    @JoinColumn
    private Admin admin;

    //Connection and service provider
    @OneToMany(mappedBy = "serviceProvider",cascade = CascadeType.ALL)
    List<Connection> connectionList = new ArrayList<>();

    //Country and service provider
    @OneToMany(mappedBy = "serviceProvider",cascade = CascadeType.ALL)
    List<Country> countryList = new ArrayList<>();

    //Many - Many relationship between User and Service Provider
    @ManyToMany
    @JoinTable(name = "serviceProviders_users",
            joinColumns = @JoinColumn(name = "serviceProviderId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> user = new ArrayList<>();



    public ServiceProvider() {
    }

    public ServiceProvider(int id, String name) {
        this.serviceProviderId = serviceProviderId;
        this.name = name;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public int getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
