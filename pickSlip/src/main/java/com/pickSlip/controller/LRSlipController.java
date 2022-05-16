package com.pickSlip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.service.LRSlipService;

@Controller
public class LRSlipController {
	
	@Autowired
	LRSlipService lrSlipService;

	@PostMapping("/lr-slip-upload-print")
	public ResponseEntity<Resource> lrSlipUploadPrint(RedirectAttributes redir, @RequestParam("file") MultipartFile file) {
		return lrSlipService.lrSlipUploadPrint(redir, file);
	}
}
