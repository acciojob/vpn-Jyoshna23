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
        User user = userRepository2.findById(userId).get();
        if(!user.getConnected()){
            throw new Exception("Already disconnected");
        }else{
            user.setMaskedIp(null);
            user.setConnected(false);
            List<Connection> connectionList = user.getConnectionList();
            connectionList.remove(user.getId());
            userRepository2.save(user);
        }
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        User sender = userRepository2.findById(senderId).get();
        User receiver = userRepository2.findById(receiverId).get();

       if(receiver.getConnected()){
           String receiverCurrentCountry = receiver.getOriginalCountry().toString();
           if(sender.getOriginalCountry().toString().equals(receiverCurrentCountry)){
               //sender is already in the same country no need to connect vpn
               return sender;
           }
       }else{
           if (sender.getServiceProviderList() == null) {
               throw new Exception("Cannot establish communication");
           }
           // sender needs to connect to suitable vpn
           List<ServiceProvider> serviceProviderList = sender.getServiceProviderList();
           int id = Integer.MAX_VALUE;
           ServiceProvider serviceProvider = null;
           Country country = null;
           for(ServiceProvider serviceProvider1 : serviceProviderList){
               for(Country country1 : serviceProvider.getCountryList()){
                   if(id > serviceProvider1.getId()){
                       id = serviceProvider1.getId();
                       country = country1;
                       serviceProvider = serviceProvider1;
                   }
               }
           }

           if(serviceProvider != null){
               Connection connection = new Connection();
               connection.setUser(sender);
               connection.setServiceProvider(serviceProvider);

               String updatedMaskedIp =   country.getCode() + "." + serviceProvider.getId() + "." + sender.getId();

               sender.setMaskedIp(updatedMaskedIp);
               sender.setConnected(true);
               sender.getConnectionList().add(connection);


               userRepository2.save(sender);
               serviceProviderRepository2.save(serviceProvider);
           }



       }
        return sender;
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