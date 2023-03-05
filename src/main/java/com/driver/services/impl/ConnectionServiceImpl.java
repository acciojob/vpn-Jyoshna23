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

        if(user.getMaskedIp()!=null){
            throw new Exception("Already connected");
        }
        else if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }
        else {
                if (user.getServiceProviderList() == null) {
                    throw new Exception("Unable to connect");
                }

               List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
                int id = Integer.MAX_VALUE;
                ServiceProvider serviceProvider = null;
                Country country = null;

                for(ServiceProvider serviceProvider1 : serviceProviderList){
                    List<Country> countryList = serviceProvider1.getCountryList();
                     for(Country country1 : countryList){
                        if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && id > serviceProvider1.getId()){
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

                    serviceProvider.getConnectionList().add(connection);

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
        }
            user.setMaskedIp(null);
            user.setConnected(false);
//            List<Connection> connectionList = user.getConnectionList();
//            connectionList.remove(user.getId());
            userRepository2.save(user);

        return user;
    }


    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        User sender = userRepository2.findById(senderId).get();
        User receiver = userRepository2.findById(receiverId).get();

        if(receiver.getMaskedIp() != null) {
            String receiverCurrentCountry = receiver.getMaskedIp();

            String code = receiverCurrentCountry.substring(0, 3);
            if (code.equals(sender.getOriginalCountry().getCode())) {
                //sender is already in the same country no need to connect vpn
                return sender;
            } else {
                String countryName = "";

                if (code.equalsIgnoreCase(CountryName.IND.toString())) {
                    countryName = CountryName.IND.toString();
                }
                if (code.equalsIgnoreCase(CountryName.USA.toString())) {
                    countryName = CountryName.USA.toString();
                }
                if (code.equalsIgnoreCase(CountryName.AUS.toString())) {
                    countryName = CountryName.AUS.toString();
                }
                if (code.equalsIgnoreCase(CountryName.CHI.toString())) {
                    countryName = CountryName.CHI.toString();
                }
                if (code.equalsIgnoreCase(CountryName.JPN.toString())) {
                    countryName = CountryName.JPN.toString();
                }


                // sender needs to connect to suitable vpn
                User sender1 = connect(senderId, countryName);

                if (!sender1.getConnected()) {
                    throw new Exception("Cannot establish communication");
                } else return sender1;

             }
        }else{
            if(receiver.getOriginalCountry().equals(sender.getOriginalCountry())){
                return sender;
            }
            String countryName = receiver.getOriginalCountry().getCountryName().toString();
            User sender1 =  connect(senderId,countryName);
            if (!sender1.getConnected()){
                throw new Exception("Cannot establish communication");
            }
            else return sender1;
        }
    }
}

