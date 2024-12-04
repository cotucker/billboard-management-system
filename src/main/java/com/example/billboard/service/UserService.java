package com.example.billboard.service;

import com.example.billboard.dto.AdvertisementDto;
import com.example.billboard.dto.UserDto;
import com.example.billboard.mapper.UserMapper;
import com.example.billboard.model.Advertisement;
import com.example.billboard.model.Device;
import com.example.billboard.model.User;
import com.example.billboard.model.enums.UserRole;
import com.example.billboard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final AdvertisementRepository advertisementRepository;
    private final LogRepository logRepository;
    private final MediaFileRepository mediaFileRepository;
    public List<UserDto> findAllUsers() {
            return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public User findUserById(Long userid) {
        Optional<User> user = userRepository.findById(userid);
        return user.orElseThrow();
    }
        public void editUser(UserDto user, User oldUser, String action) {
            switch (action) {
                case "u":
                    oldUser.setUsername(user.getUsername());
                    oldUser.setPassword(user.getPassword());
                    oldUser.setRole(user.getRole());
                    userRepository.save(oldUser);
                    break;
                case "d":
                    List<Device> devices = deviceRepository.findByOwnerId(oldUser.getId());
                    for(Device device : devices) {

                        List<Advertisement> advertisements = advertisementRepository.findByHostDeviceId(device.getId());
                        advertisementRepository.deleteAll(advertisements);
                    }
                    deviceRepository.deleteAll(devices);
                    logRepository.deleteAll(logRepository.findAllByOwner(oldUser));
                    mediaFileRepository.deleteAll(mediaFileRepository.findAllByOwnerId(oldUser.getId()));
                    userRepository.delete(oldUser);
                    break;
            }
    }

    public User createUser(UserDto user) {
        return userRepository.save(UserMapper.toUser(user));
    }
    public boolean isUsernameExist(UserDto user) {
        return userRepository.findByUsername(user.getUsername()).isPresent();
    }
    public String registerOrLoginUser(UserDto user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
         if(existingUser.isPresent()){
            if(existingUser.get().getRole() == UserRole.ADMIN) {
                return "redirect:/api/admin/users";
            }else {
                if(user.getPassword().equals(existingUser.get().getPassword()))
                {
                    return "redirect:/api/user/" + existingUser.get().getId();
                }else {
                    return "redirect:/login";
                }
            }
        } else {
            return "redirect:/api/user/" + userRepository.save(UserMapper.toUser(user)).getId();
         }
    }
    public int getUsersCount() {
        return userRepository.getUsersCount();
    }
}
