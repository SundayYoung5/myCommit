package logAnalysis;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author sunday
 */

public class LogAnalysis {
    public static void main(String[] args) {
        //使用List存储获取到的数据，去重则将List转成set进行去重
        List<String> allUrls = new ArrayList<>();
        List<String> ipv4Addresses = new ArrayList<>();
        List<String> progressInfoLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("log.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 1. 提取所有 http 或 https 协议的 URL
                extractUrls(line, allUrls);

                // 2. 提取 IPv4 地址
                extractIPv4Addresses(line, ipv4Addresses);

                // 3. 找到所有进度信息
                extractProgressInfo(line, progressInfoLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*======================URL===========================*/
        System.out.println("======================URL===========================");
        for (String url : allUrls) {
            System.out.println(url);
        }
        System.out.println("去重前URL个数: " + allUrls.size());
        Set<String> uniqueUrls = new HashSet<>(allUrls);
        System.out.println("去重后URL个数: " + uniqueUrls.size());

        /*======================IPv4===========================*/
        //IPv4地址去重，使用 Set 去重
        Set<String> uniqueIPv4Addresses = new HashSet<>(ipv4Addresses);
        System.out.println("======================IPv4===========================");
        System.out.println("IPv4地址：");
        for (String ipv4Address : uniqueIPv4Addresses) {
            System.out.println(ipv4Address);
        }
        System.out.println("去重后IPv4地址个数: " + uniqueIPv4Addresses.size());

        /*======================log行===========================*/
        System.out.println("进度信息原始log行:");
        for (String progressLine : progressInfoLines) {
            System.out.println(progressLine);
        }
    }

    @SuppressWarnings("all")
    private static void extractUrls(String line, List<String> allUrls) {
        // 匹配 http 或 https 协议的 URL
        Pattern pattern = Pattern.compile("(https?://[\\w./-]+)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String url = matcher.group(1);
            allUrls.add(url);
        }
    }

    @SuppressWarnings("all")
    private static void extractIPv4Addresses(String line, List<String> ipv4Addresses) {
        // 匹配 IPv4 地址
        Pattern pattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String ipv4Address = matcher.group();
            ipv4Addresses.add(ipv4Address);
        }
    }

    @SuppressWarnings("all")
    private static void extractProgressInfo(String line, List<String> progressInfoLines) {
        // 匹配 (1/89), (2/89) 格式的进度信息
        Pattern pattern1 = Pattern.compile("\\((\\d+)/(\\d+)\\)");
        Matcher matcher1 = pattern1.matcher(line);
        if (matcher1.find()) {
            progressInfoLines.add(line);
        }

        // 匹配 [1/30], [2/30] 格式的进度信息
        Pattern pattern2 = Pattern.compile("\\[(\\d+)/(\\d+)\\]");
        Matcher matcher2 = pattern2.matcher(line);
        if (matcher2.find()) {
            progressInfoLines.add(line);
        }
    }
}

