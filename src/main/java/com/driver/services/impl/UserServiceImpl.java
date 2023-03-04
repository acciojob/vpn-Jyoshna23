package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Country country = new Country();

       if(countryName.equals(CountryName.AUS)){
           country.setCountryName(CountryName.AUS);
           country.setCode(CountryName.AUS.toCode());
       }else if(countryName.equals(CountryName.CHI)){
           country.setCountryName(CountryName.CHI);
           country.setCode(CountryName.CHI.toCode());
       }else if(countryName.equals(CountryName.IND)){
           country.setCountryName(CountryName.IND);
           country.setCode(CountryName.IND.toCode());
       } else if (countryName.equals(CountryName.JPN)) {
           country.setCountryName(CountryName.JPN);
           country.setCode(CountryName.JPN.toCode());
       }else {
           country.setCountryName(CountryName.USA);
           country.setCode(CountryName.USA.toCode());
       }

       user.setCountry(country);
       int user_Id = user.getUserId();
       int code = Integer.parseInt(country.getCode());
       user.setOriginalIp(code +"."+user_Id);

       user.setConnected(false);
       user.setMaskedIp(null);

        userRepository3.save(user);

       return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user = userRepository3.findById(userId).get();

        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        serviceProviderList.add(serviceProvider);

        return user;
    }
}
