package com.example.billboard.controller;

import com.example.billboard.dto.DeviceDto;
import com.example.billboard.dto.LogDto;
import com.example.billboard.dto.UserDto;
import com.example.billboard.model.User;
import com.example.billboard.model.enums.Action;
import com.example.billboard.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminController {
    private final UserService userService;
    private final DeviceService deviceService;
    private final AdvertisementService advertisementService;
    private final LogService logService;
    private final MediaFileService mediaFileService;
    @GetMapping("")
    public String listUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("userService", this.userService);
        model.addAttribute("adService", this.advertisementService);
        model.addAttribute("deviceService", this.deviceService);
        return "users-list";
    }

    @GetMapping("/{userid}")
    public String editUser(@PathVariable("userid") long userid, Model model) {
        User user = userService.findUserById(userid);
        List<DeviceDto> devices = deviceService.findDevicesByUserId(userid);
        DeviceDto newDevice = new DeviceDto();
        String action = "";
        model.addAttribute("ads", this.advertisementService);
        model.addAttribute("mediaFileService", this.mediaFileService);
        model.addAttribute("user", user);
        model.addAttribute("devices", devices);
        model.addAttribute("newDevice", newDevice);
        model.addAttribute("action", action);
        return "user-edit";
    }
    @PostMapping("/{userid}")
    public String saveEditedUser(@PathVariable("userid") long userid,
                                 @ModelAttribute("user") UserDto user,
                                 @ModelAttribute("action") String action,
                                 @ModelAttribute("newDevice") DeviceDto newDevice) {
        User oldUser = userService.findUserById(userid);
        deviceService.addDevice(newDevice, oldUser);
        logService.saveLog(oldUser, LocalDateTime.now(), Action.NEW_DEVICE);
        userService.editUser(user, oldUser, action);
        return "redirect:/api/admin/users";
    }
    @GetMapping("/create")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-create";
    }
    @PostMapping("/create")
    public String saveCreatedUser(@ModelAttribute("user") UserDto user) {
        if(!userService.isUsernameExist(user)) {
            logService.saveLog(userService.createUser(user), LocalDateTime.now(), Action.NEW_USER);
        }
        return "redirect:/api/admin/users";
    }
    @GetMapping("/logs")
    public String logsMonitor(Model model) {
        List<LogDto> logs = logService.findAllLogs();
        String action = null;
        model.addAttribute("logs", logs);
        model.addAttribute("action", action);
        return "logs";
    }
    @PostMapping("/logs")
    public String processAction(@ModelAttribute("action") String action) {
        return "redirect:/api/admin/users/logs/" + action;
    }
    @GetMapping({"/logs/", "/logs/{action}"})
    public ResponseEntity<Resource> exportLogs(@PathVariable(name = "action", required = false) String action) throws IOException {
        return logService.exportLogsToFile(action);
    }
}


