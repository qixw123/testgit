package cn.redcdn.jec;

import javax.servlet.Filter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
@MapperScan(basePackages = { "cn.redcdn.jec" })
public class JIEnterpriseContactServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JIEnterpriseContactServiceApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JIEnterpriseContactServiceApplication.class);
	}
	
	// 用于处理编码问题
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

}
