package de.nativehint;

import de.nativehint.service.HintGenerationService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class HintGeneratorApplication {


	public static void main(String[] args) throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("de.nativehint");
		context.refresh();

		HintGenerationService hintGenerationService = context.getBean(HintGenerationService.class);
		hintGenerationService.run();

		context.close();
	}

}
