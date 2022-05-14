package com.pickSlip.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.entity.LocationMaster;

public interface LocationMasterService {

	void uploadFile(RedirectAttributes redir, MultipartFile file);

	List<LocationMaster> getLocationData();

	ResponseEntity<Resource> exportLocationMaster();

}
