package com.itheima.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public interface gg01 {
	
	void Hellome(Model model);

	void getListAll(Model model) throws IOException;
}
