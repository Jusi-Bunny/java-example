package com.example.minio;

import io.minio.*;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@SpringBootTest
class MinioApplicationTests {

    @Test
    void main() {

        try (MinioClient minioClient = createMinioClient()) {

            // 判断 test 桶是否存在，没有就创建
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket("test")
                    .build());
            if (!found) {
                // 创建 test 桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket("test")
                        .build());
                // 设置 test 桶的访问权限（JSON 格式）
                String policy = """
                        {
                          "Statement" : [ {
                            "Action" : "s3:GetObject",
                            "Effect" : "Allow",
                            "Principal" : "*",
                            "Resource" : "arn:aws:s3:::test/*"
                          } ],
                          "Version" : "2012-10-17"
                        }""";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket("test")
                        .config(policy)
                        .build());
            } else {
                System.out.println("【MinIO】Bucket 'test' 已存在");
            }

            // 对上传的图片进行重命名
            String prefix = UUID.randomUUID().toString().replace("-", "");
            // 要上传的图片的路径
            ClassPathResource resource = new ClassPathResource("test.png");
            String originalFilename = resource.getFile().getAbsolutePath();
            System.out.println("【MinIO】源文件：" + originalFilename);
            // 文件的后缀名（文件格式）
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = prefix + suffix;

            System.out.println("【MinIO】文件重命名：" + newFilename);

            // 上传本地图片
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket("test")
                    .filename(originalFilename)
                    .object(newFilename)
                    .build());
            System.out.println("【MinIO】上传成功");
        } catch (Exception e) {
            System.out.println("【MinIO】上传失败：" + e);
        }
    }

    /**
     * 构造 MinIO Client
     * endpoint() 方法接收端点
     * credentials() 方法接收 Access Key 和 Secret Key 两个参数，这里直接使用管理员的账号和密码
     *
     */
    private MinioClient createMinioClient() {
        return MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("admin", "XTMQO9BV5Z3QL38R385Z6O1V7Q9QOQ4U")
                .build();
    }
}
