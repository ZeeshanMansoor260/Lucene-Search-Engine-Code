package webInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class ExePythonW2V {
	public String exeW2V(String cmd) {
//		String result = GoodWindowsExec.main(new String[] {"cd D:\\Program Files\\Eclipse Workspace\\LuceneWeb && py word2vec.py distributed_computing"});
//		String result = GoodWindowsExec.main(new String[] {"cd"});
		String result = GoodWindowsExec.main(new String[] {cmd});
		System.out.println(result);
		return result;
	}
}


class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    public static String result;

    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
            	System.out.println(type + ">" + line);    
            	if(type.equals("OUTPUT")) {
            		result = line;
            	}
            }
            	
                
         } catch (IOException ioe)
         {
            ioe.printStackTrace();  
         }
    }
}
class GoodWindowsExec
{
    public static String main(String args[])
    {
    	String resultFinal = "";
        if (args.length < 1)
        {
            System.out.println("USAGE: java GoodWindowsExec <cmd>");
            System.exit(1);
        }

        try
        {            
            String osName = System.getProperty("os.name" );
            System.out.println("OS NAME IS " + osName);
            String[] cmd = new String[4];
            if( osName.equals( "Windows NT" ) )
            {
                cmd[0] = "cmd.exe" ;
                cmd[1] = "/C" ;
                cmd[2] = args[0];
            }
            else if( osName.equals( "Windows 95" ) )
            {
                cmd[0] = "command.com" ;
                cmd[1] = "/C" ;
                cmd[2] = args[0];
            } else if (osName.toUpperCase().trim().contains("WINDOWS")) {
                cmd[0] = "cmd.exe" ;
                cmd[1] = "/C" ;
                cmd[2] = args[0];
                
            }

            Runtime rt = Runtime.getRuntime();
            String command = cmd[0] + " " + cmd[1] + " " + cmd[2];
            System.out.println("Execing " + command);
            Process proc = rt.exec(command);
            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR");            

            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            System.out.println("******Printing Errro******");
            errorGobbler.start();
            System.out.println("******Printing Output******");
            outputGobbler.start();
            
            // any error???
            System.out.println("*****Waiting Started*****");
            int exitVal = proc.waitFor();
            resultFinal = StreamGobbler.result;
            
            System.out.println("*****Waiting Finished*****");
            System.out.println("ExitValue: " + exitVal);        
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
		return resultFinal;
    }
}
