package oppdaterer;

//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import org.apache.hc.client5.http.classic.HttpClient;
//import org.apache.hc.client5.http.classic.methods.HttpGet;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.ClassicHttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class FileDownloader
{

  public static void main(String[] args)
  {
//    // Replace with your API endpoint URL
////    String apiUrl = "http://52.169.201.122:8080/get-file?filePath=A_Scenarios%2Fsc_atc_DATC_til_FATC_bkj.xml";
//
//    // Specify the local file path where you want to save the downloaded file
//    String localFilePath = "downloads\\kart2.txt";
//
//    String encodedFilePath = null;
//
//    try
//    {
//      encodedFilePath = URLEncoder.encode("kart2.txt", StandardCharsets.UTF_8.toString());
//    } catch (UnsupportedEncodingException ex)
//    {
//      Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
//    }
//
//    String apiUrl = "http://52.169.201.122:8080/get-file?filePath=" + encodedFilePath;
//
//    HttpClient httpClient = HttpClients.createDefault();
//    HttpGet httpGet = new HttpGet(apiUrl);
//    try ( ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpGet))
//    {
//      if (response.getCode() == 200)
//      {
//        try ( InputStream inputStream = response.getEntity().getContent();  OutputStream outputStream = new FileOutputStream(localFilePath))
//        {
//          byte[] buffer = new byte[1024];
//          int bytesRead;
//          while ((bytesRead = inputStream.read(buffer)) != -1)
//          {
//            outputStream.write(buffer, 0, bytesRead);
//          }
//          System.out.println("File downloaded successfully to: " + localFilePath);
//        }
//      } else
//      {
//        System.err.println("HTTP error! Status: " + response.getCode());
//      }
//
//    } catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }
}
