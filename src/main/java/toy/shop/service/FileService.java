package toy.shop.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    /**
     * 파일을 업로드하는 메서드입니다. 주어진 파일 데이터를 UUID를 기반으로 고유한 파일 이름을 생성한 후
     * 지정된 경로에 저장합니다.
     *
     * @param uploadPath 파일이 저장될 경로 (디렉토리 경로)
     * @param originalFileName 원본 파일 이름 (확장자를 포함)
     * @param fileData 업로드할 파일의 데이터 (바이트 배열)
     * @return 저장된 파일의 고유한 이름 (UUID + 확장자)
     * @throws IOException 파일 저장 중 입출력 예외가 발생할 경우
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException {
        UUID uuid = UUID.randomUUID();
        String expansion = originalFileName.substring(originalFileName.lastIndexOf("."));

        String saveFileName = uuid + expansion;
        String uploadFullUrl = uploadPath + saveFileName;

        FileOutputStream fos = new FileOutputStream(uploadFullUrl);
        fos.write(fileData);
        fos.close();

        return saveFileName;
    }

    /**
     * 파일을 삭제하는 메서드입니다. 지정된 경로와 파일 이름을 사용하여 해당 파일이 존재하면 삭제합니다.
     *
     * @param uploadPath 파일이 저장된 경로 (디렉토리 경로)
     * @param fileName 삭제할 파일의 이름
     */
    public void deleteFile(String uploadPath, String fileName) {
        String deleteFileName = uploadPath + fileName;
        File deleteFile = new File(deleteFileName);

        if (!deleteFile.exists() || !deleteFile.delete()) {
            throw new RuntimeException("이미지를 찾을 수 없거나 삭제할 수 없습니다.");
        }

        deleteFile.delete();
    }

    /**
     * 파일을 임시 저장소에서 메인 저장소로 이동하는 메서드입니다.
     *
     * @param fromPath 원본 파일 경로 (임시 저장소)
     * @param toPath 대상 파일 경로 (메인 저장소)
     * @param fileName 이동할 파일의 이름
     */
    public void moveFile(String fromPath, String toPath, String fileName) {
        File fromFile = new File(fromPath + fileName);
        File toFile = new File(toPath + fileName);

        if (!fromFile.exists()) {
            throw new RuntimeException("이동할 파일이 존재하지 않습니다.");
        }

        if (!fromFile.renameTo(toFile)) {
            throw new RuntimeException("파일을 이동할 수 없습니다.");
        }
    }
}
