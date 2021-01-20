package com.ibbgou.appprocessdemo;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CmdUtils {

    @Nullable
    public static String[] safeExec(final String cmd) {
        String[] result = null;
        ExecutorService executorService = null;
        try {
            final Callable<String[]> callable = new Callable<String[]>() {
                @Override
                public String[] call() throws Exception {
                    return _exec(cmd);
                }
            };
            executorService = Executors.newSingleThreadExecutor();
            final Future<String[]> submit = executorService.submit(callable);
            result = submit.get(120_000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }

        return result;
    }

    private static String[] _exec(String cmd) {
        final List<String> resultLine = new ArrayList<>();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            final BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read the output from the command
            System.out.println("GameLogger. Here is the standard output of the command:\n");
            String line = null;
            while ((line = stdInput.readLine()) != null) {
                resultLine.add(line);
            }

            // Read any errors from the attempted command
            System.out.println("GameLogger. Here is the standard error of the command (if any):\n");
            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            System.err.println("error=" + e.getLocalizedMessage());
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultLine.toArray(new String[0]);
    }

}
