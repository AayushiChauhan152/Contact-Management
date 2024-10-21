package com.scm.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageService {

	private Cloudinary cloudinary;

	public ImageService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	public String uploadImage(MultipartFile file) {
		String fileName = UUID.randomUUID().toString();
		try {
			byte[] data = new byte[file.getInputStream().available()];

			file.getInputStream().read(data);

			cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", fileName));
			return this.getUrlFromPublicId(fileName);

		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	public String getUrlFromPublicId(String publicId) {
		return cloudinary.url().transformation(new Transformation<>().width(500).height(500).crop("fill"))
				.generate(publicId);
	}

}
