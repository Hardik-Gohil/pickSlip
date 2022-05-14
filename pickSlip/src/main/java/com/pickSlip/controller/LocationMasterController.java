package com.pickSlip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.entity.LocationMaster;
import com.pickSlip.service.LocationMasterService;

@Controller
public class LocationMasterController {

	@Autowired
	LocationMasterService locationMasterService;

	@PostMapping("/location-upload")
	public String uploadFile(RedirectAttributes redir, @RequestParam("file") MultipartFile file) {
		locationMasterService.uploadFile(redir, file);
		return "redirect:/tab/3";
	}

	@GetMapping("/location-data")
	@ResponseBody
	public List<LocationMaster> getLocationData() {
		return locationMasterService.getLocationData();
	}

	@GetMapping("/export-location-master")
	public ResponseEntity<Resource> exportLocationMaster() {
		return locationMasterService.exportLocationMaster();
	}
}
