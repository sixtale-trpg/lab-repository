package org.infinity.sixtalebackend.infra.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.MultipartUpload;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * S3 파일 업로드 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3 이미지 생성
     * @param multipartFiles
     * @param dirName
     * @return
     * @throws IOException
     */
    @Transactional
    public List<String> upload(MultipartFile[] multipartFiles, String dirName) throws IOException
    {

        // 파일 리스트 하나씩 업로드
                            List<String> listUrl = new ArrayList<>();
            for(MultipartFile mf: multipartFiles) {
                String contentType = mf.getContentType();

                // 파일 확장자 체크
                if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                    throw new IllegalArgumentException("Invalid file content type: " + contentType);
                }

                File uploadFile = convert(mf)
                        .orElseThrow(()-> new IllegalArgumentException("MultipartFile -> File로 전환이 실패"));

            // 파일명 중복을 피하기 위해 날짜 추가
            String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy-MM-dd_HH-mm-ss"));
            String fileName = dirName + formatDate + "_" + uploadFile.getName();


            // put - S3로 업로드
            String uploadImageUrl = putS3(uploadFile, fileName, contentType);
            // 로컬 파일 삭제
            removeFile(uploadFile);

            listUrl.add(uploadImageUrl);

        }
        return listUrl;
    }

    /**
     * 파일 변환 함수
     * @param file
     * @return
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convertFile);
        return Optional.of(convertFile);
    }

    /**
     * S3 업로드
     * @param uploadFile
     * @param fileName
     * @return
     */
    private String putS3(File uploadFile, String fileName,String contentType) {
        try (FileInputStream fis = new FileInputStream(uploadFile)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .acl("public-read")
                    .contentType(contentType) // Content-Type 설정
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fis, uploadFile.length()));

            return s3Client.utilities().getUrl(b -> b.bucket(bucket).key(fileName)).toExternalForm();
        } catch (IOException e) {
            log.error("파일 읽기 오류", e);
            throw new RuntimeException("파일 읽기 실패", e);
        } catch (S3Exception e) {
            log.error("S3 업로드 중 에러 발생: {}", e.awsErrorDetails().errorMessage(), e);
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    /**
     * 로컬 파일 삭제
     * @param targetFile
     */
    private void removeFile(File targetFile) {
        if (targetFile.exists()) {
            if (targetFile.delete()) {
                log.info("파일이 삭제");
            } else {
                log.info("파일이 삭제 실패");
            }
        }
    }

    /**
     * S3 이미지 삭제
     * @param fileName
     */
    public void delete(String fileName) {
        try {
            String decodedFileName = java.net.URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            log.info("Deleting file: {}", decodedFileName);
            s3Client.deleteObject(b -> b.bucket(bucket).key(decodedFileName));
        } catch (S3Exception e) {
            log.error("S3 파일 삭제 중 에러 발생: {}", e.awsErrorDetails().errorMessage(), e);
            throw new RuntimeException("S3 파일 삭제 실패", e);
        }
    }
}
