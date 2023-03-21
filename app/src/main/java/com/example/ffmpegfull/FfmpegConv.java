package com.example.ffmpegfull;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.arthenica.mobileffmpeg.FFmpeg.executeAsync;

import com.arthenica.mobileffmpeg.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FfmpegConv {

    static void ffmpegTask(String inputFile, String outFile, FfmpegListener listener) {
            String ffmpegCommand = "-i " + inputFile + " -c copy -f mpegts " + outFile;
            ExecutorService myExecutor = Executors.newFixedThreadPool(8);

        long executionId = executeAsync(ffmpegCommand, (executionId1, returnCode) -> {
                if (returnCode == RETURN_CODE_SUCCESS) {
                }
                else{

                }
            listener.sendResult(Config.getLastCommandOutput());
            }, myExecutor);

    }

}
