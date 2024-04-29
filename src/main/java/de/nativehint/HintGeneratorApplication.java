package de.nativehint;

import de.nativehint.service.HintGenerationDiffService;
import de.nativehint.service.HintGenerationFolderService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class HintGeneratorApplication {


	public static void main(String[] args) throws IOException, URISyntaxException {
		//SpringApplication.run(HintsgeneratorApplication.class, args);

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("de.nativehint");
		context.refresh();

		HintGenerationDiffService hintGenerationDiffService = context.getBean(HintGenerationDiffService.class);
		hintGenerationDiffService.run();
		//hintGenerationDiffService.runFromBasePath();

		HintGenerationFolderService hintGenerationFolderService = context.getBean(HintGenerationFolderService.class);
		//hintGenerationFolderService.run();

		context.close();
	}

}
