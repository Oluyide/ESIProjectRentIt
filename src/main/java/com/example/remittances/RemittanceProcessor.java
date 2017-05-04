package com.example.remittances;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

@Service
class RemittanceProcessor {
	public String extractRemittance(MimeMessage msg) throws Exception {
		Multipart multipart = (Multipart) msg.getContent();
		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);
			if (bodyPart.getContentType().contains("json") &&
				bodyPart.getFileName().startsWith("remittance"))
				return IOUtils.toString(bodyPart.getInputStream(), "UTF-8");
		}
		throw new Exception("oops");
	}
}

