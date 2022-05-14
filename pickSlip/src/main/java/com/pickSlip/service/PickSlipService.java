package com.pickSlip.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.entity.PickSlip;

public interface PickSlipService {

	void pickSlipTab(Model model, String tabId);

	void uploadFile(RedirectAttributes redir, MultipartFile file);

	DataTablesOutput<PickSlip> getPickSlips(DataTablesInput input, Integer systemStatus);
	
	void pickSlipUpdateStatus(String pickSlipNos, Integer status);

	ResponseEntity<Resource> getPickSlipDataPrint(ModelMap modelMap, String pickSlipNo, HttpServletRequest request, HttpServletResponse respons);

	ResponseEntity<Resource> exportPickSlipData(Integer systemStatus);

	void uploadPhotos(Model model, String pickSlipNo);

	String uploadPhotos(String pickSlipNo, MultipartFile[] files);

	ResponseEntity<ByteArrayResource> downloadZip(HttpServletResponse response, String pickSlipNo);

}
