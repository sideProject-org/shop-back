package toy.shop.cmmn.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@ConfigurationProperties(prefix = "path")
public class TempImageDeleteScheduler {

    // TMP 디렉토리 경로 찾아오기
    private List<String> tmpDirectories;

    public List<String> getTmpDirectories() {
        return tmpDirectories;
    }

    public void setTmpDirectories(List<String> tmpDirectories) {
        this.tmpDirectories = tmpDirectories;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteTempImage() {
        if (tmpDirectories != null && !tmpDirectories.isEmpty()) {
            for (String directoryPath : tmpDirectories) {
                File directory = new File(directoryPath);
                if (directory.exists() && directory.isDirectory()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            try {
                                if (isOlderThan24Hours(file)) {
                                    if (file.delete()) {
                                        log.info("삭제 완료 {}", file.getName());
                                    } else {
                                        log.info("삭제 실패 {}", file.getName());
                                    }
                                } else {
                                    log.info("24시간이 지난 파일이 존재하지 않습니다.");
                                }
                            } catch (Exception e) {
                                log.error("Error deleting file {} - {}", file.getName(), e);
                            }
                        }
                    } else {
                        log.info("삭제할 파일이 존재하지 않습니다. {}", directoryPath);
                    }
                } else {
                    log.info("Directory not found or not readable: {}", directoryPath);
                }
            }
        } else {
            log.info("No tmp directories configured");
        }
    }

    private boolean isOlderThan24Hours(File file) {
        try {
            Instant fileCreationTime = Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toInstant();
            Instant twentyFourHoursAgo = Instant.now().minusSeconds(24 * 60 * 60);

            return fileCreationTime.isBefore(twentyFourHoursAgo);
        } catch (Exception e) {
            log.error("Could not retrieve creation time for: {} - {}",file.getName(), e.getMessage());

            return false;
        }
    }
}
