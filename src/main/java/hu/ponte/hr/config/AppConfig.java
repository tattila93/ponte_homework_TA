package hu.ponte.hr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

/**
 * @author zoltan
 */
@Configuration
public class AppConfig
{
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(Locale.ENGLISH);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		//the size of the uploaded file could be modified here also  --> (bytes) 	2,097,152 = 2MB
		multipartResolver.setMaxUploadSize(100000000);
		return multipartResolver;
	}


}
