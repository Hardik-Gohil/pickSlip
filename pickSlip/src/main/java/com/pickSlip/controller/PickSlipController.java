package com.pickSlip.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.entity.PickSlip;
import com.pickSlip.service.PickSlipService;

@Controller
public class PickSlipController {

	@Autowired
	PickSlipService pickSlipService;

	@GetMapping({ "/", "/pick-slip" })
	public String pickSlip(Model model) {
		return "redirect:/tab/1";
	}

	@GetMapping("/tab/{tabId}")
	public String pickSlipTab(Model model, @PathVariable("tabId") String tabId) {
		pickSlipService.pickSlipTab(model, tabId);
		return "tabs";
	}

	@PostMapping("/pick-slip-upload")
	public String uploadFile(RedirectAttributes redir, @RequestParam("file") MultipartFile file) {
		pickSlipService.uploadFile(redir, file);
		return "redirect:/tab/1";
	}

	@PostMapping("/get-pick-slips")
	@ResponseBody
	public DataTablesOutput<PickSlip> getPickSlips(@RequestBody DataTablesInput input, @RequestParam("systemStatus") Integer systemStatus) {
		return pickSlipService.getPickSlips(input, systemStatus);
	}

	@PostMapping("/pick-slip-update-status")
	public String pickSlipUpdateStatus(@RequestParam("pickSlipNos") String pickSlipNos, @RequestParam("status") Integer status, @RequestParam("tabId") String tabId) {
		pickSlipService.pickSlipUpdateStatus(pickSlipNos, status);
		return "redirect:/tab/" + tabId;
	}

	@GetMapping("/print")
	public ResponseEntity<Resource> getPickSlipDataPrint(ModelMap modelMap, @RequestParam("pickSlipNo") String pickSlipNo, HttpServletRequest request, HttpServletResponse respons) {
		return pickSlipService.getPickSlipDataPrint(modelMap, pickSlipNo, request, respons);
	}

	@GetMapping("/export-pick-slip-data")
	public ResponseEntity<Resource> exportPickSlipData(@RequestParam("systemStatus") Integer systemStatus) {
		return pickSlipService.exportPickSlipData(systemStatus);
	}

	@GetMapping("/upload-photos")
	public String uploadPhotos(Model model, @RequestParam("pickSlipNo") String pickSlipNo) {
		pickSlipService.uploadPhotos(model, pickSlipNo);
		return "UploadPhotos";
	}

	@PostMapping("/upload-photos")
	@ResponseBody
	public String uploadPhotos(@RequestParam("pickSlipNo") String pickSlipNo, @RequestParam("files[]") MultipartFile[] files) {
		return pickSlipService.uploadPhotos(pickSlipNo, files);
	}

	@GetMapping(value = "/downloadZip", produces = "application/zip")
	public ResponseEntity<ByteArrayResource> downloadZip(HttpServletResponse response, @RequestParam("pickSlipNo") String pickSlipNo) {
		return pickSlipService.downloadZip(response, pickSlipNo);
	}
}
