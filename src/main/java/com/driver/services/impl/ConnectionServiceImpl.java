package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
            User user = userRepository2.findById(userId).get();

            if(user.getConnected()){
                throw new Exception("Already connected");
            }else if(user.getOriginalCountry().getCountryName().toString().equals(countryName)){
            return user;
        }else {
                if (user.getServiceProviderList() == null) {
                    throw new Exception("Unable to connect");
                }

               List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
                int id = Integer.MAX_VALUE;
                ServiceProvider serviceProvider = null;
                Country country = null;

                for(ServiceProvider serviceProvider1 : serviceProviderList){
                    for(Country country1 : serviceProvider1.getCountryList()){
                        if(country1.getCountryName().toString().equals(countryName) && id > serviceProvider1.getId()){
                            id = serviceProvider1.getId();
                            country = country1;
                            serviceProvider = serviceProvider1;
                        }
                    }
                }

                if(serviceProvider != null){
                    Connection connection = new Connection();
                    connection.setUser(user);
                    connection.setServiceProvider(serviceProvider);

                    String updatedMaskedIp =   country.getCode() + "." + serviceProvider.getId() + "." + userId;

                    user.setMaskedIp(updatedMaskedIp);
                    user.setConnected(true);
                    user.getConnectionList().add(connection);


                    userRepository2.save(user);
                    serviceProviderRepository2.save(serviceProvider);
                }

            }
        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {

        return null;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        return null;
    }
}


//
//    List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
//    List<User> userList;
//            for(ServiceProvider serviceProvider : serviceProviderList){
//        userList = serviceProvider.getUser();
//        for(User users : userList) {
//            if (users.getUserId() == userId) {
//                throw new Exception("Already connected exception");
//            }
//        }
//    }
//
//    CountryName userCountryName = user.getCountry().getCountryName();
//            if(userCountryName.toString().equals(countryName)){
//
//    }
//
//            for(ServiceProvider serviceProvider :serviceProviderList){
//        userList = serviceProvider.getUser();
//        for(User users : userList) {
//            if (users.getUserId() == userId) {
//                List<Country> countryList = serviceProvider.getCountryList();
//                for(Country country : countryList){
//                    if(country.getCountryName().equals(countryName)){
//                        Connection connection = new Connection();
//                        user.setMaskedIp();
//                    }
//                }
//            }