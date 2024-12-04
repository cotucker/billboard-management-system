package com.example.billboard.controller;

import com.example.billboard.dto.AdvertisementDto;
import com.example.billboard.dto.DeviceDto;
import com.example.billboard.service.AdvertisementService;
import com.example.billboard.service.DeviceService;
import com.example.billboard.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final DeviceService deviceService;
    private final AdvertisementService advertisementService;
    private final MediaFileService mediaFileService;
    @GetMapping("/")
    public String homePage(Model model) {
        List<DeviceDto> ungroupedDevices = deviceService.findAllDevicesWithAdvertisement();
        model.addAttribute("ungroupedDevices", ungroupedDevices);
        model.addAttribute("ads", this.advertisementService);
        model.addAttribute("mediaFileService", this.mediaFileService);
        return "home-page";
    }
    @GetMapping("/device/{deviceid}")
    public String redirectToAdvertisementDisplay(@PathVariable("deviceid") long deviceid, Model model){
        List<AdvertisementDto> advertisements = advertisementService.findAdvertisementsByHostDeviceId(deviceid);
        if(advertisements.isEmpty())
            return "redirect:/";
        return "redirect:/device/{deviceid}/" + advertisements.get(0).getId();
    }
    @GetMapping("/device/{deviceid}/{adId}")
    public String displayAdvertisement(@PathVariable("deviceid") long deviceid
            , @PathVariable(name = "adId", required = false) Long adId
            , Model model) {
        model.addAttribute("advertisement", advertisementService.findAdvertisementById(adId));
        model.addAttribute("deviceId", deviceid);
        return "ad-display";
    }
    @PostMapping("/device/{deviceid}/{adId}")
    public String sentNextAdvertisement(@PathVariable("deviceid") long deviceid
            , @PathVariable(name = "adId", required = false) Long adId) {
        List<AdvertisementDto> advertisements = advertisementService.findAdvertisementsByHostDeviceId(deviceid);
        Long nextAdvertisementId = advertisementService.getNextAdvertisementId(advertisements, adId);
        return "redirect:/device/{deviceid}/" + nextAdvertisementId;
    }
    @GetMapping("/video/{adid}")
    public ResponseEntity<byte[]> displayVideo(@PathVariable(name = "adid", required = false) Long adId) {
        byte[] videoBytes = advertisementService.getMediaFileDataByAdvrtisementId(adId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mp4");
        headers.add("Content-Disposition", "inline; filename=video.mp4");
        return new ResponseEntity<>(videoBytes, headers, HttpStatus.OK);
    }
}
