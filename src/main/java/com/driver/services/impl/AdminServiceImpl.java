package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepository1.save(admin);

        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {

        Admin admin = adminRepository1.findById(adminId).get();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setAdmin(admin);
        serviceProvider.setName(providerName);

        List<ServiceProvider> serviceProviderList = admin.getServiceProviders();
        serviceProviderList.add(serviceProvider);
        adminRepository1.save(admin);
        return admin;
    }



    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {



       if(isValidCountryName(countryName)) {
           ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
           Country country = getCountryName(countryName);
           country.setCountryName(country.getCountryName());
           country.setUser(null);

           country.setServiceProvider(serviceProvider);
           List<Country> countryList = serviceProvider.getCountryList();
           countryList.add(country);
           serviceProviderRepository1.save(serviceProvider);

           return serviceProvider;
       }else{
           throw new Exception("Country not found");
       }
    }

    private static Country getCountryName(String countryName){
        Country country = new Country();
        if(countryName.equalsIgnoreCase(CountryName.AUS.toString())){
            country.setCountryName(CountryName.AUS);
            country.setCode(CountryName.AUS.toCode());
        }else if(countryName.equalsIgnoreCase(CountryName.CHI.toString())){
            country.setCountryName(CountryName.CHI);
            country.setCode(CountryName.CHI.toCode());
        }else if(countryName.equalsIgnoreCase(CountryName.IND.toString())){
            country.setCountryName(CountryName.IND);
            country.setCode(CountryName.IND.toCode());
        } else if (countryName.equalsIgnoreCase(CountryName.JPN.toString())) {
            country.setCountryName(CountryName.JPN);
            country.setCode(CountryName.JPN.toCode());
        }else if (countryName.equalsIgnoreCase(CountryName.USA.toString())){
            country.setCountryName(CountryName.USA);
            country.setCode(CountryName.USA.toCode());
        }

        return country;
    }

    private static boolean isValidCountryName(String code) {
        String[] validCodes = {"ind", "aus", "usa", "chi", "jpn"};
        for (String validCode : validCodes) {
            if (code.equalsIgnoreCase(validCode)) {
                return true;
            }
        }
        return false;
    }
}



