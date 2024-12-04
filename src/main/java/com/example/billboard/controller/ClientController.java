package com.example.billboard.controller;

import com.example.billboard.dto.AdvertisementDto;
import com.example.billboard.dto.DeviceDto;
import com.example.billboard.dto.MediaFileDto;
import com.example.billboard.model.User;
import com.example.billboard.model.enums.Action;
import com.example.billboard.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ClientController {
    private final UserService userService;
    private final DeviceService deviceService;
    private final AdvertisementService advertisementService;
    private final LogService logService;
    private final MediaFileService mediaFileService;
    @GetMapping("/{userid}")
    public String userPage(@PathVariable("userid") long userid, Model model) {
        User user = userService.findUserById(userid);
        List<DeviceDto> ungroupedDevices = deviceService.findUngroupedDevices(userid);
        DeviceDto newDevice = new DeviceDto();
        model.addAttribute("ads", this.advertisementService);
        model.addAttribute("userService", this.userService);
        model.addAttribute("deviceService", this.deviceService);
        model.addAttribute("mediaFileService", this.mediaFileService);
        model.addAttribute("user", user);
        model.addAttribute("ungroupedDevices", ungroupedDevices);
        model.addAttribute("newDevice", newDevice);
        Map<Long, List<DeviceDto>> devicesGroupedByGroupNumber = deviceService.getDevicesGroupedByGroupNumber(userid);
        model.addAttribute("devicesGroupedByGroupNumber", devicesGroupedByGroupNumber);
        return "user-page";
    }
    @PostMapping("/{userid}")
    public String saveEditedUser(@PathVariable("userid") long userid,
                                 @RequestParam(name = "selectedDevices", required = false) List<Long> selectedDeviceIds,
                                 @ModelAttribute("newDevice") DeviceDto newDevice) {
        deviceService.groupDevicesById(selectedDeviceIds);
        User oldUser = userService.findUserById(userid);
        if(!newDevice.getName().isEmpty()) {
            logService.saveLog(oldUser, LocalDateTime.now(), Action.NEW_DEVICE);
        }
        deviceService.addDevice(newDevice, oldUser);
        logService.saveLog(oldUser, LocalDateTime.now(), Action.NEW_DEVICE);
        return "redirect:/api/user/{userid}";
    }
    @GetMapping("/{userid}/schedule")
    public String editSchedule(@PathVariable("userid") long userid, Model model) {
        User user = userService.findUserById(userid);
        List<DeviceDto> ungroupedDevices = deviceService.findUngroupedDevices(userid);
        List<MediaFileDto> files =  mediaFileService.finalAllByOwnerId(userid);
        model.addAttribute("ads", this.advertisementService);
        model.addAttribute("userService", this.userService);
        model.addAttribute("deviceService", this.deviceService);
        model.addAttribute("mediaFileService", this.mediaFileService);
        model.addAttribute("user", user);
        model.addAttribute("ungroupedDevices", ungroupedDevices);
        model.addAttribute("files", files);
        model.addAttribute("fileService", mediaFileService);
        Map<Long, List<DeviceDto>> devicesGroupedByGroupNumber = deviceService.getDevicesGroupedByGroupNumber(userid);
        model.addAttribute("devicesGroupedByGroupNumber", devicesGroupedByGroupNumber);
        return "user-schedule";
    }
    @PostMapping("/{userid}/schedule")
    public String saveEditedSchedule(@PathVariable("userid") long userid,
                                     @RequestParam(name = "selectedDevices", required = false) List<Long> selectedDeviceIds,
                                     @RequestParam(name = "selectedDevicesGroup", required = false) List<Long> selectedDeviceGroupIds,
                                     @RequestParam(name = "selectedFiles", required = false) List<Long> selectedFilesIds){
        deviceService.assignAdverstismentToDevice(selectedDeviceGroupIds, selectedDeviceIds, selectedFilesIds);
        if((selectedDeviceIds != null || selectedDeviceGroupIds != null && selectedFilesIds != null)){
            logService.saveLog(userService.findUserById(userid), LocalDateTime.now(), Action.NEW_SCHEDULE);
        }
        return "redirect:/api/user/{userid}";
    }
    @GetMapping("/{userid}/devices")
    public String editDevices(@PathVariable("userid") long userid, Model model) {
        List<DeviceDto> ungroupedDevices = deviceService.findUngroupedDevices(userid);
        Map<Long, List<DeviceDto>> devicesGroupedByGroupNumber = deviceService.getDevicesGroupedByGroupNumber(userid);
        model.addAttribute("ads", this.advertisementService);
        model.addAttribute("mediaFileService", this.mediaFileService);
        model.addAttribute("ungroupedDevices", ungroupedDevices);
        model.addAttribute("devicesGroupedByGroupNumber", devicesGroupedByGroupNumber);
        model.addAttribute("user", userService.findUserById(userid));
        return "device-edit";
    }
    @PostMapping("/{userid}/devices")
    public String deleteDevices(@PathVariable("userid") long userid,
                                 @RequestParam(name = "selectedDevices", required = false) List<Long> selectedDeviceIds) {
        if(selectedDeviceIds != null) {
            logService.saveSetOfLog(userService.findUserById(userid), LocalDateTime.now(), Action.DELETE_DEVICE, selectedDeviceIds.size());
        }
        deviceService.deleteSelectedDevices(selectedDeviceIds);
        return "redirect:/api/user/{userid}/devices";
    }
    @GetMapping("/{userid}/files")
    public String editFiles(@PathVariable("userid") long userid, Model model) {
        User user = userService.findUserById(userid);
        List<MediaFileDto> files =  mediaFileService.finalAllByOwnerId(userid);
        model.addAttribute("user", user);
        model.addAttribute("files", files);
        model.addAttribute("fileService", mediaFileService);
        return "user-files";
    }
    @PostMapping("/{userid}/files")
    public String saveEditedFiles(@PathVariable("userid") long userid,
                                  @RequestParam(name = "selectedFiles", required = false) List<Long> selectedFilesIds,
                                  @RequestParam(name = "files", required = false) MultipartFile[] files) {
        mediaFileService.deleteSelectedFiles(selectedFilesIds);
        mediaFileService.uploadFiles(files, userService.findUserById(userid));
        return "redirect:/api/user/{userid}/files";
    }
    @GetMapping("/{userid}/device/{deviceid}/{adId}")
    public String displayAdvertisement(@PathVariable(name = "userid", required = false) long userid, @PathVariable("deviceid") long deviceid
            , @PathVariable(name = "adId", required = false) Long adId
            , Model model) {
        List<AdvertisementDto> advertisements = advertisementService.findAdvertisementsByHostDeviceId(deviceid);
        User user = userService.findUserById(userid);
        model.addAttribute("advertisement", advertisementService.findAdvertisementById(adId));
        model.addAttribute("user", user);
        model.addAttribute("deviceId", deviceid);
        return "ad-display";
    }
    @PostMapping("/{userid}/device/{deviceid}/{adId}")
    public String sentNextAdvertisement(@PathVariable("userid") long userid
            , @PathVariable("deviceid") long deviceid
            , @PathVariable(name = "adId", required = false) Long adId) {
        List<AdvertisementDto> advertisements = advertisementService.findAdvertisementsByHostDeviceId(deviceid);
        Long nextAdvertisementId = advertisementService.getNextAdvertisementId(advertisements, adId);


        return "redirect:/api/user/{userid}/device/{deviceid}/" + nextAdvertisementId;
    }
    @GetMapping("/{userid}/video/{adid}")
    public ResponseEntity<byte[]> displayVideo(@PathVariable("userid") long userid
            , @PathVariable(name = "adid", required = false) Long adId) {
        byte[] videoBytes = advertisementService.getMediaFileDataByAdvrtisementId(adId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mp4");
        headers.add("Content-Disposition", "inline; filename=video.mp4");
        return new ResponseEntity<>(videoBytes, headers, HttpStatus.OK);
    }
}
