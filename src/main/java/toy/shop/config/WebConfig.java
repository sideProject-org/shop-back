package toy.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${path.profileImage}")
    private String profileImagePath;

    @Value("${path.noticeTmpImage}")
    private String noticeTmpImagePath;

    @Value("${path.noticeImage}")
    private String noticeImagePath;

    @Value("${path.boardTmpImage}")
    private String boardTmpImagePath;

    @Value("${path.boardImage}")
    private String boardImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/profileImage/**")
                .addResourceLocations("file:" + profileImagePath + "/");

        registry.addResourceHandler("/images/noticeTmpImage/**")
                .addResourceLocations("file:" + noticeTmpImagePath + "/");

        registry.addResourceHandler("/images/noticeImage/**")
                .addResourceLocations("file:" + noticeImagePath + "/");

        registry.addResourceHandler("/images/boardTmpImage/**")
                .addResourceLocations("file:" + boardTmpImagePath + "/");

        registry.addResourceHandler("/images/boardImage/**")
                .addResourceLocations("file:" + boardImagePath + "/");
    }
}
