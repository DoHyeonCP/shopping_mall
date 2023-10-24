package com.example.mall.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;


@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)
    throws Exception{
        UUID uuid = UUID.randomUUID(); // 서로다른 개체를 구별하기 위한 ID
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 바이트단위 출력을 내보내는
        fos.write(fileData);
        fos.close();
        return savedFileName; // 업로드된 파일의 이름 반환
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else{
            log.info("파일이 존재하지 않습니다");
        }
        

    }
    
}
