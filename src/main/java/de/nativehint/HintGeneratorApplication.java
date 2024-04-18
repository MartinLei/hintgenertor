package de.nativehint;

import de.nativehint.service.HintGenerationService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class HintGeneratorApplication {


	public static void main(String[] args) throws IOException, URISyntaxException {
		//SpringApplication.run(HintsgeneratorApplication.class, args);

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("de.nativehint");
		context.refresh();

		HintGenerationService hintGenerationService = context.getBean(HintGenerationService.class);
		hintGenerationService.run();

		context.close();
	}

}
