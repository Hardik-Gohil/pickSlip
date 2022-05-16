package com.pickSlip.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface LRSlipService {

	ResponseEntity<Resource> lrSlipUploadPrint(RedirectAttributes redir, MultipartFile file);

}
