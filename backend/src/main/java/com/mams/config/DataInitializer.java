package com.mams.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mams.entity.AssetBalance;
import com.mams.entity.Base;
import com.mams.entity.EquipmentType;
import com.mams.entity.Role;
import com.mams.entity.User;
import com.mams.repository.AssetBalanceRepository;
import com.mams.repository.BaseRepository;
import com.mams.repository.EquipmentTypeRepository;
import com.mams.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(BaseRepository bases, EquipmentTypeRepository types, UserRepository users,
            AssetBalanceRepository balances, PasswordEncoder encoder) {
        return args -> {
            Base bangalore = bases.findAll().stream().filter(b -> b.getName().equals("Bangalore Base")).findFirst().orElseGet(() -> bases.save(new Base("Bangalore Base", "Bangalore")));
            Base chennai = bases.findAll().stream().filter(b -> b.getName().equals("Chennai Base")).findFirst().orElseGet(() -> bases.save(new Base("Chennai Base", "Chennai")));
            Base hyderabad = bases.findAll().stream().filter(b -> b.getName().equals("Hyderabad Base")).findFirst().orElseGet(() -> bases.save(new Base("Hyderabad Base", "Hyderabad")));

            EquipmentType vehicle = types.findAll().stream().filter(t -> t.getName().equals("Vehicle")).findFirst().orElseGet(() -> types.save(new EquipmentType("Vehicle", "Transport")));
            EquipmentType weapon = types.findAll().stream().filter(t -> t.getName().equals("Weapon")).findFirst().orElseGet(() -> types.save(new EquipmentType("Weapon", "Arms")));
            EquipmentType ammo = types.findAll().stream().filter(t -> t.getName().equals("Ammunition")).findFirst().orElseGet(() -> types.save(new EquipmentType("Ammunition", "Consumable")));
            EquipmentType radio = types.findAll().stream().filter(t -> t.getName().equals("Communication Equipment")).findFirst().orElseGet(() -> types.save(new EquipmentType("Communication Equipment", "Communication")));

            createUser(users, encoder, "admin", "Admin@123", "System Administrator", Role.ADMIN, null);
            createUser(users, encoder, "commander", "Commander@123", "Bangalore Base Commander", Role.BASE_COMMANDER, bangalore);
            createUser(users, encoder, "logistics", "Logistics@123", "Logistics Officer", Role.LOGISTICS_OFFICER, bangalore);

            seedBalance(balances, bangalore, vehicle, 100);
            seedBalance(balances, bangalore, weapon, 250);
            seedBalance(balances, bangalore, ammo, 1000);
            seedBalance(balances, bangalore, radio, 80);
            seedBalance(balances, chennai, vehicle, 80);
            seedBalance(balances, chennai, weapon, 180);
            seedBalance(balances, chennai, ammo, 800);
            seedBalance(balances, chennai, radio, 60);
            seedBalance(balances, hyderabad, vehicle, 60);
            seedBalance(balances, hyderabad, weapon, 140);
            seedBalance(balances, hyderabad, ammo, 700);
            seedBalance(balances, hyderabad, radio, 50);
        };
    }

    private void createUser(UserRepository repo, PasswordEncoder enc, String username, String password, String fullName, Role role, Base base) {
        if (repo.findByUsername(username).isEmpty()) {
            User u = new User();
            u.setUsername(username);
            u.setPassword(enc.encode(password));
            u.setFullName(fullName);
            u.setRole(role);
            u.setBase(base);
            repo.save(u);
        }
    }

    private void seedBalance(AssetBalanceRepository repo, Base b, EquipmentType t, int opening) {
        if (repo.findByBaseIdAndEquipmentTypeId(b.getId(), t.getId()).isEmpty()) {
            AssetBalance x = new AssetBalance();
            x.setBase(b);
            x.setEquipmentType(t);
            x.setOpeningBalance(opening);
            repo.save(x);
        }
    }
}
