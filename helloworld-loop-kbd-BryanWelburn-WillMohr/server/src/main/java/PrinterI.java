import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.Duration;

public class PrinterI implements Demo.Printer

{

    public String printString(String s, com.zeroc.Ice.Current current)
        {
            int final_retrun_num = 0;
            LocalDateTime timeStart = LocalDateTime.now();

            if(s == null || s.length()<=0){
                System.out.println("Error: No input provided.");
                System.out.println(s);
                return "Error:0";
            }

            String[] text = s.split(":");
            System.out.println("Username: "+text[0]);
            System.out.println("Hostname: "+text[1]);

            if(text.length < 3){
                System.out.println("Error: No input provided.");
                System.out.println(s);
                return "Error:0";
            }

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(text[2]);

            int firstInt = 0;

            if(m.find()){
                firstInt = Integer.parseInt(m.group());
            } else {
                System.out.println("Error: No positive intergers.");
                System.out.println(s);
            }

            if(firstInt == 0){
                return "Error:0";
            }
            if(firstInt >= 46){
                System.out.println("Error: number exceeds limit, please choose an integer from 1-45.");
                System.out.println(s);
                return "Error:0";
            }

            for(int i = 0; i < firstInt; i++){
                final_retrun_num = fibonacci(i);
                System.out.print(final_retrun_num + " ");
            }
            System.out.println("\n----------------");

            LocalDateTime timeEnd = LocalDateTime.now();
            Duration totalTime = Duration.between(timeStart, timeEnd);
            // System.out.println(totalTime);

            return totalTime.toString()+":"+final_retrun_num;
        }

        public int fibonacci(int n){
            if(n <= 1){
                return 1;
            }
            return fibonacci(n-1) + fibonacci(n-2);
        }
    }
