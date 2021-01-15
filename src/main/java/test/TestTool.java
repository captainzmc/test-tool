package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestTool {

    public static void main(String [] args) throws Exception{
        if(args.length==0 || (args.length > 0 && args[0].contains("help"))) {
            System.out.println("Usage:");
            System.out.println("     -model  <concurrent or serial>   Model of execute commands in the config file");
            System.out.println("     -file   <file1>                  The path of config file");
        } else if (args.length == 4) {
            if (args[0].contains("model") && args[2].contains("file")) {
               if(args[1].contains("concurrent")) {
                   long startTime = System.currentTimeMillis();
                   concurrentTest((args[3]));
                   long stopTime = System.currentTimeMillis();
                   System.out.println("#####Total Execute Time = "+(stopTime-startTime) +"ms#######");
               }
                if(args[1].contains("serial")) {
                    long startTime = System.currentTimeMillis();
                    serialTest((args[3]));
                    long stopTime = System.currentTimeMillis();
                    System.out.println("#####Total Execute Time = "+(stopTime-startTime) +"ms#######");
                }
            }
        } else {
            throw new Exception("Parameter is not standard, the test cannot be executed. " +
                "Can use -help to see the support");
        }

    }
    /* Run the process of the commands. */
    public static Process startProcess(String[] cmdArray1)
            throws Exception {
        Process process;

        ProcessBuilder builder = new ProcessBuilder(cmdArray1);
        builder.redirectErrorStream(true);
        process = builder.start();
        return process;
    }

    public static void printResult(Process process) throws Exception{
        /* Print out the error and standard output for the process. */
        InputStream os;
        InputStream error;
        os = process.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(os));
        String line = null;
        while((line = read.readLine())!=null){
            System.out.println(line);
        }

        error=process.getErrorStream();
        BufferedReader in = new BufferedReader( new InputStreamReader(error));
        String temp = null;
        while((temp = in.readLine()) != null){
            System.out.println(temp);
        }
    }

    // running concurrent test
    public static void concurrentTest(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            InputStreamReader reader;
            BufferedReader br;
            List <Process> processList = new ArrayList<>();
            int taskNum=0;
            try {
                reader = new InputStreamReader(new FileInputStream(file));
                br = new BufferedReader(reader);
                String lineContent = null;
                while((lineContent = br.readLine())!=null){
                    if(lineContent != null) {
                        System.out.println(lineContent);
                        String [] cmdArray = lineContent.split("\\s+");
                        processList.add(startProcess(cmdArray));
                        taskNum ++;
                    }
                }
                br.close();
                reader.close();

                for(Process process : processList) {
                    printResult(process);
                }
                System.out.println("#####Total task num = "+(taskNum) +"#######");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch  (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // running serial test
    public static void serialTest(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            InputStreamReader reader;
            BufferedReader br;
            int taskNum=0;
            try {
                reader = new InputStreamReader(new FileInputStream(file));
                br = new BufferedReader(reader);
                String lineContent = null;
                while((lineContent = br.readLine())!=null){
                    if(lineContent != null) {
                        System.out.println(lineContent);
                        String [] cmdArray = lineContent.split("\\s+");
                        printResult(startProcess(cmdArray));
                        taskNum ++;
                    }
                }
                br.close();
                reader.close();
                System.out.println("#####Total task num = "+(taskNum) +"#######");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch  (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

