package com.eduservices.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


public class Compressor {

    private static final Logger log = LoggerFactory.getLogger(Compressor.class);

    public static int BITE_SIZE=4*1024;

    public static byte[] compressDocument(byte[] data) {
        try{
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            deflater.finish();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] tmp = new byte[BITE_SIZE];

            while(!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp,0, size);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e){
            log.info("Error compressDocument: {}",e.getLocalizedMessage());
            return data;
        }
    }

    public static byte[] decompressDocument(byte[] data) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] tmp = new byte[BITE_SIZE];
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (DataFormatException | IOException e){
            log.info("Error decompressDocument: {}",e.getLocalizedMessage());
            return data;
        }
    }
}
